package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.intellij.ui.scale.JBUIScale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

internal object LightsaberLayoutCalculator {

    /**
     * Computes the maximum icon widths for left and right sides from a collection of sabers.
     * Forward blades contribute to the left side; backward blades contribute to the right side.
     */
    private fun computeMaxIconWidths(
        entries: Sequence<Pair<Boolean, Int>>,
    ): Pair<Int, Int> {
        var maxLeft = 0
        var maxRight = 0
        entries.forEach { (isForward, iconWidth) ->
            if (isForward) {
                maxLeft = max(maxLeft, iconWidth)
            } else {
                maxRight = max(maxRight, iconWidth)
            }
        }
        return Pair(maxLeft, maxRight)
    }

    fun compute(
        lightsabers: Lightsabers,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        val (maxLeftIconWidthSingle, maxRightIconWidthSingle) = computeMaxIconWidths(
            lightsabers.lightsabers.withIndex().asSequence().mapNotNull { (index, lightsaber) ->
                if (!lightsaber.isDoubleBladed) lightsaber.blades[0].isForward to iconWidths[index] else null
            }
        )

        val singleBladeLength = width - maxLeftIconWidthSingle - maxRightIconWidthSingle
        val singleRightBladeX = width - maxRightIconWidthSingle

        return lightsabers.lightsabers.mapIndexed { index, lightsaber ->
            val iconWidth = iconWidths[index]

            when {
                !showIcon || singleBladeLength < iconWidth ->
                    LightsaberLayout(
                        listOf(computeNoHiltBlade(lightsaber, width, amountFull, lightsaber.blades[0].isForward)),
                        null,
                    )

                lightsaber.isDoubleBladed -> {
                    val doubleBladeLength = singleBladeLength - iconWidth
                    if (doubleBladeLength < iconWidth) {
                        LightsaberLayout(
                            listOf(computeNoHiltBlade(lightsaber, width, amountFull, lightsaber.blades[0].isForward)),
                            null,
                        )
                    } else {
                        val center = (maxLeftIconWidthSingle + singleRightBladeX) / 2
                        computeDoubleBladedLayout(
                            lightsaber,
                            center - iconWidth / 2,
                            center + iconWidth / 2,
                            doubleBladeLength,
                            width,
                            amountFull,
                        )
                    }
                }

                else -> {
                    val blade = lightsaber.blades[0]
                    val isLeftSide = blade.isForward
                    val bladeX = if (isLeftSide) maxLeftIconWidthSingle else singleRightBladeX

                    computeSingleBladeLayout(
                        lightsaber,
                        blade,
                        iconWidth,
                        width,
                        amountFull,
                        true,
                        singleBladeLength,
                        if (isLeftSide) 0 else bladeX,
                    )
                }
            }
        }
    }

    /**
     * Computes layout for all CombinedIds in a selected variant.
     * For jar-kai entities, individual blades use full available width minus only the hilt length.
     *
     * @param lightsabers The lightsaber entity with declared variants
     * @param selectedVariantIndex The index of the variant to render (from LightsaberVariantSelector)
     * @param iconWidths List of icon widths for each lightsaber hilt
     * @param width Total available width for rendering
     * @param amountFull Progress bar fill amount
     * @param showIcon Whether to show hilt icons
     * @return List of LightsaberLayout for the selected variant's CombinedIds
     */
    fun computeVariantLayout(
        lightsabers: Lightsabers,
        selectedVariantIndex: Int,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        val variant = lightsabers.variants.getOrNull(selectedVariantIndex) ?: lightsabers.variants.first()

        return computeVariantLayouts(lightsabers, variant, iconWidths, width, amountFull, showIcon)
    }

    /**
     * Computes layouts for all CombinedIds in a selected variant.
     */
    private fun computeVariantLayouts(
        lightsabers: Lightsabers,
        variant: List<CombinedId>,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        if (lightsabers.isJarKai) {
            return computeJarKaiVariantLayouts(lightsabers, variant, iconWidths, width, amountFull, showIcon)
        }

        val grouped = variant.groupBy { it.lightsaberId }
        val isFullDoubleBlade = grouped.size == 1 &&
            grouped.values.any { ids ->
                ids.map { it.bladeId }.toSet() == setOf(0, 1)
            }

        return if (isFullDoubleBlade) {
            // Both blades of same double-bladed saber: use centered layout
            val lightsaber = lightsabers.lightsabers.first()
            val iconWidth = iconWidths.first()
            computeDoubleBladedVariantLayout(lightsaber, iconWidth, width, amountFull, showIcon)
        } else {
            variant.map { combinedId ->
                val lightsaber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                    ?: lightsabers.lightsabers.first()
                val iconWidth = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()

                computeVariantBladeLayout(
                    lightsaber,
                    combinedId.bladeId,
                    iconWidth,
                    width,
                    amountFull,
                    showIcon,
                )
            }
        }
    }

    /**
     * Computes layouts for jar-kai variants: single blade or multiple sabers.
     */
    private fun computeJarKaiVariantLayouts(
        lightsabers: Lightsabers,
        variant: List<CombinedId>,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        // For multiple jar-kai sabers, account for all hilts so blades stop at the other hilt's emitter
        val (maxLeftIconWidth, maxRightIconWidth) = if (variant.size > 1) {
            computeMaxIconWidths(
                variant.asSequence().map { combinedId ->
                    val saber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                        ?: lightsabers.lightsabers.first()
                    val iconW = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()
                    val blade = saber.blades.getOrNull(combinedId.bladeId) ?: saber.blades.first()
                    blade.isForward to iconW
                },
            )
        } else {
            Pair(0, 0)
        }

        return variant.map { combinedId ->
            val lightsaber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                ?: lightsabers.lightsabers.first()
            val iconWidth = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()

            computeVariantBladeLayout(
                lightsaber,
                combinedId.bladeId,
                iconWidth,
                width,
                amountFull,
                showIcon,
                maxLeftIconWidth,
                maxRightIconWidth,
            )
        }
    }

    /**
     * Unified helper for computing a single-blade layout with hilt icon.
     * All single-blade cases (standard, variant, jar-kai) reduce to this computation once
     * the available blade length and hilt position are determined.
     *
     * @param lightsaber The lightsaber entity
     * @param blade The specific blade to render
     * @param iconWidth Width of the hilt icon
     * @param width Total available width for rendering
     * @param amountFull Progress bar fill amount
     * @param showIcon Whether to show hilt icons
     * @param maxBladeLength Available length for the blade (after accounting for all hilts)
     * @param hiltX X position of the hilt icon
     * @return LightsaberLayout with blade geometry and hilt geometry
     */
    private fun computeSingleBladeLayout(
        lightsaber: Lightsaber,
        blade: Blade,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
        maxBladeLength: Int,
        hiltX: Int,
    ): LightsaberLayout {
        val isLeftSide = blade.isForward

        return if (showIcon && width >= iconWidth) {
            val maxBladeWidth = maxBladeLength + abs(JBUIScale.scale(blade.xBlade))
            val bladeWidth = computeBladeWidth(amountFull, maxBladeWidth, width, blade.isShoto)
            val borderWidth = computeBorderWidth(maxBladeWidth, blade.isShoto)

            // Blade starts at hilt emitter, not at hilt start
            val anchorX = if (isLeftSide) {
                hiltX + iconWidth + JBUIScale.scale(blade.xBlade)
            } else {
                hiltX + JBUIScale.scale(blade.xBlade)
            }

            LightsaberLayout(
                listOf(computeBladeGeometry(lightsaber, blade, anchorX, bladeWidth, borderWidth, isLeftSide)),
                HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)),
            )
        } else {
            LightsaberLayout(
                listOf(computeNoHiltBlade(lightsaber, width, amountFull, isLeftSide)),
                if (showIcon) HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)) else null,
            )
        }
    }

    /**
     * Computes layout for a single blade variant from a CombinedId.
     * For jar-kai variants with multiple hilts, [maxLeftIconWidth] and [maxRightIconWidth] account for all hilts
     * so blades stop at the other hilt's emitter. For non-jar-kai variants, these default to 0.
     */
    private fun computeVariantBladeLayout(
        lightsaber: Lightsaber,
        bladeId: Int,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
        maxLeftIconWidth: Int = 0,
        maxRightIconWidth: Int = 0,
    ): LightsaberLayout {
        val blade = lightsaber.blades.getOrNull(bladeId) ?: lightsaber.blades.first()

        // Hilt positioning: forward blades at the left edge, backward blades at the right edge
        val hiltX = if (blade.isForward) 0 else width - iconWidth

        // When multiple hilts are present, account for all of them so blades stop at the other hilt's emitter;
        // when only one hilt is rendered, use just that hilt's width
        val leftIconWidth = max(maxLeftIconWidth, if (blade.isForward) iconWidth else 0)
        val rightIconWidth = max(maxRightIconWidth, if (!blade.isForward) iconWidth else 0)
        val singleBladeLength = width - leftIconWidth - rightIconWidth

        return computeSingleBladeLayout(
            lightsaber,
            blade,
            iconWidth,
            width,
            amountFull,
            showIcon,
            singleBladeLength,
            hiltX,
        )
    }

    /**
     * Computes a centered layout for BOTH blades of a double-bladed saber in variant mode.
     * Hilt is centered; blades extend left and right from hilt edges.
     */
    private fun computeDoubleBladedVariantLayout(
        lightsaber: Lightsaber,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        val leftBlade = lightsaber.blades[0]
        val rightBlade = lightsaber.blades[1]
        val centerHiltX = (width - iconWidth) / 2

        // Use full available length minus hilt
        val maxBladeWidth = width - iconWidth

        val (leftBladeWidth, leftBorderWidth) = computeDoubleBladedSide(leftBlade, maxBladeWidth, width, amountFull)
        val (rightBladeWidth, rightBorderWidth) = computeDoubleBladedSide(rightBlade, maxBladeWidth, width, amountFull)

        val leftBladeAnchor = centerHiltX + JBUIScale.scale(leftBlade.xBlade)
        val rightBladeAnchor = centerHiltX + iconWidth + JBUIScale.scale(rightBlade.xBlade)

        return listOf(
            LightsaberLayout(
                listOf(
                    BladeGeometry(
                        leftBlade,
                        x = leftBladeAnchor - leftBladeWidth.roundToInt(),
                        y = JBUIScale.scale(lightsaber.yShift + leftBlade.yBlade),
                        width = leftBladeWidth.roundToInt(),
                        height = JBUIScale.scale(leftBlade.bladeSize),
                        borderX = leftBladeAnchor - leftBorderWidth.roundToInt(),
                        borderWidth = leftBorderWidth.roundToInt(),
                    ),
                    BladeGeometry(
                        rightBlade,
                        x = rightBladeAnchor,
                        y = JBUIScale.scale(lightsaber.yShift + rightBlade.yBlade),
                        width = rightBladeWidth.roundToInt(),
                        height = JBUIScale.scale(rightBlade.bladeSize),
                        borderX = rightBladeAnchor,
                        borderWidth = rightBorderWidth.roundToInt(),
                    ),
                ),
                if (showIcon && width >= iconWidth) {
                    HiltGeometry(centerHiltX, JBUIScale.scale(lightsaber.yShift))
                } else {
                    null
                },
            ),
        )
    }

    /**
     * Computes blade width and border for one side of a double-bladed saber.
     * The progress amount is split between two blades (divided by 2), and shoto blades are halved again.
     */
    private fun computeDoubleBladedSide(
        blade: Blade,
        maxBladeWidth: Int,
        width: Int,
        amountFull: Int,
    ): Pair<Float, Float> {
        val length = maxBladeWidth + abs(JBUIScale.scale(blade.xBlade))
        val ratio = length.toFloat() / width
        var bladeWidth = (amountFull * ratio / 2)
        if (blade.isShoto) bladeWidth /= 2

        val maxBladeWidthWithOffset = maxBladeWidth.toFloat() + abs(JBUIScale.scale(blade.xBlade)).toFloat()
        val borderWidth = if (blade.isShoto) maxBladeWidthWithOffset / 4 else maxBladeWidthWithOffset / 2

        return Pair(bladeWidth, borderWidth)
    }

    private fun computeNoHiltBlade(
        lightsaber: Lightsaber,
        width: Int,
        amountFull: Int,
        isLeftSide: Boolean,
    ): BladeGeometry {
        val blade = lightsaber.blades[0]
        val maxBladeWidth = if (blade.isShoto) width / 2 else width
        val bladeWidth = if (blade.isShoto) amountFull / 2 else amountFull

        return BladeGeometry(
            blade,
            x = if (isLeftSide) 0 else width - bladeWidth,
            y = JBUIScale.scale(lightsaber.yShift + blade.yBlade),
            width = bladeWidth,
            height = JBUIScale.scale(blade.bladeSize),
            borderX = if (isLeftSide) 0 else width - maxBladeWidth,
            borderWidth = maxBladeWidth,
        )
    }


    private fun computeBladeWidth(amountFull: Int, maxBladeWidth: Int, width: Int, isShoto: Boolean): Int {
        val bladeWidth = (amountFull * maxBladeWidth.toFloat() / width).roundToInt()
        return if (isShoto) bladeWidth / 2 else bladeWidth
    }

    private fun computeBorderWidth(maxBladeWidth: Int, isShoto: Boolean): Float =
        if (isShoto) maxBladeWidth / 2f else maxBladeWidth.toFloat()

    private fun computeBladeGeometry(
        lightsaber: Lightsaber,
        blade: Blade,
        anchorX: Int,
        bladeWidth: Int,
        borderWidth: Float,
        extendsRight: Boolean,
    ): BladeGeometry = BladeGeometry(
        blade,
        x = if (extendsRight) anchorX else anchorX - bladeWidth,
        y = JBUIScale.scale(lightsaber.yShift + blade.yBlade),
        width = bladeWidth,
        height = JBUIScale.scale(blade.bladeSize),
        borderX = if (extendsRight) anchorX else anchorX - borderWidth.roundToInt(),
        borderWidth = borderWidth.roundToInt(),
    )

    private fun computeDoubleBladedLayout(
        lightsaber: Lightsaber,
        hiltX: Int,
        secondBladeX: Int,
        maxBladeWidth: Int,
        width: Int,
        amountFull: Int,
    ): LightsaberLayout {
        val leftBlade = lightsaber.blades[0]
        val rightBlade = lightsaber.blades[1]

        val (leftBladeWidth, leftBorderWidth) = computeDoubleBladedSide(leftBlade, maxBladeWidth, width, amountFull)
        val (rightBladeWidth, rightBorderWidth) = computeDoubleBladedSide(rightBlade, maxBladeWidth, width, amountFull)

        return LightsaberLayout(
            listOf(
                BladeGeometry(
                    leftBlade,
                    x = JBUIScale.scale(leftBlade.xBlade) + hiltX - leftBladeWidth.roundToInt(),
                    y = JBUIScale.scale(lightsaber.yShift + leftBlade.yBlade),
                    width = leftBladeWidth.roundToInt(),
                    height = JBUIScale.scale(leftBlade.bladeSize),
                    borderX = JBUIScale.scale(leftBlade.xBlade) + hiltX - leftBorderWidth.roundToInt(),
                    borderWidth = leftBorderWidth.roundToInt(),
                ),
                BladeGeometry(
                    rightBlade,
                    x = secondBladeX + JBUIScale.scale(rightBlade.xBlade),
                    y = JBUIScale.scale(lightsaber.yShift + rightBlade.yBlade),
                    width = rightBladeWidth.roundToInt(),
                    height = JBUIScale.scale(rightBlade.bladeSize),
                    borderX = secondBladeX + JBUIScale.scale(rightBlade.xBlade),
                    borderWidth = rightBorderWidth.roundToInt(),
                ),
            ),
            HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)),
        )
    }
}
