package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_ICON
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
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
import java.awt.Shape
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.JProgressBar

private const val VEHICLE_PROGRESSBAR_HEIGHT = 20
private const val PROGRESSBAR_CORNER_SIZE = 9f
private const val FACTION_CREST_X_POSITION = 4.0

internal class VehicleProgressBarDecorator(
    private val starWarsState: () -> StarWarsState?,
) {
    private lateinit var forwardIcon: ColoredImageComponent
    private lateinit var backwardIcon: ColoredImageComponent
    private lateinit var factionCrestIcon: ColoredImageComponent

    internal fun update(vehicle: StarWarsVehicle) {
        forwardIcon = ColoredImageComponent(StarWarsResourceLoader.getImage(vehicle.fileName))
        backwardIcon = ColoredImageComponent(StarWarsResourceLoader.getReversedImage(vehicle.fileName))
        factionCrestIcon =
            ColoredImageComponent(StarWarsResourceLoader.getFactionLogo("vehicles", vehicle.factionId, false))
    }

    internal fun getHeight(): Int = JBUIScale.scale(VEHICLE_PROGRESSBAR_HEIGHT)

    internal fun paintProgressBar(
        vehicle: StarWarsVehicle,
        graphics2D: Graphics2D,
        c: JComponent,
        width: Int,
        height: Int,
        amountFull: Int,
        velocity: Float,
        progressBar: JProgressBar,
    ) {
        val rectangle2D = getRoundRectangle(width, height)
        drawProgress(vehicle, width, height, amountFull, velocity, graphics2D, rectangle2D, progressBar)
        drawFactionCrest(width, height, graphics2D, rectangle2D, c)
        if (starWarsState()?.showIcon ?: DEFAULT_SHOW_ICON) {
            drawIcon(vehicle, amountFull, velocity, graphics2D, rectangle2D, c)
        }
        drawBorder(rectangle2D, graphics2D, progressBar)
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

    private fun drawFactionCrest(width: Int, height: Int, graphics2D: Graphics2D, clip: Shape, component: JComponent) {
        if (starWarsState()?.showFactionCrests ?: DEFAULT_SHOW_FACTION_CRESTS) {
            val previousClip = graphics2D.clip
            val previousColor = graphics2D.color
            graphics2D.clip = clip
            graphics2D.color = component.foreground

            var x = JBUIScale.scale(FACTION_CREST_X_POSITION.toFloat()).toDouble()
            val y = (height.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.height)) / 2

            factionCrestIcon.foreground = component.foreground
            // left crest
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // middle crest
            x = (width.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.width)) / 2
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // right crest
            x = width.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.width) - JBUIScale.scale(
                FACTION_CREST_X_POSITION.toFloat(),
            )
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            graphics2D.clip = previousClip
            graphics2D.color = previousColor
        }
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
        graphics2D.paint = getTransparencyPaint(progressBar.background, progress, width, movingRight)
        graphics2D.clip =
            if (movingRight) Rectangle(progress, height) else Rectangle(progress, 0, progressBar.width, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun getTransparencyPaint(backgroundColor: Color, progress: Int, width: Int, movingRight: Boolean): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return transparent
        }

        if (movingRight) {
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
        return if (progress < width) {
            LinearGradientPaint(
                progress.toFloat(),
                JBUIScale.scale(2f),
                width.toFloat(),
                JBUIScale.scale(2f),
                floatArrayOf(0f, 1f),
                arrayOf(transparent, backgroundColor),
            )
        } else {
            transparent
        }
    }

    private fun getFillPaint(vehicle: StarWarsVehicle): Paint = vehicle.color

    private fun drawIcon(
        vehicle: StarWarsVehicle,
        amountFull: Int,
        velocity: Float,
        graphics2D: Graphics2D,
        clip: Shape,
        component: JComponent,
    ) {
        val previousClip = graphics2D.clip
        val previousColor = graphics2D.color
        graphics2D.clip = clip

        val isMovingRight = velocity >= 0
        val icon = if (isMovingRight) forwardIcon else backwardIcon
        if (starWarsState()?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES) {
            graphics2D.color = component.foreground
            icon.foreground = component.foreground
        } else {
            icon.foreground = null
        }
        val x = amountFull +
            if (isMovingRight) {
                JBUIScale.scale(vehicle.xShift)
            } else {
                JBUIScale.scale(-icon.preferredSize.width - vehicle.xShift)
            }
        val y = JBUIScale.scale(vehicle.yShift)
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
}
