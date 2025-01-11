package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.configuration.components.PreviewPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.UiOptionsPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.VehiclesPanel
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.events.VehicleClickListener
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.util.*
import javax.swing.JPanel

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel: JPanel

    private lateinit var previewPanel: PreviewPanel

    val starWarsState: StarWarsState = StarWarsState()

    private val uiOptionsPanel = UiOptionsPanel(starWarsState)

    private val vehiclesPanel = VehiclesPanel(starWarsState)

    val panel: JPanel
        get() = mainPanel

    init {
        createUI()
    }

    fun updateUI(starWarsState: StarWarsState?) {
        if (starWarsState != null) {
            this.starWarsState.copy(starWarsState)
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

        previewPanel.addPropertyChangeListener(uiOptionsPanel)
        vehiclesPanel.addPropertyChangeListener(uiOptionsPanel)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        previewPanel = PreviewPanel(
            starWarsState,
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        uiOptionsPanel.addPropertyChangeListener {
            if (isProgressBarTextEvent(it.propertyName) ||
                isProgressBarDrawEvent(it.propertyName) ||
                isVehicleChangeEvent(it.propertyName) ||
                it.propertyName == VehicleSelectorEvent
            ) {
                repaintProgressBar()
            }
        }

        formBuilder.addComponent(uiOptionsPanel)
    }

    private fun isProgressBarTextEvent(propertyName: String): Boolean =
        propertyName == ShowVehicleNamesEvent ||
            propertyName == ShowToolTipsEvent

    private fun isProgressBarDrawEvent(propertyName: String): Boolean =
        propertyName == ShowFactionCrestsEvent ||
            propertyName == SameVelocityEvent ||
            propertyName == SolidProgressBarColorEvent ||
            propertyName == ShowVehicleEvent ||
            propertyName == DrawSilhouettesEvent

    private fun isVehicleChangeEvent(propertyName: String): Boolean =
        propertyName == ChangeVehicleAfterPassEvent ||
            propertyName == NumberOfPassesUntilVehicleChangeEvent

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
