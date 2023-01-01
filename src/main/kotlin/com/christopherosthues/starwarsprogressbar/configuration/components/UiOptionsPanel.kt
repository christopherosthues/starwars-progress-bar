package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW_VEHICLES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.intellij.ui.components.JBCheckBox
import java.awt.GridLayout

private const val GAP = 5

private const val NUMBER_OF_ROWS = 4

internal class UiOptionsPanel : JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS)) {
    private val showVehicleNameCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), DEFAULT_SHOW_VEHICLE_NAMES)
    private val showToolTipsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), DEFAULT_SHOW_TOOLTIPS)
    private val showFactionCrestsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS), DEFAULT_SHOW_FACTION_CRESTS)
    private val sameVehicleVelocityCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY),
        DEFAULT_SAME_VEHICLE_VELOCITY
    )
    private val enableNewVehiclesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.ENABLE_NEW_VEHICLES),
        DEFAULT_ENABLE_NEW_VEHICLES
    )
    private val solidProgressBarColorCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SOLID_PROGRESS_BAR_COLOR),
        DEFAULT_SOLID_PROGRESS_BAR_COLOR
    )
    private val showVehicleCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SHOW_VEHICLE),
        DEFAULT_SHOW_VEHICLE
    )
    private val drawSilhouttesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.DRAW_SILHOUETTES),
        DEFAULT_DRAW_SILHOUETTES
    )

    val showVehicle: Boolean
        get() = showVehicleCheckBox.isSelected

    val showVehicleNames: Boolean
        get() = showVehicleNameCheckBox.isSelected

    val showToolTips: Boolean
        get() = showToolTipsCheckBox.isSelected

    val showFactionCrests: Boolean
        get() = showFactionCrestsCheckBox.isSelected

    val sameVehicleVelocity: Boolean
        get() = sameVehicleVelocityCheckBox.isSelected

    val enableNewVehicles: Boolean
        get() = enableNewVehiclesCheckBox.isSelected

    val solidProgressBarColor: Boolean
        get() = solidProgressBarColorCheckBox.isSelected

    val drawSilhouttes: Boolean
        get() = drawSilhouttesCheckBox.isSelected

    init {
        layout = GridLayout(NUMBER_OF_ROWS, 2, GAP, GAP)

        showVehicleCheckBox.addItemListener {
            firePropertyChange(
                this::showVehicle.name,
                !showVehicle,
                showVehicle
            )
        }
        showVehicleNameCheckBox.addItemListener {
            firePropertyChange(
                this::showVehicleNames.name,
                !showVehicleNames,
                showVehicleNames
            )
        }
        showToolTipsCheckBox.addItemListener {
            firePropertyChange(
                this::showToolTips.name,
                !showToolTips,
                showToolTips
            )
        }
        showFactionCrestsCheckBox.addItemListener {
            firePropertyChange(
                this::showFactionCrests.name,
                !showFactionCrests,
                showFactionCrests
            )
        }
        sameVehicleVelocityCheckBox.addItemListener {
            firePropertyChange(
                this::sameVehicleVelocity.name,
                !sameVehicleVelocity,
                sameVehicleVelocity
            )
        }
        enableNewVehiclesCheckBox.addItemListener {
            firePropertyChange(
                this::enableNewVehicles.name,
                !enableNewVehicles,
                enableNewVehicles
            )
        }
        solidProgressBarColorCheckBox.addItemListener {
            firePropertyChange(
                this::solidProgressBarColor.name,
                !solidProgressBarColor,
                solidProgressBarColor
            )
        }
        drawSilhouttesCheckBox.addItemListener {
            firePropertyChange(
                this::drawSilhouttes.name,
                !drawSilhouttes,
                drawSilhouttes
            )
        }

        add(showVehicleCheckBox)
        add(sameVehicleVelocityCheckBox)
        add(showVehicleNameCheckBox)
        add(enableNewVehiclesCheckBox)
        add(showToolTipsCheckBox)
        add(solidProgressBarColorCheckBox)
        add(showFactionCrestsCheckBox)
        add(drawSilhouttesCheckBox)
    }

    fun updateUI(starWarsState: StarWarsState) {
        showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
        showVehicleCheckBox.isSelected = starWarsState.showVehicle
        showToolTipsCheckBox.isSelected = starWarsState.showToolTips
        showFactionCrestsCheckBox.isSelected = starWarsState.showFactionCrests
        sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity
        enableNewVehiclesCheckBox.isSelected = starWarsState.enableNewVehicles
        solidProgressBarColorCheckBox.isSelected = starWarsState.solidProgressBarColor
        drawSilhouttesCheckBox.isSelected = starWarsState.drawSilhouettes
    }
}
