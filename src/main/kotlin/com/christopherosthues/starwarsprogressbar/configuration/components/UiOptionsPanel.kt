package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.Language
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_VEHICLE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW_VEHICLES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_LANGUAGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ItemEvent
import javax.swing.JLabel
import javax.swing.JPanel

private const val GAP = 5

private const val NUMBER_OF_ROWS = 6
private const val MINIMUM_NUMBER_OF_PASSES = 1
private const val MAXIMUM_NUMBER_OF_PASSES = 20
private const val HORIZONTAL_GAP = 8

internal class UiOptionsPanel : JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS)) {
    private val showVehicleNameCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), DEFAULT_SHOW_VEHICLE_NAMES)
    private val showToolTipsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), DEFAULT_SHOW_TOOLTIPS)
    private val showFactionCrestsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS), DEFAULT_SHOW_FACTION_CRESTS)
    private val sameVehicleVelocityCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY),
        DEFAULT_SAME_VEHICLE_VELOCITY,
    )
    private val enableNewVehiclesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.ENABLE_NEW_VEHICLES),
        DEFAULT_ENABLE_NEW_VEHICLES,
    )
    private val solidProgressBarColorCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SOLID_PROGRESS_BAR_COLOR),
        DEFAULT_SOLID_PROGRESS_BAR_COLOR,
    )
    private val showVehicleCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SHOW_VEHICLE),
        DEFAULT_SHOW_VEHICLE,
    )
    private val drawSilhouettesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.DRAW_SILHOUETTES),
        DEFAULT_DRAW_SILHOUETTES,
    )
    private val changeVehicleAfterPassCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.CHANGE_VEHICLE_AFTER_PASS),
        DEFAULT_CHANGE_VEHICLE_AFTER_PASS,
    )
    private val numberOfPassesUntilVehicleChangeSpinner =
        JBIntSpinner(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, MINIMUM_NUMBER_OF_PASSES, MAXIMUM_NUMBER_OF_PASSES)

    private val selectionLabel = JLabel(StarWarsBundle.message(BundleConstants.SELECTOR))
    private val vehicleSelectorComboBox = ComboBox(SelectionType.entries.toTypedArray())
    private val languageLabel = JLabel(StarWarsBundle.message(BundleConstants.LANGUAGE))
    private val languageComboBox = ComboBox(arrayOf(Language.ENGLISH, Language.GERMAN, Language.SPANISH))

    private var oldLanguage: Language? = null

    val language: Language
        get() = languageComboBox.selectedItem as Language? ?: DEFAULT_LANGUAGE

    val showVehicle: Boolean
        get() = showVehicleCheckBox.isSelected

    val showVehicleNames: Boolean
        get() = showVehicleNameCheckBox.isSelected

    val showToolTips: Boolean
        get() = showToolTipsCheckBox.isSelected

    val showFactionCrests: Boolean
        get() = showFactionCrestsCheckBox.isSelected

    val sameVehicleVelocity: Boolean
        get() = sameVehicleVelocityCheckBox.isSelected

    val enableNewVehicles: Boolean
        get() = enableNewVehiclesCheckBox.isSelected

    val solidProgressBarColor: Boolean
        get() = solidProgressBarColorCheckBox.isSelected

    val drawSilhouettes: Boolean
        get() = drawSilhouettesCheckBox.isSelected

    val changeVehicleAfterPass: Boolean
        get() = changeVehicleAfterPassCheckBox.isSelected

    val numberOfPassesUntilVehicleChange: Int
        get() = numberOfPassesUntilVehicleChangeSpinner.number

    val vehicleSelector: SelectionType
        get() = vehicleSelectorComboBox.selectedItem as SelectionType? ?: DEFAULT_VEHICLE_SELECTOR

    init {

        languageComboBox.addItemListener {
            if (it.stateChange == ItemEvent.DESELECTED) {
                oldLanguage = it.item as Language?
            }
            if (it.stateChange == ItemEvent.SELECTED) {
                val newLanguage = it.item as Language? ?: DEFAULT_LANGUAGE
                StarWarsBundle.setLocale(newLanguage.toLocale())
                firePropertyChange(
                    this::language.name,
                    oldLanguage,
                    newLanguage,
                )
            }
        }
        layout = GridLayout(NUMBER_OF_ROWS, 2, GAP, GAP)

        showVehicleCheckBox.addItemListener {
            firePropertyChange(
                this::showVehicle.name,
                !showVehicle,
                showVehicle,
            )
        }
        showVehicleNameCheckBox.addItemListener {
            firePropertyChange(
                this::showVehicleNames.name,
                !showVehicleNames,
                showVehicleNames,
            )
        }
        showToolTipsCheckBox.addItemListener {
            firePropertyChange(
                this::showToolTips.name,
                !showToolTips,
                showToolTips,
            )
        }
        showFactionCrestsCheckBox.addItemListener {
            firePropertyChange(
                this::showFactionCrests.name,
                !showFactionCrests,
                showFactionCrests,
            )
        }
        sameVehicleVelocityCheckBox.addItemListener {
            firePropertyChange(
                this::sameVehicleVelocity.name,
                !sameVehicleVelocity,
                sameVehicleVelocity,
            )
        }
        enableNewVehiclesCheckBox.addItemListener {
            firePropertyChange(
                this::enableNewVehicles.name,
                !enableNewVehicles,
                enableNewVehicles,
            )
        }
        solidProgressBarColorCheckBox.addItemListener {
            firePropertyChange(
                this::solidProgressBarColor.name,
                !solidProgressBarColor,
                solidProgressBarColor,
            )
        }
        drawSilhouettesCheckBox.addItemListener {
            firePropertyChange(
                this::drawSilhouettes.name,
                !drawSilhouettes,
                drawSilhouettes,
            )
        }
        changeVehicleAfterPassCheckBox.addItemListener {
            numberOfPassesUntilVehicleChangeSpinner.isEnabled = changeVehicleAfterPass
            firePropertyChange(
                this::changeVehicleAfterPass.name,
                !changeVehicleAfterPass,
                changeVehicleAfterPass,
            )
        }
        numberOfPassesUntilVehicleChangeSpinner.addChangeListener {
            firePropertyChange(
                this::numberOfPassesUntilVehicleChange.name,
                numberOfPassesUntilVehicleChange,
                numberOfPassesUntilVehicleChange,
            )
        }
        numberOfPassesUntilVehicleChangeSpinner.isEnabled = changeVehicleAfterPass
        vehicleSelectorComboBox.addItemListener {
            firePropertyChange(
                this::vehicleSelector.name,
                vehicleSelector,
                vehicleSelector,
            )
        }

        val languagePanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        languagePanel.add(languageLabel)
        languagePanel.add(languageComboBox)

        val passesPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        passesPanel.add(changeVehicleAfterPassCheckBox)
        passesPanel.add(numberOfPassesUntilVehicleChangeSpinner)

        val selectionPanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        selectionPanel.add(selectionLabel)
        selectionPanel.add(vehicleSelectorComboBox)

        add(languagePanel)
        add(JPanel())
        add(showVehicleCheckBox)
        add(sameVehicleVelocityCheckBox)
        add(showVehicleNameCheckBox)
        add(enableNewVehiclesCheckBox)
        add(showToolTipsCheckBox)
        add(solidProgressBarColorCheckBox)
        add(showFactionCrestsCheckBox)
        add(drawSilhouettesCheckBox)
        add(passesPanel)
        add(selectionPanel)

        addPropertyChangeListener {
            if (it.propertyName == this::language.name) {
                title = StarWarsBundle.message(BundleConstants.UI_OPTIONS)
                showVehicleNameCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME)
                showToolTipsCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS)
                showFactionCrestsCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS)
                sameVehicleVelocityCheckBox.text = StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY)
                enableNewVehiclesCheckBox.text = StarWarsBundle.message(BundleConstants.ENABLE_NEW_VEHICLES)
                solidProgressBarColorCheckBox.text = StarWarsBundle.message(BundleConstants.SOLID_PROGRESS_BAR_COLOR)
                showVehicleCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_VEHICLE)
                drawSilhouettesCheckBox.text = StarWarsBundle.message(BundleConstants.DRAW_SILHOUETTES)
                changeVehicleAfterPassCheckBox.text = StarWarsBundle.message(BundleConstants.CHANGE_VEHICLE_AFTER_PASS)
                selectionLabel.text = StarWarsBundle.message(BundleConstants.SELECTOR)
                languageLabel.text = StarWarsBundle.message(BundleConstants.LANGUAGE)
            }
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        languageComboBox.item = starWarsState.language // TODO: not null here
        showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
        showVehicleCheckBox.isSelected = starWarsState.showVehicle
        showToolTipsCheckBox.isSelected = starWarsState.showToolTips
        showFactionCrestsCheckBox.isSelected = starWarsState.showFactionCrests
        sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity
        enableNewVehiclesCheckBox.isSelected = starWarsState.enableNewVehicles
        solidProgressBarColorCheckBox.isSelected = starWarsState.solidProgressBarColor
        drawSilhouettesCheckBox.isSelected = starWarsState.drawSilhouettes
        changeVehicleAfterPassCheckBox.isSelected = starWarsState.changeVehicleAfterPass
        numberOfPassesUntilVehicleChangeSpinner.value = starWarsState.numberOfPassesUntilVehicleChange
        vehicleSelectorComboBox.item = starWarsState.vehicleSelector // TODO: not null here

        StarWarsBundle.setLocale(language.toLocale())
        firePropertyChange(
            this::language.name,
            oldLanguage,
            language,
        )
    }
}
