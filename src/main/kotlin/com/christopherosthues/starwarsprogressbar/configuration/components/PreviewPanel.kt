package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.christopherosthues.starwarsprogressbar.selectors.VehicleSelector
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarUI
import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.LabeledComponent
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JProgressBar

private const val HORIZONTAL_GAP = 10

internal class PreviewPanel(
    private val showVehicle: () -> Boolean,
    private val showVehicleNames: () -> Boolean,
    private val showToolTips: () -> Boolean,
    private val showFactionCrests: () -> Boolean,
    private val sameVehicleVelocity: () -> Boolean,
    private val enableNewVehicles: () -> Boolean,
    private val solidProgressBarColor: () -> Boolean,
    private val drawSilhouettes: () -> Boolean,
    private val changeVehicleAfterPass: () -> Boolean,
    private val numberOfPassesUntilVehicleChange: () -> Int,
    private val enabledVehicles: () -> Map<String, Boolean>?,
    private val vehicleSelector: () -> SelectionType,
) : JTitledPanel(StarWarsBundle.message(BundleConstants.PREVIEW_TITLE)) {

    private var determinateProgressBar: JProgressBar
    private var indeterminateProgressBar: JProgressBar

    init {
        val progressBarPanel = JPanel(GridLayout(1, 2, HORIZONTAL_GAP, 0))
        layout = GridBagLayout()

        val previewButton = JButton(AllIcons.Actions.Refresh)
        previewButton.addActionListener {
            setProgressBarUI(enabledVehicles())
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

        progressBarPanel.add(
            LabeledComponent.create(
                determinateProgressBar,
                StarWarsBundle.message(BundleConstants.DETERMINATE),
                BorderLayout.NORTH,
            ),
        )
        progressBarPanel.add(
            LabeledComponent.create(
                indeterminateProgressBar,
                StarWarsBundle.message(BundleConstants.INDETERMINATE),
                BorderLayout.NORTH,
            ),
        )

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
                determinateVehicle,
                enabledVehicles,
                showVehicle,
                showVehicleNames,
                showToolTips,
                showFactionCrests,
                sameVehicleVelocity,
                solidProgressBarColor,
                drawSilhouettes,
                changeVehicleAfterPass,
                numberOfPassesUntilVehicleChange,
                vehicleSelector,
            ),
        )

        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                indeterminateVehicle,
                enabledVehicles,
                showVehicle,
                showVehicleNames,
                showToolTips,
                showFactionCrests,
                sameVehicleVelocity,
                solidProgressBarColor,
                drawSilhouettes,
                changeVehicleAfterPass,
                numberOfPassesUntilVehicleChange,
                vehicleSelector,
            ),
        )
    }

    private fun selectVehicle(enabledVehicles: Map<String, Boolean>?): StarWarsVehicle = VehicleSelector.selectVehicle(enabledVehicles, enableNewVehicles(), vehicleSelector())

    fun selectVehicle(vehicle: StarWarsVehicle) {
        setProgressBarUI(vehicle, vehicle)
    }

    fun repaintProgressBar() {
        determinateProgressBar.repaint()
    }
}
