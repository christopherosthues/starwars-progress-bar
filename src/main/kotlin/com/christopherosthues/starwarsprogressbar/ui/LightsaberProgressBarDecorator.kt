package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.LinearGradientPaint
import java.awt.Paint
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.JProgressBar
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
        c: JComponent,
        width: Int,
        height: Int,
        amountFull: Int,
        velocity: Float,
        progressBar: JProgressBar,
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

        // TODO: double bladed
        // TODO: Jar'Kai
        if (lightsaber.isDoubleBladed) {
            drawDoubleBladedLightsaber(graphics2D, lightsaber, index, width, height, amountFull)
        } else {
            drawSingleBladedLightsaber(graphics2D, lightsaber, index, width, height, amountFull)
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
        height: Int,
        amountFull: Int
    ) {
        val lightsaberIcon = lightsaberIcons[index]
        val iconWidth = lightsaberIcon.width
        var lightsaberBladeWidth = width - iconWidth - JBUIScale.scale(lightsaber.xShift)
        if (lightsaberBladeWidth < 0) {
            lightsaberBladeWidth = 0
        }
        val ratio = lightsaberBladeWidth.toFloat() / width
        val full = amountFull * ratio * (if (lightsaber.isShoto) 0.5f else 1f)

        // Define colors for the vertical gradient
        val lightsaberColor = lightsaber.color

        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)

        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeX = if (lightsaber.id.isOdd()) JBUIScale.scale(lightsaber.xShift) + iconWidth else width - JBUIScale.scale(lightsaber.xShift) - iconWidth - full
        val bladeSize = JBUIScale.scale(lightsaber.bladeSize)

        val fractions = floatArrayOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
        val colors = arrayOf(lightsaberColor.brighter().brighter(), lightsaberColor, colorMiddle, colorMiddle, lightsaberColor, lightsaberColor.brighter().brighter())
        val gradient = LinearGradientPaint(
            0f, bladeY.toFloat(), 0f, bladeY + bladeSize.toFloat(), fractions, colors
        )

        val arc = bladeSize

        graphics2D.paint = JBColor(
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50),
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50)
        )

        graphics2D.fillRoundRect(bladeX.toInt(), bladeY - JBUIScale.scale(3), full.roundToInt(), bladeSize + JBUIScale.scale(6) , arc, arc)

        graphics2D.paint = gradient

        graphics2D.fillRoundRect(bladeX.toInt(), bladeY, full.roundToInt(), bladeSize , arc, arc)

        val hiltX = if (lightsaber.id.isOdd()) JBUIScale.scale(lightsaber.xShift) else width - JBUIScale.scale(lightsaber.xShift) - iconWidth
        val hiltY = JBUIScale.scale(lightsaber.yShift)
        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }

    private fun drawDoubleBladedLightsaber(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        index: Int,
        width: Int,
        height: Int,
        amountFull: Int
    ) {
        val lightsaberIcon = lightsaberIcons[index]
        val iconWidth = lightsaberIcon.width
        var lightsaberBladeWidth = width - iconWidth
        if (lightsaberBladeWidth < 0) {
            lightsaberBladeWidth = 0
        }
        val ratio = lightsaberBladeWidth.toFloat() / width
        val full = amountFull * ratio * (if (lightsaber.isShoto) 0.5f else 1f) / 2

        val lightsaberColor = lightsaber.color

        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)

        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeSize = JBUIScale.scale(lightsaber.bladeSize)

        val fractions = floatArrayOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
        val colors = arrayOf(lightsaberColor.brighter().brighter(), lightsaberColor, colorMiddle, colorMiddle, lightsaberColor, lightsaberColor.brighter().brighter()) // Corresponding colors
        val gradient = LinearGradientPaint(
            0f, bladeY.toFloat(), 0f, bladeY + bladeSize.toFloat(), fractions, colors
        )

        val arc = bladeSize

        val hiltX = (width - iconWidth + JBUIScale.scale(lightsaber.xShift)) / 2
        var bladeX = hiltX - full.toInt()

        graphics2D.paint = JBColor(
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50),
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50)
        )
        graphics2D.fillRoundRect(bladeX, bladeY - JBUIScale.scale(3), full.roundToInt(), bladeSize + JBUIScale.scale(6) , arc, arc)

        graphics2D.paint = gradient

        graphics2D.fillRoundRect(bladeX, bladeY, full.roundToInt(), bladeSize , arc, arc)

        bladeX = hiltX + iconWidth

        graphics2D.paint = JBColor(
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50),
            Color(lightsaber.color.red, lightsaber.color.green, lightsaber.color.blue, 50)
        )
        graphics2D.fillRoundRect(bladeX, bladeY - JBUIScale.scale(3), full.roundToInt(), bladeSize + JBUIScale.scale(6) , arc, arc)

        graphics2D.paint = gradient

        graphics2D.fillRoundRect(bladeX, bladeY, full.roundToInt(), bladeSize , arc, arc)

        val hiltY = JBUIScale.scale(lightsaber.yShift)
        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }
}
