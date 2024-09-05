/**
 * MIT License
 *
 * Copyright 2020-2021 Karl Goffin
 * Copyright 2022 Christopher Osthues
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_VEHICLE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.christopherosthues.starwarsprogressbar.selectors.VehicleSelector.selectVehicle
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.UIUtil
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Insets
import java.awt.LinearGradientPaint
import java.awt.Paint
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicProgressBarUI

private const val PROGRESSBAR_HEIGHT = 20
private const val PROGRESSBAR_CORNER_SIZE = 9f

private const val FACTION_CREST_X_POSITION = 4.0

internal class StarWarsProgressBarUI(
    private var vehicle: StarWarsVehicle,
    private val enabledVehicles: () -> Map<String, Boolean>?,
    private val showVehicle: () -> Boolean,
    private val showVehicleName: () -> Boolean,
    private val showToolTips: () -> Boolean,
    private val showFactionCrests: () -> Boolean,
    private val sameVehicleVelocity: () -> Boolean,
    private val solidProgressBarColor: () -> Boolean,
    private val drawSilhouettes: () -> Boolean,
    private val changeVehicleAfterPass: () -> Boolean,
    private val numberOfPassesUntilVehicleChange: () -> Int,
    private val vehicleSelector: () -> SelectionType,
) : BasicProgressBarUI() {

    private var forwardIcon = ColoredImageComponent(StarWarsResourceLoader.getVehicleImage(vehicle.fileName))
    private var backwardIcon = ColoredImageComponent(StarWarsResourceLoader.getReversedVehicleImage(vehicle.fileName))
    private var factionCrestIcon =
        ColoredImageComponent(StarWarsResourceLoader.getFactionLogo(vehicle.factionId, false))
    private var velocity = getVelocity()
    private var position = 0
    private var numberOfPasses = 0

    constructor() : this(
        selectVehicle(
            StarWarsPersistentStateComponent.instance?.state?.vehiclesEnabled,
            false,
            StarWarsPersistentStateComponent.instance?.state?.vehicleSelector
                ?: DEFAULT_VEHICLE_SELECTOR,
        ),
        { StarWarsPersistentStateComponent.instance?.state?.vehiclesEnabled },
        { StarWarsPersistentStateComponent.instance?.state?.showVehicle ?: DEFAULT_SHOW_VEHICLE },
        { StarWarsPersistentStateComponent.instance?.state?.showVehicleNames ?: DEFAULT_SHOW_VEHICLE_NAMES },
        { StarWarsPersistentStateComponent.instance?.state?.showToolTips ?: DEFAULT_SHOW_TOOLTIPS },
        { StarWarsPersistentStateComponent.instance?.state?.showFactionCrests ?: DEFAULT_SHOW_FACTION_CRESTS },
        { StarWarsPersistentStateComponent.instance?.state?.sameVehicleVelocity ?: DEFAULT_SAME_VEHICLE_VELOCITY },
        { StarWarsPersistentStateComponent.instance?.state?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR },
        { StarWarsPersistentStateComponent.instance?.state?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES },
        {
            StarWarsPersistentStateComponent.instance?.state?.changeVehicleAfterPass
                ?: DEFAULT_CHANGE_VEHICLE_AFTER_PASS
        },
        {
            StarWarsPersistentStateComponent.instance?.state?.numberOfPassesUntilVehicleChange
                ?: DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
        },
        {
            StarWarsPersistentStateComponent.instance?.state?.vehicleSelector
                ?: DEFAULT_VEHICLE_SELECTOR
        },
    )

    private fun updateVehicle() {
        vehicle = selectVehicle(enabledVehicles(), false, vehicleSelector())
        forwardIcon = ColoredImageComponent(StarWarsResourceLoader.getVehicleImage(vehicle.fileName))
        backwardIcon = ColoredImageComponent(StarWarsResourceLoader.getReversedVehicleImage(vehicle.fileName))
        factionCrestIcon = ColoredImageComponent(StarWarsResourceLoader.getFactionLogo(vehicle.factionId, false))
    }

    private fun getVelocity(): Float = if (sameVehicleVelocity()) 1f else vehicle.velocity

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int = availableLength

    override fun getPreferredSize(c: JComponent?): Dimension = Dimension(super.getPreferredSize(c).width, JBUIScale.scale(PROGRESSBAR_HEIGHT))

    override fun paintIndeterminate(g: Graphics?, c: JComponent?) {
        paintProgressBar(g, c, false)
        updatePositionAndVelocity()
    }

    override fun paintDeterminate(g: Graphics?, c: JComponent?) {
        resetPositionAndVelocity()
        paintProgressBar(g, c, true)
    }

    private fun updatePositionAndVelocity() {
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
        if (changeVehicleAfterPass() && numberOfPasses % numberOfPassesUntilVehicleChange() == 0) {
            updateVehicle()
        }
    }

    private fun resetPositionAndVelocity() {
        velocity = getVelocity()
        position = 0
    }

    private fun paintProgressBar(g: Graphics?, c: JComponent?, paintDeterminate: Boolean) {
        if (g == null || c == null) {
            return
        }

        if (isUnsupported(g, c)) {
            if (paintDeterminate) {
                super.paintDeterminate(g, c)
            } else {
                super.paintIndeterminate(g, c)
            }
        } else {
            setProgressBarText()
            setToolTipText()

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
            val amountFull = if (paintDeterminate) getAmountFull(border, barRectWidth, barRectHeight) else position

            graphics2D.color = getBackgroundColor(c)
            if (c.isOpaque) {
                g.fillRect(0, 0, width, height)
            }

            // TODO: investigate why progressbar text is not painted after changing theme/look and feel
            val rectangle2D = getRoundRectangle(width, height)
            drawProgress(width, height, amountFull, graphics2D, rectangle2D)
            drawFactionCrest(width, height, graphics2D, rectangle2D, c)
            if (showVehicle()) {
                drawIcon(amountFull, graphics2D, rectangle2D, c)
            }
            drawBorder(rectangle2D, graphics2D)
            paintStringIfNeeded(graphics2D, c, height, border, barRectWidth, barRectHeight, amountFull)

            config.restore()
        }
    }

    private fun isUnsupported(graphics: Graphics, component: JComponent): Boolean = graphics !is Graphics2D ||
        progressBar.orientation != SwingConstants.HORIZONTAL ||
        !component.componentOrientation.isLeftToRight

    private fun setToolTipText() {
        val localizedName = StarWarsBundle.message(vehicle.localizationKey)
        if (showToolTips() && progressBar.toolTipText != localizedName) {
            progressBar.toolTipText = localizedName
        } else if (!showToolTips()) {
            progressBar.toolTipText = ""
        }
    }

    private fun setProgressBarText() {
        val localizedName = StarWarsBundle.message(vehicle.localizationKey)
        progressBar.isStringPainted = showVehicleName()
        if (showVehicleName() && progressBar.string != localizedName) {
            progressBar.string = localizedName
        } else if (!showVehicleName()) {
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

    private fun drawFactionCrest(width: Int, height: Int, graphics2D: Graphics2D, clip: Shape, component: JComponent) {
        if (showFactionCrests()) {
            val previousClip = graphics2D.clip
            val previousColor = graphics2D.color
            graphics2D.clip = clip
            graphics2D.color = component.foreground

            var x = FACTION_CREST_X_POSITION
            val y = (height.toDouble() + JBUIScale.scale(-factionCrestIcon.preferredSize.height)) / 2

            factionCrestIcon.foreground = component.foreground
            // left crest
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // middle crest
            x = width.toDouble() / 2 + JBUIScale.scale(-factionCrestIcon.preferredSize.width / 2)
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // right crest
            x = width.toDouble() + JBUIScale.scale(-factionCrestIcon.preferredSize.width) - 2
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            graphics2D.clip = previousClip
            graphics2D.color = previousColor
        }
    }

    private fun drawProgress(
        width: Int,
        height: Int,
        progress: Int,
        graphics2D: Graphics2D,
        rectangle2D: RoundRectangle2D,
    ) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val movingRight = velocity >= 0
        graphics2D.paint = getFillPaint()
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

        if (solidProgressBarColor()) {
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

    private fun getFillPaint(): Paint = vehicle.color

    private fun drawIcon(amountFull: Int, graphics2D: Graphics2D, clip: Shape, component: JComponent) {
        val previousClip = graphics2D.clip
        val previousColor = graphics2D.color
        graphics2D.clip = clip

        val isMovingRight = velocity >= 0
        val icon = if (isMovingRight) forwardIcon else backwardIcon
        if (drawSilhouettes()) {
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
        val y = vehicle.yShift
        icon.paint(graphics2D, x, y)
        graphics2D.clip = previousClip
        graphics2D.color = previousColor
    }

    private fun drawBorder(rectangle2D: RoundRectangle2D, graphics2D: Graphics2D) {
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
    ) {
        if (progressBar.isStringPainted) {
            graphics2D.translate(0, -(component.height - height) / 2)
            paintString(graphics2D, border.left, border.top, barRectWidth, barRectHeight, amountFull, border)
        }
    }
}
