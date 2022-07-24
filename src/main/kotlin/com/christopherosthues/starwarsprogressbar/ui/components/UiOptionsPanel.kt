package com.christopherosthues.starwarsprogressbar.ui.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsState
import com.intellij.ui.components.JBCheckBox
import java.awt.GridLayout

private const val GAP = 5

internal class UiOptionsPanel : JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS)) {
    private val showVehicleNameCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), false)
    private val showToolTipsCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), true)
    private val sameVehicleVelocityCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY),
        false
    )

    val showVehicleNames: Boolean
        get() = showVehicleNameCheckBox.isSelected

    val showToolTips: Boolean
        get() = showToolTipsCheckBox.isSelected

    val sameVehicleVelocity: Boolean
        get() = sameVehicleVelocityCheckBox.isSelected

    init {
        layout = GridLayout(2, 2, GAP, GAP)

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
        sameVehicleVelocityCheckBox.addItemListener {
            firePropertyChange(
                this::sameVehicleVelocity.name,
                !sameVehicleVelocity,
                sameVehicleVelocity
            )
        }

        add(showVehicleNameCheckBox)
        add(sameVehicleVelocityCheckBox)
        add(showToolTipsCheckBox)
    }

    fun updateUI(starWarsState: StarWarsState) {
        showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
        showToolTipsCheckBox.isSelected = starWarsState.showToolTips
        sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity
    }
}
