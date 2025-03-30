package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_ICON
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
import javax.swing.JComponent
import kotlin.math.abs
import kotlin.math.max
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
        component: JComponent,
        width: Int,
        height: Int,
        amountFull: Int,
    ) {
        var maxRightIconWidth = 0
        var maxLeftIconWidth = 0
        var onlyDoubleBladed = true
        lightsabers.lightsabers.indices.forEach {
            val lightsaberIcon = lightsaberIcons[it]
            val iconWidth = lightsaberIcon.width
            if (it.isEven()) {
                maxLeftIconWidth = max(maxLeftIconWidth, iconWidth)
            } else {
                maxRightIconWidth = max(maxRightIconWidth, iconWidth)
            }
            onlyDoubleBladed = onlyDoubleBladed && lightsabers.lightsabers[it].isDoubleBladed
        }

        val shouldDrawSingleLightsaberWithHilt =
            (starWarsState()?.showIcon ?: DEFAULT_SHOW_ICON) && width - max(
                maxLeftIconWidth,
                maxRightIconWidth
            ) >= max(maxLeftIconWidth, maxRightIconWidth)
        val shouldDrawDoubleBladedLightsaberWithHilt =
            (starWarsState()?.showIcon ?: DEFAULT_SHOW_ICON) && (width - max(
                maxLeftIconWidth,
                maxRightIconWidth
            )) / 2 >= max(maxLeftIconWidth, maxRightIconWidth)
        val singleBladeLength = width - maxLeftIconWidth - maxRightIconWidth
        val ratio = singleBladeLength.toFloat() / width
        val scaledAmountFull = amountFull * ratio
        val scaledAmountFullDoubleBladed = scaledAmountFull / 2
        val leftBladeX = maxLeftIconWidth
        val rightBladeX = width - maxRightIconWidth

        lightsabers.lightsabers.indices.forEach {
            drawLightsaber(
                graphics2D,
                component,
                lightsabers,
                it,
                width,
                height,
                amountFull,
                shouldDrawSingleLightsaberWithHilt,
                shouldDrawDoubleBladedLightsaberWithHilt,
                scaledAmountFullDoubleBladed,
                leftBladeX,
                rightBladeX,
                onlyDoubleBladed
            )
        }
    }

    private fun drawLightsaber(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsabers: Lightsabers,
        index: Int,
        width: Int,
        height: Int,
        amountFull: Int,
        shouldDrawSingleLightsaberWithHilt: Boolean,
        shouldDrawDoubleBladedLightsaberWithHilt: Boolean,
        amountFullDoubleBladed: Float,
        leftBladeX: Int,
        rightBladeX: Int,
        onlyDoubleBladed: Boolean
    ) {
        val lightsaber = lightsabers.lightsabers[index]
        val lightsaberIcon = lightsaberIcons[index]
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val color = graphics2D.color

        // Enable anti-aliasing for smoother rendering
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (lightsaber.isDoubleBladed && shouldDrawDoubleBladedLightsaberWithHilt) {
            drawDoubleBladedLightsaberWithHilt(graphics2D, component, lightsaber, lightsaberIcon, width, amountFullDoubleBladed, amountFull, onlyDoubleBladed)
        } else if (shouldDrawSingleLightsaberWithHilt) {
            drawSingleBladedLightsaberWithHilt(graphics2D, component, lightsaber, lightsaberIcon, width, amountFull, leftBladeX, rightBladeX)
        } else {
            drawLightsaberWithoutHilt(graphics2D, lightsaber, width, amountFull)
        }

        graphics2D.color = color
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun drawSingleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaber: Lightsaber,
        lightsaberIcon: ColoredImageComponent,
        width: Int,
        amountFull: Int,
        leftBladeX: Int,
        rightBladeX: Int,
    ) {
        val singleBladeLength = rightBladeX - leftBladeX + abs(JBUIScale.scale(lightsaber.xBlade))
        val ratio = singleBladeLength.toFloat() / width
        val full = amountFull * ratio

        val scaledAmountFull = if (lightsaber.isShoto) full / 2 else full
        val bladeX = lightsaber.xBlade + if (lightsaber.id.isOdd()) leftBladeX else rightBladeX - scaledAmountFull.roundToInt()
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)
        val bladeWidth = scaledAmountFull.roundToInt()

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth, bladeHeight)

        val hiltX = if (lightsaber.id.isOdd()) 0 else rightBladeX
        val hiltY = JBUIScale.scale(lightsaber.yShift)
        drawHilt(graphics2D, component, lightsaberIcon, hiltX, hiltY)
    }

    private fun drawHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaberIcon: ColoredImageComponent,
        hiltX: Int,
        hiltY: Int
    ) {
        if (starWarsState()?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES) {
            graphics2D.color = component.foreground
            lightsaberIcon.foreground = component.foreground
        } else {
            lightsaberIcon.foreground = null
        }

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

    private fun bladeGlow(lightsaber: Lightsaber): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))
        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return transparent
        }

        val lightsaberColor = lightsaber.color
        return JBColor(
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50),
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50)
        )
    }

    private fun drawDoubleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaber: Lightsaber,
        lightsaberIcon: ColoredImageComponent,
        width: Int,
        full: Float,
        amountFull: Int,
        onlyDoubleBladed: Boolean
    ) {
        var bladeWidth = full;
        val iconWidth = lightsaberIcon.width
        if (onlyDoubleBladed) {
            val ratio = (width - iconWidth).toFloat() / width
            bladeWidth = amountFull * ratio / 2
        } else {
            bladeWidth = full
        }
        bladeWidth = if (lightsaber.isShoto) bladeWidth / 2 else bladeWidth

        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeSize = JBUIScale.scale(lightsaber.bladeSize)
        val hiltX = (width - iconWidth) / 2
        var bladeX = hiltX - bladeWidth.toInt()

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth.roundToInt(), bladeSize)

        bladeX = hiltX + iconWidth

        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth.roundToInt(), bladeSize)

        val hiltY = JBUIScale.scale(lightsaber.yShift)
        drawHilt(graphics2D, component, lightsaberIcon, hiltX, hiltY)
    }
}
