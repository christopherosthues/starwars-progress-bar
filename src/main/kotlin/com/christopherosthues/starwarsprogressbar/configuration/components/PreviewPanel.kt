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

    private var determinateProgressBarContainer: LabeledComponent<JComponent>
    private var determinateProgressBar: JProgressBar
    private var indeterminateProgressBarContainer: LabeledComponent<JComponent>
    private var indeterminateProgressBar: JProgressBar

    init {
        val progressBarPanel = JPanel(GridLayout(2, 2, HORIZONTAL_GAP, 0))
        layout = GridBagLayout()

        val previewButton = JButton(AllIcons.Actions.Refresh)
        previewButton.addActionListener {
            setProgressBarUI(starWarsState.vehiclesEnabled, starWarsState.lightsabersEnabled)
        }
        var gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.anchor = GridBagConstraints.EAST
        add(previewButton, gridBagConstraints)

        determinateProgressBar = JProgressBar(0, 100)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 50

        indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true

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
            selectEntity(enabledVehicles, enabledLightsabers),
            selectEntity(enabledVehicles, enabledLightsabers),
        )
    }

    private fun setProgressBarUI(determinateEntity: StarWarsEntity, indeterminateEntity: StarWarsEntity) {
        determinateProgressBar.setUI(
            StarWarsProgressBarUI(
                { starWarsState },
                determinateEntity,
            ),
        )

        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                { starWarsState },
                indeterminateEntity,
            ),
        )
    }

    private fun selectEntity(enabledVehicles: Map<String, Boolean>?, enabledLightsabers: Map<String, Boolean>?): StarWarsEntity =
        StarWarsSelector.selectEntity(enabledVehicles, enabledLightsabers, starWarsState.enableNew, starWarsState.selector!!)

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
}
