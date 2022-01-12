package com.christopherosthues.starwarsprogressbar.ui.configuration

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.ui.*
import com.christopherosthues.starwarsprogressbar.ui.components.JTitledPanel
import com.christopherosthues.starwarsprogressbar.ui.components.VehiclesPanel
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import java.awt.*
import javax.swing.*

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel: JPanel

    private lateinit var determinateProgressBar: JProgressBar
    private lateinit var indeterminateProgressBar: JProgressBar

    private val vehiclesPanel = VehiclesPanel()

    private val showVehicleNameCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), false)
    private val showToolTipsCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), true)
    private val sameVehicleVelocityCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY), false)

    val panel: JPanel
        get() = mainPanel

    val enabledVehicles: Map<String, Boolean>
        get() = vehiclesPanel.enabledVehicles

    val showVehicleNames: Boolean
        get() = showVehicleNameCheckBox.isSelected

    val showToolTips: Boolean
        get() = showToolTipsCheckBox.isSelected

    val sameVehicleVelocity: Boolean
        get() = sameVehicleVelocityCheckBox.isSelected

    init {
        createUI()
    }

    fun updateUI(starWarsState: StarWarsState?) {
        if (starWarsState != null) {
            showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
            showToolTipsCheckBox.isSelected = starWarsState.showToolTips
            sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity

            vehiclesPanel.updateUI(starWarsState)
        }
    }

    private fun createUI() {
        mainPanel = JPanel(BorderLayout())
        val formBuilder = FormBuilder.createFormBuilder()

        createPreviewSection(formBuilder)

        createUiOptionsSection(formBuilder)

        createVehicleSection(formBuilder)

        mainPanel.add(formBuilder.panel, BorderLayout.NORTH)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        val previewPanel = JTitledPanel(StarWarsBundle.message(BundleConstants.PREVIEW_TITLE))
        previewPanel.layout = GridLayout(1, 2, 10, 0)

        determinateProgressBar = JProgressBar(0, 2)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 1
        determinateProgressBar.setUI(
            StarWarsProgressBarUI(
                VehicleSelector.selectRandomVehicle(),
                showVehicleNameCheckBox::isSelected,
                showToolTipsCheckBox::isSelected,
                sameVehicleVelocityCheckBox::isSelected
            )
        )

        indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true
        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                VehicleSelector.selectRandomVehicle(),
                showVehicleNameCheckBox::isSelected,
                showToolTipsCheckBox::isSelected,
                sameVehicleVelocityCheckBox::isSelected
            )
        )

        previewPanel.add(
            LabeledComponent.create(
                determinateProgressBar,
                StarWarsBundle.message(BundleConstants.DETERMINATE),
                BorderLayout.NORTH
            )
        )
        previewPanel.add(
            LabeledComponent.create(
                indeterminateProgressBar,
                StarWarsBundle.message(BundleConstants.INDETERMINATE),
                BorderLayout.NORTH
            )
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        val uiOptionsPanel = JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS))
        uiOptionsPanel.layout = GridLayout(2, 2, 5, 5)

        showVehicleNameCheckBox.addItemListener { repaintProgressBar() }
        showToolTipsCheckBox.addItemListener { repaintProgressBar() }
        sameVehicleVelocityCheckBox.addItemListener { repaintProgressBar() }

        uiOptionsPanel.add(showVehicleNameCheckBox)
        uiOptionsPanel.add(sameVehicleVelocityCheckBox)
        uiOptionsPanel.add(showToolTipsCheckBox)

        formBuilder.addComponent(uiOptionsPanel)
    }

    private fun repaintProgressBar() {
        determinateProgressBar.repaint()
    }

    private fun createVehicleSection(formBuilder: FormBuilder) {
        formBuilder.addComponent(vehiclesPanel)
    }
}
