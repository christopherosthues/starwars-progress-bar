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
    private data class BladeSlot(val bladeX: Int, val secondBladeX: Int, val maxBladeWidth: Int)

    fun compute(
        lightsabers: Lightsabers,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        var maxLeftIconWidthSingle = 0
        var maxRightIconWidthSingle = 0
        lightsabers.lightsabers.forEachIndexed { index, lightsaber ->
            if (lightsaber.isDoubleBladed) return@forEachIndexed
            val iconWidth = iconWidths[index]
            if (lightsaber.blades[0].isForward) {
                maxLeftIconWidthSingle = max(maxLeftIconWidthSingle, iconWidth)
            } else {
                maxRightIconWidthSingle = max(maxRightIconWidthSingle, iconWidth)
            }
        }

        val singleBladeLength = width - maxLeftIconWidthSingle - maxRightIconWidthSingle
        val singleRightBladeX = width - maxRightIconWidthSingle

        return lightsabers.lightsabers.mapIndexed { index, lightsaber ->
            val slot = if (showIcon) {
                computeSlot(lightsaber, iconWidths[index], singleBladeLength, maxLeftIconWidthSingle, singleRightBladeX)
            } else {
                null
            }

            when {
                slot == null ->
                    LightsaberLayout(
                        listOf(computeNoHiltBlade(lightsaber, width, amountFull, lightsaber.blades[0].isForward)),
                        null,
                    )

                lightsaber.isDoubleBladed -> computeDoubleBladedLayout(lightsaber, slot, width, amountFull)

                else -> computeSingleBladedLayout(lightsaber, slot, width, amountFull)
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
        if (variant.size == 1) {
            // Single jar-kai blade variant
            val combinedId = variant.first()
            val lightsaber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                ?: lightsabers.lightsabers.first()
            val iconWidth = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()

            return listOf(
                computeJarKaiVariantLayout(
                    lightsaber,
                    combinedId.bladeId,
                    iconWidth,
                    width,
                    amountFull,
                    showIcon,
                ),
            )
        }

        // Multiple jar-kai sabers: account for all hilts so blades stop at the other hilt's emitter
        var maxLeftIconWidthSingle = 0
        var maxRightIconWidthSingle = 0
        variant.forEach { combinedId ->
            val saber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                ?: lightsabers.lightsabers.first()
            val iconW = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()
            val blade = saber.blades.getOrNull(combinedId.bladeId) ?: saber.blades.first()
            if (blade.isForward) {
                maxLeftIconWidthSingle = max(maxLeftIconWidthSingle, iconW)
            } else {
                maxRightIconWidthSingle = max(maxRightIconWidthSingle, iconW)
            }
        }

        return variant.map { combinedId ->
            val lightsaber = lightsabers.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                ?: lightsabers.lightsabers.first()
            val iconWidth = iconWidths.getOrNull(combinedId.lightsaberId - 1) ?: iconWidths.first()

            computeJarKaiVariantLayout(
                lightsaber,
                combinedId.bladeId,
                iconWidth,
                width,
                amountFull,
                showIcon,
                maxLeftIconWidthSingle,
                maxRightIconWidthSingle,
            )
        }
    }

    /**
     * Computes layout for a jar-kai individual blade variant.
     * When maxLeftIconWidthSingle and maxRightIconWidthSingle are provided (multi-saber case),
     * the blade length accounts for all hilts so blades stop at the other hilt's emitter.
     */
    private fun computeJarKaiVariantLayout(
        lightsaber: Lightsaber,
        bladeId: Int,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
        maxLeftIconWidthSingle: Int = 0,
        maxRightIconWidthSingle: Int = 0,
    ): LightsaberLayout {
        val blade = lightsaber.blades.getOrNull(bladeId) ?: lightsaber.blades.first()
        val isLeftSide = blade.isForward

        // Hilt positioning per rendering-contract.md for jar-kai
        val hiltX = if (isLeftSide) 0 else width - iconWidth

        return if (showIcon && width >= iconWidth) {
            // When multiple hilts are present, account for all of them so blades stop at the other hilt's emitter;
            // when only one hilt is rendered, use just that hilt's width
            val leftIconWidth = max(maxLeftIconWidthSingle, if (isLeftSide) iconWidth else 0)
            val rightIconWidth = max(maxRightIconWidthSingle, if (!isLeftSide) iconWidth else 0)
            val singleBladeLength = width - leftIconWidth - rightIconWidth
            val maxBladeWidth = singleBladeLength + abs(JBUIScale.scale(blade.xBlade))
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
     * Direction is taken from [Blade.isForward] (related to lightsaber ID parity: odd IDs are forward, even
     * IDs backward): forward blades get the hilt at the left edge with the blade extending rightward;
     * backward blades get the hilt at the right edge with the blade extending leftward.
     */
    private fun computeVariantBladeLayout(
        lightsaber: Lightsaber,
        bladeId: Int,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): LightsaberLayout {
        val blade = lightsaber.blades.getOrNull(bladeId) ?: lightsaber.blades.first()

        // Direction from the blade itself (odd lightsaber IDs are forward, even IDs backward)
        val isForward = blade.isForward

        // Hilt positioning per FR-003/FR-004: forward blades at the left edge, backward blades at the right edge
        val hiltX = if (isForward) {
            0
        } else {
            width - iconWidth
        }

        return if (showIcon && width >= iconWidth) {
            val maxBladeLength = width - iconWidth
            val maxBladeWidth = maxBladeLength + abs(JBUIScale.scale(blade.xBlade))
            val bladeWidth = computeBladeWidth(amountFull, maxBladeWidth, width, blade.isShoto)
            val borderWidth = computeBorderWidth(maxBladeWidth, blade.isShoto)

            // Blade starts at the hilt emitter: right edge for forward blades, left edge for backward blades
            val anchorX = if (isForward) {
                hiltX + iconWidth + JBUIScale.scale(blade.xBlade)
            } else {
                hiltX + JBUIScale.scale(blade.xBlade)
            }

            LightsaberLayout(
                listOf(computeBladeGeometry(lightsaber, blade, anchorX, bladeWidth, borderWidth, isForward)),
                HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)),
            )
        } else {
            LightsaberLayout(
                listOf(computeNoHiltBlade(lightsaber, width, amountFull, isForward)),
                if (showIcon) HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)) else null,
            )
        }
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

        // Match original computeDoubleBladedLayout: use full available length minus hilt
        val maxBladeWidth = width - iconWidth
        val shotoLengthFactor = 4

        // Left blade: extends LEFT from hilt
        val leftLength = maxBladeWidth + abs(JBUIScale.scale(leftBlade.xBlade))
        val leftRatio = leftLength.toFloat() / width
        var leftBladeWidth = amountFull * leftRatio / 2
        if (leftBlade.isShoto) leftBladeWidth /= 2

        val maxLeftBladeWidth = maxBladeWidth.toFloat() + abs(JBUIScale.scale(leftBlade.xBlade)).toFloat()
        val leftBorderWidth = if (leftBlade.isShoto) {
            maxLeftBladeWidth / shotoLengthFactor
        } else {
            maxLeftBladeWidth / 2
        }

        // Right blade: extends RIGHT from hilt
        val rightLength = maxBladeWidth + abs(JBUIScale.scale(rightBlade.xBlade))
        val rightRatio = rightLength.toFloat() / width
        var rightBladeWidth = amountFull * rightRatio / 2
        if (rightBlade.isShoto) rightBladeWidth /= 2

        val maxRightBladeWidth = maxBladeWidth.toFloat() + abs(JBUIScale.scale(rightBlade.xBlade)).toFloat()
        val rightBorderWidth = if (rightBlade.isShoto) {
            maxRightBladeWidth / shotoLengthFactor
        } else {
            maxRightBladeWidth / 2
        }

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

    private fun computeSlot(
        lightsaber: Lightsaber,
        iconWidth: Int,
        singleBladeLength: Int,
        maxLeftIconWidthSingle: Int,
        singleRightBladeX: Int,
    ): BladeSlot? {
        if (lightsaber.isDoubleBladed) {
            return if (singleBladeLength - iconWidth >= iconWidth) {
                val center = (maxLeftIconWidthSingle + singleRightBladeX) / 2
                BladeSlot(center - iconWidth / 2, center + iconWidth / 2, singleBladeLength - iconWidth)
            } else {
                null
            }
        }

        return if (singleBladeLength >= iconWidth) {
            val bladeX = if (lightsaber.blades[0].isForward) maxLeftIconWidthSingle else singleRightBladeX
            BladeSlot(bladeX, 0, singleBladeLength)
        } else {
            null
        }
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

    private fun computeSingleBladedLayout(
        lightsaber: Lightsaber,
        slot: BladeSlot,
        width: Int,
        amountFull: Int,
    ): LightsaberLayout {
        val blade = lightsaber.blades[0]
        val isLeftSide = blade.isForward
        val maxBladeWidth = slot.maxBladeWidth + abs(JBUIScale.scale(blade.xBlade))
        val bladeWidth = computeBladeWidth(amountFull, maxBladeWidth, width, blade.isShoto)
        val anchorX = JBUIScale.scale(blade.xBlade) + slot.bladeX
        val borderWidth = computeBorderWidth(maxBladeWidth, blade.isShoto)

        return LightsaberLayout(
            listOf(computeBladeGeometry(lightsaber, blade, anchorX, bladeWidth, borderWidth, isLeftSide)),
            HiltGeometry(if (isLeftSide) 0 else slot.bladeX, JBUIScale.scale(lightsaber.yShift)),
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
        slot: BladeSlot,
        width: Int,
        amountFull: Int,
    ): LightsaberLayout {
        val leftBlade = lightsaber.blades[0]
        val rightBlade = lightsaber.blades[1]

        val shotoLengthFactor = 4
        val leftLength = slot.maxBladeWidth + abs(JBUIScale.scale(leftBlade.xBlade))
        val leftRatio = leftLength.toFloat() / width
        var leftBladeWidth = amountFull * leftRatio / 2
        if (leftBlade.isShoto) {
            leftBladeWidth /= 2
        }

        val maxLeftBladeWidth = slot.maxBladeWidth.toFloat() + abs(JBUIScale.scale(leftBlade.xBlade)).toFloat()
        val leftBorderWidth = if (leftBlade.isShoto) {
            maxLeftBladeWidth / shotoLengthFactor
        } else {
            maxLeftBladeWidth / 2
        }

        val rightLength = slot.maxBladeWidth + abs(JBUIScale.scale(rightBlade.xBlade))
        val rightRatio = rightLength.toFloat() / width
        var rightBladeWidth = amountFull * rightRatio / 2
        if (rightBlade.isShoto) {
            rightBladeWidth /= 2
        }

        val maxRightBladeWidth = slot.maxBladeWidth.toFloat() + abs(JBUIScale.scale(rightBlade.xBlade)).toFloat()
        val rightBorderWidth = if (rightBlade.isShoto) {
            maxRightBladeWidth / shotoLengthFactor
        } else {
            maxRightBladeWidth / 2
        }

        return LightsaberLayout(
            listOf(
                BladeGeometry(
                    leftBlade,
                    x = JBUIScale.scale(leftBlade.xBlade) + slot.bladeX - leftBladeWidth.roundToInt(),
                    y = JBUIScale.scale(lightsaber.yShift + leftBlade.yBlade),
                    width = leftBladeWidth.roundToInt(),
                    height = JBUIScale.scale(leftBlade.bladeSize),
                    borderX = JBUIScale.scale(leftBlade.xBlade) + slot.bladeX - leftBorderWidth.roundToInt(),
                    borderWidth = leftBorderWidth.roundToInt(),
                ),
                BladeGeometry(
                    rightBlade,
                    x = slot.secondBladeX + JBUIScale.scale(rightBlade.xBlade),
                    y = JBUIScale.scale(lightsaber.yShift + rightBlade.yBlade),
                    width = rightBladeWidth.roundToInt(),
                    height = JBUIScale.scale(rightBlade.bladeSize),
                    borderX = slot.secondBladeX + JBUIScale.scale(rightBlade.xBlade),
                    borderWidth = rightBorderWidth.roundToInt(),
                ),
            ),
            HiltGeometry(slot.bladeX, JBUIScale.scale(lightsaber.yShift)),
        )
    }
}
