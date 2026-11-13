package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade
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
            if (lightsaber.id.isOdd()) {
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
                slot == null -> LightsaberLayout(listOf(computeNoHiltBlade(lightsaber, width, amountFull)), null)
                lightsaber.isDoubleBladed -> computeDoubleBladedLayout(lightsaber, slot, width, amountFull)
                else -> computeSingleBladedLayout(lightsaber, slot, width, amountFull)
            }
        }
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
            BladeSlot(if (lightsaber.id.isOdd()) maxLeftIconWidthSingle else singleRightBladeX, 0, singleBladeLength)
        } else {
            null
        }
    }

    private fun computeNoHiltBlade(lightsaber: Lightsaber, width: Int, amountFull: Int): BladeGeometry {
        val blade = lightsaber.blades[0]
        val maxBladeWidth = if (blade.isShoto) width / 2 else width
        val bladeWidth = if (blade.isShoto) amountFull / 2 else amountFull
        val isLeftSide = lightsaber.id.isOdd()

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
        val isLeftSide = lightsaber.id.isOdd()
        val length = slot.maxBladeWidth + abs(JBUIScale.scale(blade.xBlade))
        val ratio = length.toFloat() / width
        val full = amountFull * ratio
        val scaledAmountFull = if (blade.isShoto) full / 2 else full
        val anchorX = JBUIScale.scale(blade.xBlade) + slot.bladeX
        val bladeWidth = scaledAmountFull.roundToInt()
        val maxBladeWidth = slot.maxBladeWidth.toFloat() + abs(JBUIScale.scale(blade.xBlade)).toFloat()
        val borderWidth = if (blade.isShoto) maxBladeWidth / 2 else maxBladeWidth

        return LightsaberLayout(
            listOf(
                BladeGeometry(
                    blade,
                    x = if (isLeftSide) anchorX else anchorX - bladeWidth,
                    y = JBUIScale.scale(lightsaber.yShift + blade.yBlade),
                    width = bladeWidth,
                    height = JBUIScale.scale(blade.bladeSize),
                    borderX = if (isLeftSide) anchorX else anchorX - borderWidth.roundToInt(),
                    borderWidth = borderWidth.roundToInt(),
                ),
            ),
            HiltGeometry(if (isLeftSide) 0 else slot.bladeX, JBUIScale.scale(lightsaber.yShift)),
        )
    }

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
