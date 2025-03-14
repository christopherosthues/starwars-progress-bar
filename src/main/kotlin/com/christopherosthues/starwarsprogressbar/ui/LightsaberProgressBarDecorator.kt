package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.*
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_VEHICLE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.selectors.VehicleSelector.selectVehicle
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.JProgressBar

internal class LightsaberProgressBarDecorator(private val starWarsState: () -> StarWarsState?, private var lightsaber: Lightsaber) {
    private var lightsaberIcon = ColoredImageComponent(StarWarsResourceLoader.getVehicleImage(lightsaber.fileName))

    private var velocity = getVelocity()
    private var position = 0
    private var numberOfPasses = 0

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

    internal fun update() {
        lightsaber = selectVehicle(
            starWarsState()?.vehiclesEnabled,
            false,
            starWarsState()?.vehicleSelector ?: DEFAULT_VEHICLE_SELECTOR,
        )
        lightsaberIcon = ColoredImageComponent(StarWarsResourceLoader.getVehicleImage(lightsaber.fileName))
    }

    private fun getVelocity(): Float =
        if (starWarsState()?.sameVehicleVelocity ?: DEFAULT_SAME_VEHICLE_VELOCITY) 1f else lightsaber.velocity

    internal fun getPreferredSize(c: JComponent?, parentWidth: Int): Dimension =
        Dimension(parentWidth, JBUIScale.scale(VEHICLE_PROGRESSBAR_HEIGHT))

    internal fun updatePositionAndVelocity(progressBar: JProgressBar) {
        val actualVelocity = velocity
        val actualPosition: Int = position
        if (velocity < 0) {
            if (position <= 0) {
                updateNumberOfPasses()
                velocity = getVelocity()
                position = 0
            } else {
                position = actualPosition + JBUIScale.scale(velocity).toInt()
                velocity = actualVelocity - 0
            }
        } else if (velocity > 0) {
            if (position >= progressBar.width) {
                updateNumberOfPasses()
                velocity = -getVelocity()
                position = progressBar.width
            } else {
                position = actualPosition + JBUIScale.scale(velocity).toInt()
                velocity = actualVelocity + 0
            }
        }
    }

    private fun updateNumberOfPasses() {
        numberOfPasses++
        if ((
                starWarsState()?.changeVehicleAfterPass
                    ?: DEFAULT_CHANGE_VEHICLE_AFTER_PASS
                ) &&
            numberOfPasses % (
                starWarsState()?.numberOfPassesUntilVehicleChange
                    ?: DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
                ) == 0
        ) {
            update()
        }
    }

    internal fun resetPositionAndVelocity() {
        velocity = getVelocity()
        position = 0
    }

    internal fun paintProgressBar(
        g: Graphics,
        c: JComponent,
        paintDeterminate: Boolean,
        progressBar: JProgressBar,
        starWarsProgressBarUI: StarWarsProgressBarUI,
    ) {
        setProgressBarText(progressBar)
        setToolTipText(progressBar)

        val config = GraphicsUtil.setupAAPainting(g)
        val graphics2D = g as Graphics2D
        val border = progressBar.insets
        val width = progressBar.width
        var height = progressBar.preferredSize.height
        if (isOdd(c.height - height)) {
            height++
        }
        val barRectWidth = width - (border.right + border.left)
        val barRectHeight = height - (border.top + border.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        val amountFull =
            if (paintDeterminate) starWarsProgressBarUI.getAmountFull(barRectWidth, barRectHeight, border) else position

        graphics2D.color = getBackgroundColor(c)
        if (c.isOpaque) {
            g.fillRect(0, 0, width, height)
        }

        // TODO: investigate why progressbar text is not painted after changing theme/look and feel
        val rectangle2D = getRoundRectangle(width, height)
        drawProgress(width, height, amountFull, graphics2D, rectangle2D, progressBar)
        if (starWarsState()?.showVehicle ?: DEFAULT_SHOW_VEHICLE) {
            drawIcon(amountFull, graphics2D, rectangle2D, c)
        }
        drawBorder(rectangle2D, graphics2D, progressBar)
        paintStringIfNeeded(graphics2D, c, height, border, barRectWidth, barRectHeight, amountFull, progressBar, starWarsProgressBarUI)

        config.restore()
    }

    private fun setToolTipText(progressBar: JProgressBar) {
        if (starWarsState()?.showToolTips ?: DEFAULT_SHOW_TOOLTIPS) {
            val localizedName = StarWarsBundle.message(lightsaber.localizationKey)
            if (progressBar.toolTipText != localizedName) {
                progressBar.toolTipText = localizedName
            }
        } else if (!(starWarsState()?.showToolTips ?: DEFAULT_SHOW_TOOLTIPS)) {
            progressBar.toolTipText = ""
        }
    }

    private fun setProgressBarText(progressBar: JProgressBar) {
        progressBar.isStringPainted = starWarsState()?.showVehicleNames ?: DEFAULT_SHOW_VEHICLE_NAMES
        if (starWarsState()?.showVehicleNames ?: DEFAULT_SHOW_VEHICLE_NAMES) {
            val localizedName = StarWarsBundle.message(lightsaber.localizationKey)
            if (progressBar.string != localizedName) {
                progressBar.string = localizedName
            }
        } else if (!(starWarsState()?.showVehicleNames ?: DEFAULT_SHOW_VEHICLE_NAMES)) {
            progressBar.string = ""
        }
    }

    private fun isOdd(value: Int): Boolean = value % 2 == 1

    private fun getBackgroundColor(component: JComponent): Color {
        val parent = component.parent
        return if (parent != null) parent.background else UIUtil.getPanelBackground()
    }

    private fun getRoundRectangle(width: Int, height: Int): RoundRectangle2D {
        val arcLength = JBUIScale.scale(PROGRESSBAR_CORNER_SIZE)
        val offset = JBUIScale.scale(2f)
        return RoundRectangle2D.Float(
            JBUIScale.scale(1f),
            JBUIScale.scale(1f),
            width - offset,
            height - offset,
            arcLength,
            arcLength,
        )
    }

    private fun drawProgress(
        width: Int,
        height: Int,
        progress: Int,
        graphics2D: Graphics2D,
        rectangle2D: RoundRectangle2D,
        progressBar: JProgressBar,
    ) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        graphics2D.paint = getFillPaint()
        graphics2D.clip = Rectangle(progress, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = getTransparencyPaint(progressBar.background, progress)
        graphics2D.clip = Rectangle(progress, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun getTransparencyPaint(backgroundColor: Color, progress: Int): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return transparent
        }

        return if (progress > 0) {
            LinearGradientPaint(
                0f,
                JBUIScale.scale(2f),
                progress.toFloat(),
                JBUIScale.scale(2f),
                floatArrayOf(0f, 1f),
                arrayOf(backgroundColor, transparent),
            )
        } else {
            transparent
        }
    }

    private fun getFillPaint(): Paint = lightsaber.color

    private fun drawIcon(amountFull: Int, graphics2D: Graphics2D, clip: Shape, component: JComponent) {
        val previousClip = graphics2D.clip
        val previousColor = graphics2D.color
        graphics2D.clip = clip

        val icon = lightsaberIcon
        if (starWarsState()?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES) {
            graphics2D.color = component.foreground
            icon.foreground = component.foreground
        } else {
            icon.foreground = null
        }
        val x = amountFull + JBUIScale.scale(lightsaber.xShift)
        val y = lightsaber.yShift
        icon.paint(graphics2D, x, y)
        graphics2D.clip = previousClip
        graphics2D.color = previousColor
    }

    private fun drawBorder(rectangle2D: RoundRectangle2D, graphics2D: Graphics2D, progressBar: JProgressBar) {
        val color = graphics2D.color
        val stroke = graphics2D.stroke
        graphics2D.color = progressBar.foreground
        graphics2D.stroke = BasicStroke(2f)
        graphics2D.draw(rectangle2D)
        graphics2D.color = color
        graphics2D.stroke = stroke
    }

    private fun paintStringIfNeeded(
        graphics2D: Graphics2D,
        component: Component,
        height: Int,
        border: Insets,
        barRectWidth: Int,
        barRectHeight: Int,
        amountFull: Int,
        progressBar: JProgressBar,
        starWarsProgressBarUI: StarWarsProgressBarUI,
    ) {
        if (progressBar.isStringPainted) {
            graphics2D.translate(0, -(component.height - height) / 2)
            starWarsProgressBarUI.paintString(graphics2D, border, barRectWidth, barRectHeight, amountFull)
        }
    }
}
