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
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SELECTOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.StarWarsSelector.selectEntity
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JProgressBar
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicProgressBarUI

internal class StarWarsProgressBarUI(
    private val starWarsState: () -> StarWarsState?,
    private var starWarsEntity: StarWarsEntity,
) : BasicProgressBarUI() {
    private val vehicleProgressBarDecorator = VehicleProgressBarDecorator(starWarsState)
    private val lightsaberProgressBarDecorator = LightsaberProgressBarDecorator(starWarsState)
    private var velocity = 1f
    private var position = 0
    private var numberOfPasses = 0

    constructor() : this(
        { StarWarsPersistentStateComponent.instance?.state },
        selectEntity(
            StarWarsPersistentStateComponent.instance?.state?.vehiclesEnabled,
            StarWarsPersistentStateComponent.instance?.state?.lightsabersEnabled,
            StarWarsPersistentStateComponent.instance?.state?.enableNew ?: DEFAULT_ENABLE_NEW,
            StarWarsPersistentStateComponent.instance?.state?.selector
                ?: DEFAULT_SELECTOR,
        ),
    )

    init {
        velocity = getVelocity()
        updateDecorator()
    }

    private fun getVelocity(): Float =
        if (starWarsState()?.sameVelocity ?: DEFAULT_SAME_VELOCITY) 1f else starWarsEntity.velocity

    private fun update() {
        starWarsEntity = selectEntity(
            starWarsState()?.vehiclesEnabled,
            starWarsState()?.lightsabersEnabled,
            starWarsState()?.enableNew ?: DEFAULT_ENABLE_NEW,
            starWarsState()?.selector ?: DEFAULT_SELECTOR,
        )
        updateDecorator()
    }

    private fun updateDecorator() {
        val entity = starWarsEntity
        if (entity is StarWarsVehicle) {
            vehicleProgressBarDecorator.update(entity)
        } else if (entity is Lightsabers) {
            lightsaberProgressBarDecorator.update(entity)
        }
    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int = availableLength

    override fun getPreferredSize(c: JComponent?): Dimension {
        var height = super.getPreferredSize(c).height
        if (starWarsEntity is StarWarsVehicle) {
            height = vehicleProgressBarDecorator.getHeight()
        } else if (starWarsEntity is Lightsabers) {
            height = lightsaberProgressBarDecorator.getHeight()
        }
        return Dimension(super.getPreferredSize(c).width, height)
    }

    override fun paintIndeterminate(g: Graphics?, c: JComponent?) {
        paintProgressBar(g, c, false)
        updatePositionAndVelocity(progressBar)
    }

    override fun paintDeterminate(g: Graphics?, c: JComponent?) {
        resetPositionAndVelocity()
        paintProgressBar(g, c, true)
    }

    private fun updatePositionAndVelocity(progressBar: JProgressBar) {
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
                starWarsState()?.changeAfterPass
                    ?: DEFAULT_CHANGE_AFTER_PASS
                ) &&
            numberOfPasses % (
                starWarsState()?.numberOfPassesUntilChange
                    ?: DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE
                ) == 0
        ) {
            update()
        }
    }

    internal fun resetPositionAndVelocity() {
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
            // TODO: investigate why progressbar text is not painted after changing theme/look and feel
            setProgressBarText()
            setToolTipText()

            val config = GraphicsUtil.setupAAPainting(g)

            val graphics2D = g as Graphics2D
            val border = progressBar.insets
            val width = progressBar.width
            var height = progressBar.preferredSize.height
            if ((c.height - height).isOdd()) {
                height += JBUIScale.scale(1)
            }
            val barRectWidth = width - (border.right + border.left)
            val barRectHeight = height - (border.top + border.bottom)
            if (barRectWidth <= 0 || barRectHeight <= 0) {
                return
            }
            val amountFull =
                if (paintDeterminate) getAmountFull(barRectWidth, barRectHeight, border) else position
            graphics2D.color = getBackgroundColor(c)
            if (c.isOpaque) {
                g.fillRect(0, 0, width, height)
            }

            val entity = starWarsEntity
            if (entity is StarWarsVehicle) {
                vehicleProgressBarDecorator.paintProgressBar(
                    entity,
                    graphics2D,
                    c,
                    width,
                    height,
                    amountFull,
                    velocity,
                    progressBar,
                )
            } else if (entity is Lightsabers) {
                lightsaberProgressBarDecorator.paintProgressBar(
                    entity,
                    graphics2D,
                    c,
                    width,
                    height,
                    amountFull,
                )
            }

            paintStringIfNeeded(graphics2D, c, height, border, barRectWidth, barRectHeight, amountFull)

            config.restore()
        }
    }

    private fun getAmountFull(barRectWidth: Int, barRectHeight: Int, border: Insets): Int =
        getAmountFull(border, barRectWidth, barRectHeight)

    private fun setProgressBarText() {
        progressBar.isStringPainted = starWarsState()?.showNames ?: DEFAULT_SHOW_NAMES
        if (starWarsState()?.showNames ?: DEFAULT_SHOW_NAMES) {
            val localizedName = StarWarsBundle.message(starWarsEntity.localizationKey)
            if (progressBar.string != localizedName) {
                progressBar.string = localizedName
            }
        } else if (!(starWarsState()?.showNames ?: DEFAULT_SHOW_NAMES)) {
            progressBar.string = ""
        }
    }

    private fun setToolTipText() {
        if (starWarsState()?.showToolTips ?: DEFAULT_SHOW_TOOLTIPS) {
            val localizedName = StarWarsBundle.message(starWarsEntity.localizationKey)
            if (progressBar.toolTipText != localizedName) {
                progressBar.toolTipText = localizedName
            }
        } else if (!(starWarsState()?.showToolTips ?: DEFAULT_SHOW_TOOLTIPS)) {
            progressBar.toolTipText = ""
        }
    }

    private fun getBackgroundColor(component: JComponent): Color {
        val parent = component.parent
        return if (parent != null) parent.background else UIUtil.getPanelBackground()
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

    private fun isUnsupported(graphics: Graphics, component: JComponent): Boolean = graphics !is Graphics2D ||
        progressBar.orientation != SwingConstants.HORIZONTAL ||
        !component.componentOrientation.isLeftToRight
}
