package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.selectors.StarWarsSelector
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarUI
import com.intellij.icons.AllIcons
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.components.JBSlider
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
    private val log = com.intellij.openapi.diagnostic.Logger.getInstance(PreviewPanel::class.java)

    private var determinateProgressBarContainer: LabeledComponent<JComponent>
    private var determinateProgressBar: JProgressBar
    private val determinateProgressBarUI: StarWarsProgressBarUI = StarWarsProgressBarUI { starWarsState }
    private var indeterminateProgressBarContainer: LabeledComponent<JComponent>
    private var indeterminateProgressBar: JProgressBar
    private val indeterminateProgressBarUI: StarWarsProgressBarUI = StarWarsProgressBarUI { starWarsState }

    init {
        val progressBarPanel = JPanel(GridLayout(2, 2, HORIZONTAL_GAP, 0))
        layout = GridBagLayout()

        val previewButton = JButton(AllIcons.Actions.Refresh)
        previewButton.addActionListener {
            log.warn("Refresh preview button clicked")
            setProgressBarUI(starWarsState.vehiclesEnabled, starWarsState.lightsabersEnabled)
        }
        var gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.anchor = GridBagConstraints.EAST
        add(previewButton, gridBagConstraints)

        determinateProgressBar = JProgressBar(0, 100)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 50
        determinateProgressBar.ui = determinateProgressBarUI

        indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true
        indeterminateProgressBar.ui = indeterminateProgressBarUI

        log.warn("Initializing preview panel with default progress bar UIs")
        setProgressBarUI(null, null)

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

        val determinateSlider = JBSlider(0, 100, 50)
        determinateSlider.addChangeListener {
            determinateProgressBar.value = determinateSlider.value
        }

        progressBarPanel.add(determinateSlider)

        gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridy = 1
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        add(progressBarPanel, gridBagConstraints)
    }

    private fun setProgressBarUI(enabledVehicles: Map<String, Boolean>?, enabledLightsabers: Map<String, Boolean>?) {
        setProgressBarUI(
            selectEntity(enabledVehicles, enabledLightsabers, false),
            selectEntity(enabledVehicles, enabledLightsabers, true),
        )
    }

    private fun setProgressBarUI(determinateEntity: StarWarsEntity, indeterminateEntity: StarWarsEntity) {
        log.warn("Setting preview progress bars to determinate entity: $determinateEntity and indeterminate entity: $indeterminateEntity")
        determinateProgressBarUI.setEntity(determinateEntity)
        indeterminateProgressBarUI.setEntity(indeterminateEntity)
    }

    private fun selectEntity(
        enabledVehicles: Map<String, Boolean>?,
        enabledLightsabers: Map<String, Boolean>?,
        isIndeterminate: Boolean,
    ): StarWarsEntity =
        StarWarsSelector.selectEntity(
            enabledVehicles,
            enabledLightsabers,
            starWarsState.enableNew,
            starWarsState.determinateOrderSelector!!,
            starWarsState.indeterminateOrderSelector!!,
            isIndeterminate,
            starWarsState.determinateEntitySelector!!,
            starWarsState.indeterminateEntitySelector!!,
        )

    fun selectEntity(starWarsEntity: StarWarsEntity) {
        setProgressBarUI(starWarsEntity, starWarsEntity)
    }

    fun repaintProgressBar() {
        determinateProgressBar.repaint()
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            title = StarWarsBundle.message(BundleConstants.PREVIEW_TITLE)
            determinateProgressBarContainer.text = StarWarsBundle.message(BundleConstants.DETERMINATE)
            indeterminateProgressBarContainer.text = StarWarsBundle.message(BundleConstants.INDETERMINATE)
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        setProgressBarUI(
            starWarsState.vehiclesEnabled,
            starWarsState.lightsabersEnabled,
        )
    }
}
