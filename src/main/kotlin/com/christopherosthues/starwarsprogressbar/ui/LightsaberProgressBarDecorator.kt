package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import java.awt.Color
import java.awt.Graphics2D
import java.awt.LinearGradientPaint
import java.awt.Paint
import java.awt.RenderingHints
import kotlin.math.roundToInt

private const val LIGHTSABER_PROGRESSBAR_HEIGHT = 20

internal class LightsaberProgressBarDecorator(private val starWarsState: () -> StarWarsState?) {
    private var lightsaberIcons: MutableList<ColoredImageComponent> = mutableListOf()

//    override fun paint(g: Graphics?, c: JComponent?) {
////        val g2 = g as Graphics2D
////        val width: Int = progressBar.width
////        val height: Int = progressBar.height
////
////        // Enable anti-aliasing for smooth edges
////        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
////
////
////        // Define colors for the lightsaber effect
////        val color1: Color = Color(50, 100, 255) // Blue (top)
////        val color2: Color = Color(230, 240, 255) // Almost white (center)
////        val color3: Color = Color(50, 100, 255) // Blue (bottom)
////
////        // Create a horizontal gradient for the glowing effect
////        val gradient = GradientPaint(
////            0f, (height / 2).toFloat(), color1,
////            (width / 2).toFloat(), (height / 2).toFloat(), color2, true
////        )
////
////        g2.paint = gradient
////
////        // Draw the rounded progress bar
////        val arc = height // Make the edges round
////        val progressWidth = (width * (progressBar.getPercentComplete())).toInt()
////
////        g2.fillRoundRect(0, 0, progressWidth, height, arc, arc)
////
////        // Optional: Add a glowing effect around the saber
////        g2.color = Color(50, 100, 255, 80) // Transparent blue glow
////        g2.fillRoundRect(0, 0, progressWidth, height, arc, arc)
//        val g2 = g as Graphics2D
//        val width: Int = progressBar.width
//        val height: Int = progressBar.height
//
//
//        // Enable anti-aliasing for smoother rendering
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//
//        // Define colors for the vertical gradient
//        val colorTop = Color(50, 100, 255) // Blue (top)
//        val colorMiddle = Color(230, 240, 255) // Almost white (middle)
//        val colorBottom = Color(50, 100, 255) // Blue (bottom)
//
//
//        // Define LinearGradientPaint (Vertical Gradient)
//        val fractions = floatArrayOf(0.0f, 0.25f, 0.75f, 1.0f) // Positions: Top, Middle, Bottom
//        val colors = arrayOf(colorTop, colorMiddle, colorMiddle, colorBottom) // Corresponding colors
//        val gradient = LinearGradientPaint(
//            0f, 0f, 0f, height.toFloat(), fractions, colors
//        )
//
//        val color = g2.color
//
//        // Progress bar width based on current progress
//        val progressWidth = (width * progressBar.getPercentComplete()).toInt()
//
//        // Define arc radius (only for the right side)
//        val arc = height
//
////        // Optional: Add a glowing effect around the saber
////        g2.color = Color(50, 100, 255, 80) // Transparent blue glow
////        g2.fillRoundRect(0, 0, progressWidth, height, arc, arc)
//
//        g2.color = color
//        g2.paint = gradient
//
//        // Draw the progress bar with a rounded right tip
//        g2.fillRoundRect(0, 0, progressWidth, height, arc, arc)
//
//        // Add a clipping mask to make the left side flat
//        g2.setClip(0, 0, progressWidth - arc / 2, height)
//        g2.fillRect(0, 0, progressWidth, height) // Force flat left edge
//    }

    internal fun update(lightsabers: Lightsabers) {
        lightsaberIcons.clear()
        lightsabers.lightsabers.forEach {
            if (lightsabers.isJarKai) {
                lightsaberIcons.add(ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName + "_${it.id}")))
            } else {
                lightsaberIcons.add(ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName)))
            }
        }
    }

    internal fun getHeight(): Int = JBUIScale.scale(LIGHTSABER_PROGRESSBAR_HEIGHT)

    internal fun paintProgressBar(
        lightsabers: Lightsabers,
        graphics2D: Graphics2D,
        width: Int,
        height: Int,
        amountFull: Int,
    ) {
        lightsabers.lightsabers.indices.forEach {
            drawLightsaber(graphics2D, lightsabers, it, width, height, amountFull)
        }
    }

    private fun drawLightsaber(
        graphics2D: Graphics2D,
        lightsabers: Lightsabers,
        index: Int,
        width: Int,
        height: Int,
        amountFull: Int
    ) {
        val lightsaber = lightsabers.lightsabers[index]
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val color = graphics2D.color

        // Enable anti-aliasing for smoother rendering
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (lightsaber.isDoubleBladed) {
            drawDoubleBladedLightsaber(graphics2D, lightsaber, index, width, amountFull)
        } else {
            drawSingleBladedLightsaber(graphics2D, lightsaber, index, width, amountFull)
        }

        graphics2D.color = color
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun drawSingleBladedLightsaber(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        index: Int,
        width: Int,
        amountFull: Int
    ) {
        val lightsaberIcon = lightsaberIcons[index]
        val iconWidth = lightsaberIcon.width

        val drawHilt = shouldDrawSingleBladedLightsaberHilt(width, iconWidth)

        if (drawHilt) {
            var lightsaberBladeWidth = width - iconWidth - JBUIScale.scale(lightsaber.xShift)
            if (lightsaberBladeWidth < 0) {
                lightsaberBladeWidth = 0
            }
            val ratio = lightsaberBladeWidth.toFloat() / width
            val full = amountFull * ratio * (if (lightsaber.isShoto) 0.5f else 1f)
            drawSingleBladedLightsaberWithHilt(graphics2D, lightsaber, lightsaberIcon, iconWidth, width, full)
        } else {
            drawLightsaberWithoutHilt(graphics2D, lightsaber, width, amountFull)
        }
    }

    private fun shouldDrawSingleBladedLightsaberHilt(width: Int, iconWidth: Int): Boolean {
        return (starWarsState()?.showVehicle ?: DEFAULT_SHOW_VEHICLE) && width - iconWidth >= iconWidth
    }

    private fun drawSingleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        lightsaberIcon: ColoredImageComponent,
        iconWidth: Int,
        width: Int,
        full: Float
    ) {
        val bladeX = if (lightsaber.id.isOdd()) JBUIScale.scale(lightsaber.xShift) + iconWidth else width - JBUIScale.scale(
            lightsaber.xShift
        ) - iconWidth - full.roundToInt()
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)
        val bladeWidth = full.roundToInt()

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth, bladeHeight)

        val hiltX =
            if (lightsaber.id.isOdd()) JBUIScale.scale(lightsaber.xShift) else width - JBUIScale.scale(lightsaber.xShift) - iconWidth
        val hiltY = JBUIScale.scale(lightsaber.yShift)
        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }

    private fun drawLightsaberWithoutHilt(graphics2D: Graphics2D, lightsaber: Lightsaber, width: Int, amountFull: Int) {
        val bladeX = if (lightsaber.id.isOdd()) 0 else width
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)
        val bladeWidth = amountFull

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth, bladeHeight)
    }

    private fun drawBlade(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        bladeX: Int,
        bladeY: Int,
        bladeWidth: Int,
        bladeHeight: Int,
    ) {
        // Define colors for the vertical gradient
        val bladePaint = bladePaint(lightsaber, bladeY, bladeHeight)

        val arc = bladeHeight

        graphics2D.paint = bladeGlow(lightsaber)

        graphics2D.fillRoundRect(bladeX, bladeY - JBUIScale.scale(3), bladeWidth, bladeHeight + JBUIScale.scale(6) , arc, arc)

        graphics2D.paint = bladePaint

        graphics2D.fillRoundRect(bladeX, bladeY, bladeWidth, bladeHeight , arc, arc)
    }

    private fun bladeGlow(lightsaber: Lightsaber): Paint {
        val lightsaberColor = lightsaber.color

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return lightsaberColor
        }

        return JBColor(
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50),
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50)
        )
    }

    private fun bladePaint(lightsaber: Lightsaber, bladeY: Int, bladeHeight: Int): Paint {
        val lightsaberColor = lightsaber.color

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return lightsaberColor
        }

        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)

        val fractions = floatArrayOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
        val colors = arrayOf(
            lightsaberColor.brighter().brighter(),
            lightsaberColor,
            colorMiddle,
            colorMiddle,
            lightsaberColor,
            lightsaberColor.brighter().brighter()
        )
        val gradient = LinearGradientPaint(0f, bladeY.toFloat(), 0f, bladeY + bladeHeight.toFloat(), fractions, colors)
        return gradient
    }

    private fun drawDoubleBladedLightsaber(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        index: Int,
        width: Int,
        amountFull: Int
    ) {
        val lightsaberIcon = lightsaberIcons[index]
        val iconWidth = lightsaberIcon.width

        val shouldDrawBothBlades = shouldDrawBothBlades(width, iconWidth)

        if (shouldDrawBothBlades) {
            var lightsaberBladeWidth = width - iconWidth - JBUIScale.scale(lightsaber.xShift) // TODO: compute biggest icon single bladed
            if (lightsaberBladeWidth < 0) {
                lightsaberBladeWidth = 0
            }
            val ratio = lightsaberBladeWidth.toFloat() / width
            val fullSingleBlade = amountFull * ratio * (if (lightsaber.isShoto) 0.5f else 1f)
            val full = fullSingleBlade / 2
            drawDoubleBladedLightsaberWithHilt(graphics2D, lightsaber, lightsaberIcon, iconWidth, width, full)
        } else {
            drawSingleBladedLightsaber(graphics2D, lightsaber, index, width, amountFull)
        }
    }

    private fun shouldDrawBothBlades(width: Int, iconWidth: Int): Boolean {
        return (starWarsState()?.showVehicle ?: DEFAULT_SHOW_VEHICLE) && (width - iconWidth) / 2 >= iconWidth
    }

    private fun drawDoubleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        lightsaberIcon: ColoredImageComponent,
        iconWidth: Int,
        width: Int,
        full: Float
    ) {
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeSize = JBUIScale.scale(lightsaber.bladeSize)
        val hiltX = (width - iconWidth + JBUIScale.scale(lightsaber.xShift)) / 2
        var bladeX = hiltX - full.toInt()

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, full.roundToInt(), bladeSize)

        bladeX = hiltX + iconWidth

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, full.roundToInt(), bladeSize)

        val hiltY = JBUIScale.scale(lightsaber.yShift)
        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }
}
