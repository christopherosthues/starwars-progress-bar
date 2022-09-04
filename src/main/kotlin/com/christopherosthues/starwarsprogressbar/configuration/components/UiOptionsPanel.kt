package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.intellij.ui.components.JBCheckBox
import java.awt.GridLayout

private const val GAP = 5

private const val NUMBER_OF_ROWS = 3

internal class UiOptionsPanel : JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS)) {
    private val showVehicleNameCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), false)
    private val showToolTipsCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), true)
    private val showFactionCrestsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS), false)
    private val sameVehicleVelocityCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY),
        false
    )
    private val enableNewVehiclesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.ENABLE_NEW_VEHICLES),
        true
    )

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

    init {
        layout = GridLayout(NUMBER_OF_ROWS, 2, GAP, GAP)

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

        add(showVehicleNameCheckBox)
        add(sameVehicleVelocityCheckBox)
        add(showToolTipsCheckBox)
        add(enableNewVehiclesCheckBox)
        add(showFactionCrestsCheckBox)
    }

    fun updateUI(starWarsState: StarWarsState) {
        showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
        showToolTipsCheckBox.isSelected = starWarsState.showToolTips
        showFactionCrestsCheckBox.isSelected = starWarsState.showFactionCrests
        sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity
        enableNewVehiclesCheckBox.isSelected = starWarsState.enableNewVehicles
    }
}
