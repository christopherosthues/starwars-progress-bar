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

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.VehicleSelector.selectVehicle
import java.awt.*
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicProgressBarUI


internal class StarWarsProgressBarUI(
    starWarsState: () -> StarWarsState?,
    vehicle: StarWarsVehicle,
) : BasicProgressBarUI() {
    private val vehicleProgressBarDecorator = VehicleProgressBarDecorator(starWarsState, vehicle)
    private val lightsaberProgressBarDecorator = LightsaberProgressBarDecorator(starWarsState)

    constructor() : this(
        { StarWarsPersistentStateComponent.instance?.state },
        selectVehicle(
            StarWarsPersistentStateComponent.instance?.state?.vehiclesEnabled,
            false,
            StarWarsPersistentStateComponent.instance?.state?.vehicleSelector
                ?: DEFAULT_VEHICLE_SELECTOR,
        ),
    )

    private fun update() {
        vehicleProgressBarDecorator.update()
    }

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int = availableLength

    override fun getPreferredSize(c: JComponent?): Dimension =
        vehicleProgressBarDecorator.getPreferredSize(c, super.getPreferredSize(c).width)

    override fun paintIndeterminate(g: Graphics?, c: JComponent?) {
        paintProgressBar(g, c, false)
        vehicleProgressBarDecorator.updatePositionAndVelocity(progressBar)
    }

    override fun paintDeterminate(g: Graphics?, c: JComponent?) {
        vehicleProgressBarDecorator.resetPositionAndVelocity()
        paintProgressBar(g, c, true)
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
            vehicleProgressBarDecorator.paintProgressBar(g, c, paintDeterminate, progressBar, this)
        }
    }

    internal fun getAmountFull(barRectWidth: Int, barRectHeight: Int, border: Insets): Int = getAmountFull(border, barRectWidth, barRectHeight)

    internal fun paintString(
        graphics2D: Graphics2D,
        border: Insets,
        barRectWidth: Int,
        barRectHeight: Int,
        amountFull: Int,
    ) {
        paintString(graphics2D, border.left, border.top, barRectWidth, barRectHeight, amountFull, border)
    }

    private fun isUnsupported(graphics: Graphics, component: JComponent): Boolean = graphics !is Graphics2D ||
        progressBar.orientation != SwingConstants.HORIZONTAL ||
        !component.componentOrientation.isLeftToRight
}
