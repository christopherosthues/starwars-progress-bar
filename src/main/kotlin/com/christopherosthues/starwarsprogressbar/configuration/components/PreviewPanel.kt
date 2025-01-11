package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.LanguageEvent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.VehicleSelector
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarUI
import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.LabeledComponent
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JProgressBar

private const val HORIZONTAL_GAP = 10

internal class PreviewPanel(
    private val starWarsState: StarWarsState,
) : JTitledPanel(StarWarsBundle.message(BundleConstants.PREVIEW_TITLE)) {

    private var determinateProgressBarContainer: LabeledComponent<JComponent>
    private var determinateProgressBar: JProgressBar
    private var indeterminateProgressBarContainer: LabeledComponent<JComponent>
    private var indeterminateProgressBar: JProgressBar

    init {
        val progressBarPanel = JPanel(GridLayout(1, 2, HORIZONTAL_GAP, 0))
        layout = GridBagLayout()

        val previewButton = JButton(AllIcons.Actions.Refresh)
        previewButton.addActionListener {
            setProgressBarUI(starWarsState.vehiclesEnabled)
        }
        var gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.anchor = GridBagConstraints.EAST
        add(previewButton, gridBagConstraints)

        determinateProgressBar = JProgressBar(0, 2)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 1

        indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true

        setProgressBarUI(null)

        determinateProgressBarContainer = LabeledComponent.create(
            determinateProgressBar,
            StarWarsBundle.message(BundleConstants.DETERMINATE),
            BorderLayout.NORTH,
        )
        progressBarPanel.add(determinateProgressBarContainer)
        indeterminateProgressBarContainer = LabeledComponent.create(
            indeterminateProgressBar,
            StarWarsBundle.message(BundleConstants.INDETERMINATE),
            BorderLayout.NORTH,
        )
        progressBarPanel.add(indeterminateProgressBarContainer)

        gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridy = 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        add(progressBarPanel, gridBagConstraints)
    }

    private fun setProgressBarUI(enabledVehicles: Map<String, Boolean>?) {
        setProgressBarUI(
            selectVehicle(enabledVehicles),
            selectVehicle(enabledVehicles),
        )
    }

    private fun setProgressBarUI(determinateVehicle: StarWarsVehicle, indeterminateVehicle: StarWarsVehicle) {
        determinateProgressBar.setUI(
            StarWarsProgressBarUI(
                { starWarsState },
                determinateVehicle,
            ),
        )

        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                { starWarsState },
                indeterminateVehicle,
            ),
        )
    }

    private fun selectVehicle(enabledVehicles: Map<String, Boolean>?): StarWarsVehicle =
        VehicleSelector.selectVehicle(enabledVehicles, starWarsState.enableNewVehicles, starWarsState.vehicleSelector!!)

    fun selectVehicle(vehicle: StarWarsVehicle) {
        setProgressBarUI(vehicle, vehicle)
    }

    fun repaintProgressBar() {
        determinateProgressBar.repaint()
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LanguageEvent) {
            title = StarWarsBundle.message(BundleConstants.PREVIEW_TITLE)
            determinateProgressBarContainer.text = StarWarsBundle.message(BundleConstants.DETERMINATE)
            indeterminateProgressBarContainer.text = StarWarsBundle.message(BundleConstants.INDETERMINATE)
        }
    }
}
