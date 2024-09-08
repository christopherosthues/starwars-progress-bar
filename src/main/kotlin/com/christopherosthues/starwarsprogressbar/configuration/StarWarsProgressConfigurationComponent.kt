package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.configuration.components.PreviewPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.UiOptionsPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.VehiclesPanel
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.christopherosthues.starwarsprogressbar.ui.events.VehicleClickListener
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.util.*
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

    val showVehicle: Boolean
        get() = uiOptionsPanel.showVehicle

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

    val solidProgressBarColor: Boolean
        get() = uiOptionsPanel.solidProgressBarColor

    val drawSilhouettes: Boolean
        get() = uiOptionsPanel.drawSilhouettes

    val changeVehicleAfterPass: Boolean
        get() = uiOptionsPanel.changeVehicleAfterPass

    val numberOfPassesUntilVehicleChange: Int
        get() = uiOptionsPanel.numberOfPassesUntilVehicleChange

    val vehicleSelector: SelectionType
        get() = uiOptionsPanel.vehicleSelector

    val language: Language
        get() = uiOptionsPanel.language

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

        previewPanel.addPropertyChangeListener(uiOptionsPanel)
        vehiclesPanel.addPropertyChangeListener(uiOptionsPanel)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        previewPanel = PreviewPanel(
            this::showVehicle,
            this::showVehicleNames,
            this::showToolTips,
            this::showFactionCrests,
            this::sameVehicleVelocity,
            this::enableNewVehicles,
            this::solidProgressBarColor,
            this::drawSilhouettes,
            this::changeVehicleAfterPass,
            this::numberOfPassesUntilVehicleChange,
            this::enabledVehicles,
            this::vehicleSelector,
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        uiOptionsPanel.addPropertyChangeListener {
            if (isProgressBarTextEvent(it.propertyName) ||
                isProgressBarDrawEvent(it.propertyName) ||
                isVehicleChangeEvent(it.propertyName) ||
                it.propertyName == UiOptionsPanel::vehicleSelector.name
            ) {
                repaintProgressBar()
            }
        }

        formBuilder.addComponent(uiOptionsPanel)
    }

    private fun isProgressBarTextEvent(propertyName: String): Boolean =
        propertyName == UiOptionsPanel::showVehicleNames.name ||
            propertyName == UiOptionsPanel::showToolTips.name

    private fun isProgressBarDrawEvent(propertyName: String): Boolean =
        propertyName == UiOptionsPanel::showFactionCrests.name ||
            propertyName == UiOptionsPanel::sameVehicleVelocity.name ||
            propertyName == UiOptionsPanel::solidProgressBarColor.name ||
            propertyName == UiOptionsPanel::showVehicle.name ||
            propertyName == UiOptionsPanel::drawSilhouettes.name

    private fun isVehicleChangeEvent(propertyName: String): Boolean =
        propertyName == UiOptionsPanel::changeVehicleAfterPass.name ||
            propertyName == UiOptionsPanel::numberOfPassesUntilVehicleChange.name

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
