package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.configuration.components.PreviewPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.UiOptionsPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.VehiclesPanel
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.events.VehicleClickListener
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JPanel

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel: JPanel

    private lateinit var previewPanel: PreviewPanel

    private val uiOptionsPanel = UiOptionsPanel()

    private val vehiclesPanel = VehiclesPanel()

    val panel: JPanel
        get() = mainPanel

    val enabledVehicles: Map<String, Boolean>
        get() = vehiclesPanel.enabledVehicles

    val showVehicleNames: Boolean
        get() = uiOptionsPanel.showVehicleNames

    val showToolTips: Boolean
        get() = uiOptionsPanel.showToolTips

    val showFactionCrests: Boolean
        get() = uiOptionsPanel.showFactionCrests

    val sameVehicleVelocity: Boolean
        get() = uiOptionsPanel.sameVehicleVelocity

    val enableNewVehicles: Boolean
        get() = uiOptionsPanel.enableNewVehicles

    init {
        createUI()
    }

    fun updateUI(starWarsState: StarWarsState?) {
        if (starWarsState != null) {
            uiOptionsPanel.updateUI(starWarsState)

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
        previewPanel = PreviewPanel(
            this::showVehicleNames,
            this::showToolTips,
            this::showFactionCrests,
            this::sameVehicleVelocity,
            this::enableNewVehicles,
            this::enabledVehicles
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        uiOptionsPanel.addPropertyChangeListener() {
            if (it.propertyName.equals(UiOptionsPanel::showVehicleNames.name) ||
                it.propertyName.equals(UiOptionsPanel::showToolTips.name) ||
                it.propertyName.equals(UiOptionsPanel::showFactionCrests.name) ||
                it.propertyName.equals(UiOptionsPanel::sameVehicleVelocity.name)
            ) {
                repaintProgressBar()
            }
        }

        formBuilder.addComponent(uiOptionsPanel)
    }

    private fun repaintProgressBar() {
        previewPanel.repaintProgressBar()
    }

    private fun createVehicleSection(formBuilder: FormBuilder) {
        vehiclesPanel.addVehicleListener(object : VehicleClickListener {
            override fun vehicleClicked(vehicle: StarWarsVehicle) {
                previewPanel.selectVehicle(vehicle)
            }
        })
        formBuilder.addComponent(vehiclesPanel)
    }
}
