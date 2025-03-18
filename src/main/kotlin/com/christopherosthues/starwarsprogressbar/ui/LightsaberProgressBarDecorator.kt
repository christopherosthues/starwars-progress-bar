package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
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

private const val LIGHTSABER_PROGRESSBAR_HEIGHT = 10

internal class LightsaberProgressBarDecorator(private val starWarsState: () -> StarWarsState?) {
    private lateinit var lightsaberIcon: ColoredImageComponent

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

    internal fun update(lightsaber: Lightsaber) {
        lightsaberIcon = ColoredImageComponent(StarWarsResourceLoader.getVehicleImage(lightsaber.fileName))
    }

    internal fun getHeight(): Int = JBUIScale.scale(LIGHTSABER_PROGRESSBAR_HEIGHT)

    internal fun paintProgressBar(
        lightsaber: Lightsaber,
        graphics2D: Graphics2D,
        c: JComponent,
        width: Int,
        height: Int,
        amountFull: Int,
        velocity: Float,
        progressBar: JProgressBar,
    ) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip

        // Enable anti-aliasing for smoother rendering
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Define colors for the vertical gradient
        val colorTop = lightsaber.color.brighter().brighter()
        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)
        val colorBottom = JBColor(Color(255, 255, 255, 80), Color(255, 255, 255, 80))


        // Define LinearGradientPaint (Vertical Gradient)
        val fractions = floatArrayOf(0f, 0.1f, 0.25f, 0.33f, 0.66f, 0.75f, 0.9f, 1.0f) // Positions: Top, Middle, Bottom
        val colors = arrayOf(colorBottom, lightsaber.color, colorTop, colorMiddle, colorMiddle, colorTop, lightsaber.color, colorBottom) // Corresponding colors
        val gradient = LinearGradientPaint(
            0f, 0f, 0f, height.toFloat(), fractions, colors
        )

        val color = graphics2D.color

        // Define arc radius (only for the right side)
        val arc = height

//        // Optional: Add a glowing effect around the saber
//        graphics2D.color = Color(50, 100, 255, 80) // Transparent blue glow
//        graphics2D.fillRoundRect(0, 0, progressWidth, height, arc, arc)

        graphics2D.color = color
        graphics2D.paint = gradient

        // Draw the progress bar with a rounded right tip
        graphics2D.fillRoundRect(0, 0, amountFull, height, arc, arc)

        // Add a clipping mask to make the left side flat
        graphics2D.setClip(0, 0, amountFull - arc / 2, height)
        graphics2D.fillRect(0, 0, amountFull, height) // Force flat left edge

        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun drawProgress(
        vehicle: StarWarsVehicle,
        width: Int,
        height: Int,
        progress: Int,
        velocity: Float,
        graphics2D: Graphics2D,
        rectangle2D: RoundRectangle2D,
        progressBar: JProgressBar,
    ) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val movingRight = velocity >= 0
        graphics2D.paint = getFillPaint(vehicle)
        graphics2D.clip =
            if (movingRight) Rectangle(progress, height) else Rectangle(progress, 0, progressBar.width, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun getFillPaint(vehicle: StarWarsVehicle): Paint = vehicle.color

    private fun drawBorder(rectangle2D: RoundRectangle2D, graphics2D: Graphics2D, progressBar: JProgressBar) {
        val color = graphics2D.color
        val stroke = graphics2D.stroke
        graphics2D.color = progressBar.foreground
        graphics2D.stroke = BasicStroke(2f)
        graphics2D.draw(rectangle2D)
        graphics2D.color = color
        graphics2D.stroke = stroke
    }
}
