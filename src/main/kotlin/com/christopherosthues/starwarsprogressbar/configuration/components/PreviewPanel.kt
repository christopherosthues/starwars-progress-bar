package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarUI
import com.christopherosthues.starwarsprogressbar.util.VehicleSelector
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
    private val showVehicleNames: () -> Boolean,
    private val showToolTips: () -> Boolean,
    private val showFactionCrests: () -> Boolean,
    private val sameVehicleVelocity: () -> Boolean,
    private val enableNewVehicles: () -> Boolean,
    private val solidProgressBarColor: () -> Boolean,
    private val enabledVehicles: () -> Map<String, Boolean>?
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
                BorderLayout.NORTH
            )
        )
        progressBarPanel.add(
            LabeledComponent.create(
                indeterminateProgressBar,
                StarWarsBundle.message(BundleConstants.INDETERMINATE),
                BorderLayout.NORTH
            )
        )

        gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridy = 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        add(progressBarPanel, gridBagConstraints)
    }

    private fun setProgressBarUI(enabledVehicles: Map<String, Boolean>?) {
        setProgressBarUI(selectRandomVehicle(enabledVehicles), selectRandomVehicle(enabledVehicles))
    }

    private fun setProgressBarUI(determinateVehicle: StarWarsVehicle, indeterminateVehicle: StarWarsVehicle) {
        determinateProgressBar.setUI(
            StarWarsProgressBarUI(
                determinateVehicle,
                showVehicleNames,
                showToolTips,
                showFactionCrests,
                sameVehicleVelocity,
                solidProgressBarColor
            )
        )

        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                indeterminateVehicle,
                showVehicleNames,
                showToolTips,
                showFactionCrests,
                sameVehicleVelocity,
                solidProgressBarColor
            )
        )
    }

    private fun selectRandomVehicle(enabledVehicles: Map<String, Boolean>?): StarWarsVehicle {
        return if (enabledVehicles != null) {
            VehicleSelector.selectRandomVehicle(enabledVehicles, enableNewVehicles())
        } else {
            VehicleSelector.selectRandomVehicle()
        }
    }

    fun selectVehicle(vehicle: StarWarsVehicle) {
        setProgressBarUI(vehicle, vehicle)
    }

    fun repaintProgressBar() {
        determinateProgressBar.repaint()
    }
}
