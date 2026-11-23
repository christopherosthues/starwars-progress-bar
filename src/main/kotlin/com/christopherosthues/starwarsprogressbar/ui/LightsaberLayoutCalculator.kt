package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.selectors.LightsaberVariantSelector
import com.intellij.ui.scale.JBUIScale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

internal object LightsaberLayoutCalculator {

    /**
     * Computes the layout for the selected variant. Returns one [LightsaberLayout] per distinct
     * saber referenced by the variant, in first-appearance order. A saber whose variant entries
     * select both blades is rendered as a double-bladed lightsaber: centered when the blades point
     * in opposite directions, at the bar edge matching their direction otherwise; all other sabers
     * are placed at the bar edges by their selected blade's direction.
     */
    fun compute(
        lightsabers: Lightsabers,
        selectedVariantIndex: Int,
        iconWidths: List<Int>,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
    ): List<LightsaberLayout> {
        val variant = LightsaberVariantSelector.resolveVariant(lightsabers, selectedVariantIndex)

        // Group the variant's CombinedIds per saber; first-appearance order is preserved.
        val sabersInVariant = variant.groupBy { it.lightsaberId }

        // Sabers whose hilt sits at a bar edge reserve their icon space so blades stop at the other hilt's emitter
        val (maxLeftIconWidth, maxRightIconWidth) = computeMaxIconWidths(
            sabersInVariant.asSequence().mapNotNull { (lightsaberId, combinedIds) ->
                val saber = lightsabers.lightsabers.getOrNull(lightsaberId - 1) ?: lightsabers.lightsabers.first()
                val iconWidth = iconWidths.getOrNull(lightsaberId - 1) ?: iconWidths.first()
                hiltEdge(saber, combinedIds)?.let { edge -> edge to iconWidth }
            },
        )

        return sabersInVariant.map { (lightsaberId, combinedIds) ->
            val saber = lightsabers.lightsabers.getOrNull(lightsaberId - 1) ?: lightsabers.lightsabers.first()
            val iconWidth = iconWidths.getOrNull(lightsaberId - 1) ?: iconWidths.first()

            if (isFullDoubleBladed(combinedIds)) {
                // Both blades of this saber are selected: render as a double-bladed lightsaber. The hilt stays
                // centered when the blades point in opposite directions and moves to the bar edge otherwise.
                val direction = hiltEdge(saber, combinedIds)

                if (direction == null) {
                    computeDoubleBladedLayout(
                        saber,
                        (width - iconWidth) / 2,
                        (width - iconWidth) / 2 + iconWidth,
                        width - iconWidth,
                        width,
                        amountFull,
                        showIcon && width >= iconWidth,
                    )
                } else {
                    computeEdgeDoubleBladedLayout(
                        saber,
                        selectedBlades(saber, combinedIds),
                        iconWidth,
                        width,
                        amountFull,
                        showIcon,
                        maxLeftIconWidth,
                        maxRightIconWidth,
                    )
                }
            } else {
                computeVariantBladeLayout(
                    saber,
                    combinedIds.first().bladeId,
                    iconWidth,
                    width,
                    amountFull,
                    showIcon,
                    maxLeftIconWidth,
                    maxRightIconWidth,
                )
            }
        }
    }

    /**
     * Checks whether the variant entries select both blades of one saber (full double-blade).
     */
    private fun isFullDoubleBladed(combinedIds: List<CombinedId>): Boolean =
        combinedIds.map { it.bladeId }.toSet() == setOf(0, 1)

    /**
     * Returns the selected variant entries' blades of one saber in selection order.
     */
    private fun selectedBlades(saber: Lightsaber, combinedIds: List<CombinedId>): List<Blade> =
        combinedIds.map { saber.blades.getOrNull(it.bladeId) ?: saber.blades.first() }

    /**
     * Determines which bar edge a saber's hilt sits on for its selected variant entries. Forward blades place the
     * hilt at the left edge, backward blades at the right edge; a full double-blade with one blade forward and one
     * backward keeps its hilt centered and returns null.
     */
    private fun hiltEdge(saber: Lightsaber, combinedIds: List<CombinedId>): Boolean? {
        val directions = selectedBlades(saber, combinedIds).mapTo(mutableSetOf()) { it.isForward }
        return if (isFullDoubleBladed(combinedIds)) directions.singleOrNull() else directions.first()
    }

    /**
     * Computes a single-blade layout. All single-blade cases reduce to this once
     * the available blade length and hilt position are determined.
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
            // No hilt when the icon is hidden or does not fit; blade geometry keeps full bar width
            LightsaberLayout(
                listOf(computeNoHiltBladeGeometry(blade, lightsaber, width, amountFull)),
                null,
            )
        }
    }

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
     * Computes a double-bladed layout for a full double-blade whose blades point in the same direction. The hilt sits
     * at the bar edge matching that direction and both blades extend from it toward the opposite end of the bar, so
     * each blade uses single-blade math over the full available length.
     */
    private fun computeEdgeDoubleBladedLayout(
        lightsaber: Lightsaber,
        selectedBlades: List<Blade>,
        iconWidth: Int,
        width: Int,
        amountFull: Int,
        showIcon: Boolean,
        maxLeftIconWidth: Int,
        maxRightIconWidth: Int,
    ): LightsaberLayout {
        val isForward = selectedBlades.first().isForward

        // Hilt at the bar edge matching the blades' direction; both blades extend toward the opposite end of the bar
        val hiltX = if (isForward) 0 else width - iconWidth

        val leftIconWidth = max(maxLeftIconWidth, if (isForward) iconWidth else 0)
        val rightIconWidth = max(maxRightIconWidth, if (!isForward) iconWidth else 0)
        val singleBladeLength = width - leftIconWidth - rightIconWidth

        val layouts = selectedBlades.map { blade ->
            computeSingleBladeLayout(
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

        return LightsaberLayout(layouts.flatMap { it.blades }, layouts.first().hilt)
    }

    private fun computeDoubleBladedSide(
        blade: Blade,
        maxBladeWidth: Int,
        width: Int,
        amountFull: Int,
    ): Pair<Float, Float> {
        val length = (maxBladeWidth + abs(JBUIScale.scale(blade.xBlade))).toFloat()
        val ratio = length / width
        var bladeWidth = amountFull.toFloat() * ratio / 2
        if (blade.isShoto) bladeWidth /= 2

        val borderWidth = if (blade.isShoto) length / 4 else length / 2

        return Pair(bladeWidth, borderWidth)
    }

    private fun computeNoHiltBladeGeometry(
        blade: Blade,
        lightsaber: Lightsaber,
        width: Int,
        amountFull: Int,
    ): BladeGeometry {
        val isLeftSide = blade.isForward
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
        showHilt: Boolean = true,
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
            if (showHilt) HiltGeometry(hiltX, JBUIScale.scale(lightsaber.yShift)) else null,
        )
    }

    private fun computeMaxIconWidths(entries: Sequence<Pair<Boolean, Int>>): Pair<Int, Int> {
        val (forward, backward) = entries.toList().partition { it.first }
        val forwardMax = forward.map { it.second }.maxOfOrNull { it } ?: 0
        val backwardMax = backward.map { it.second }.maxOfOrNull { it } ?: 0
        return Pair(forwardMax, backwardMax)
    }

    private fun computeBladeWidth(amountFull: Int, maxBladeWidth: Int, width: Int, isShoto: Boolean): Int {
        val bladeWidth = (amountFull * maxBladeWidth.toFloat() / width).roundToInt()
        return if (isShoto) bladeWidth / 2 else bladeWidth
    }

    private fun computeBorderWidth(maxBladeWidth: Int, isShoto: Boolean): Float =
        if (isShoto) maxBladeWidth / 2f else maxBladeWidth.toFloat()
}
