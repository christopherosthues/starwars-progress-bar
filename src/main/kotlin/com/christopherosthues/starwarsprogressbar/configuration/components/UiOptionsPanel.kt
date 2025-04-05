package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.CHANGE_VEHICLE_AFTER_PASS_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.DRAW_SILHOUETTES_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.ENABLE_NEW_VEHICLES_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.Language
import com.christopherosthues.starwarsprogressbar.configuration.NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SAME_VELOCITY_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SHOW_FACTION_CRESTS_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SHOW_TOOL_TIPS_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SHOW_VEHICLE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SHOW_VEHICLE_NAMES_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.SOLID_PROGRESS_BAR_COLOR_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.configuration.VEHICLE_SELECTOR_EVENT
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_LANGUAGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SELECTOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_ICON
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
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

internal class UiOptionsPanel(starWarsState: StarWarsState) : JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS)) {
    private val showNameCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_NAME), DEFAULT_SHOW_NAMES)
    private val showToolTipsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), DEFAULT_SHOW_TOOLTIPS)
    private val showFactionCrestsCheckBox =
        JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS), DEFAULT_SHOW_FACTION_CRESTS)
    private val sameVelocityCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SAME_VELOCITY),
        DEFAULT_SAME_VELOCITY,
    )
    private val enableNewCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.ENABLE_NEW),
        DEFAULT_ENABLE_NEW,
    )
    private val solidProgressBarColorCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SOLID_PROGRESS_BAR_COLOR),
        DEFAULT_SOLID_PROGRESS_BAR_COLOR,
    )
    private val showIconCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.SHOW_ICON),
        DEFAULT_SHOW_ICON,
    )
    private val drawSilhouettesCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.DRAW_SILHOUETTES),
        DEFAULT_DRAW_SILHOUETTES,
    )
    private val changeAfterPassCheckBox = JBCheckBox(
        StarWarsBundle.message(BundleConstants.CHANGE_AFTER_PASS),
        DEFAULT_CHANGE_AFTER_PASS,
    )
    private val numberOfPassesUntilChangeSpinner =
        JBIntSpinner(DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE, MINIMUM_NUMBER_OF_PASSES, MAXIMUM_NUMBER_OF_PASSES)

    private val selectionLabel = JLabel(StarWarsBundle.message(BundleConstants.SELECTOR))
    private val selectorComboBox = ComboBox(SelectionType.entries.toTypedArray())
    private val languageLabel = JLabel(StarWarsBundle.message(BundleConstants.LANGUAGE))
    private val languageComboBox = ComboBox(arrayOf(Language.ENGLISH, Language.GERMAN, Language.SPANISH))

    private var oldLanguage: Language? = null

    init {

        languageComboBox.addItemListener {
            if (it.stateChange == ItemEvent.DESELECTED) {
                oldLanguage = it.item as Language?
            }
            if (it.stateChange == ItemEvent.SELECTED) {
                val newLanguage = it.item as Language? ?: DEFAULT_LANGUAGE
                starWarsState.language = newLanguage
                StarWarsBundle.setLocale(newLanguage.toLocale())
                firePropertyChange(
                    LANGUAGE_EVENT,
                    oldLanguage,
                    newLanguage,
                )
            }
        }
        layout = GridLayout(NUMBER_OF_ROWS, 2, GAP, GAP)

        showIconCheckBox.addItemListener {
            val oldValue = starWarsState.showIcon
            val newValue = showIconCheckBox.isSelected
            firePropertyChange(
                SHOW_VEHICLE_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.showIcon = newValue
        }
        showNameCheckBox.addItemListener {
            val oldValue = starWarsState.showNames
            val newValue = showNameCheckBox.isSelected
            firePropertyChange(
                SHOW_VEHICLE_NAMES_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.showNames = newValue
        }
        showToolTipsCheckBox.addItemListener {
            val oldValue = starWarsState.showToolTips
            val newValue = showToolTipsCheckBox.isSelected
            firePropertyChange(
                SHOW_TOOL_TIPS_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.showToolTips = newValue
        }
        showFactionCrestsCheckBox.addItemListener {
            val oldValue = starWarsState.showNames
            val newValue = showFactionCrestsCheckBox.isSelected
            firePropertyChange(
                SHOW_FACTION_CRESTS_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.showFactionCrests = newValue
        }
        sameVelocityCheckBox.addItemListener {
            val oldValue = starWarsState.showNames
            val newValue = sameVelocityCheckBox.isSelected
            firePropertyChange(
                SAME_VELOCITY_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.sameVelocity = newValue
        }
        enableNewCheckBox.addItemListener {
            val oldValue = starWarsState.enableNew
            val newValue = enableNewCheckBox.isSelected
            firePropertyChange(
                ENABLE_NEW_VEHICLES_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.enableNew = newValue
        }
        solidProgressBarColorCheckBox.addItemListener {
            val oldValue = starWarsState.solidProgressBarColor
            val newValue = solidProgressBarColorCheckBox.isSelected
            firePropertyChange(
                SOLID_PROGRESS_BAR_COLOR_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.solidProgressBarColor = newValue
        }
        drawSilhouettesCheckBox.addItemListener {
            val oldValue = starWarsState.drawSilhouettes
            val newValue = drawSilhouettesCheckBox.isSelected
            firePropertyChange(
                DRAW_SILHOUETTES_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.drawSilhouettes = newValue
        }
        changeAfterPassCheckBox.addItemListener {
            val oldValue = starWarsState.changeAfterPass
            val newValue = changeAfterPassCheckBox.isSelected
            numberOfPassesUntilChangeSpinner.isEnabled = newValue
            firePropertyChange(
                CHANGE_VEHICLE_AFTER_PASS_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.changeAfterPass = newValue
        }
        numberOfPassesUntilChangeSpinner.addChangeListener {
            val oldValue = starWarsState.numberOfPassesUntilChange
            val newValue = numberOfPassesUntilChangeSpinner.number
            firePropertyChange(
                NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.numberOfPassesUntilChange = newValue
        }
        numberOfPassesUntilChangeSpinner.isEnabled = changeAfterPassCheckBox.isSelected
        selectorComboBox.addItemListener {
            val oldValue = starWarsState.selector
            val newValue = selectorComboBox.selectedItem as SelectionType? ?: DEFAULT_SELECTOR
            firePropertyChange(
                VEHICLE_SELECTOR_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.selector = newValue
        }

        val languagePanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        languagePanel.add(languageLabel)
        languagePanel.add(languageComboBox)

        val passesPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        passesPanel.add(changeAfterPassCheckBox)
        passesPanel.add(numberOfPassesUntilChangeSpinner)

        val selectionPanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        selectionPanel.add(selectionLabel)
        selectionPanel.add(selectorComboBox)

        add(languagePanel)
        add(JPanel())
        add(showIconCheckBox)
        add(sameVelocityCheckBox)
        add(showNameCheckBox)
        add(enableNewCheckBox)
        add(showToolTipsCheckBox)
        add(solidProgressBarColorCheckBox)
        add(showFactionCrestsCheckBox)
        add(drawSilhouettesCheckBox)
        add(passesPanel)
        add(selectionPanel)

        addPropertyChangeListener {
            if (it.propertyName == LANGUAGE_EVENT) {
                title = StarWarsBundle.message(BundleConstants.UI_OPTIONS)
                showNameCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_NAME)
                showToolTipsCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS)
                showFactionCrestsCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_FACTION_CRESTS)
                sameVelocityCheckBox.text = StarWarsBundle.message(BundleConstants.SAME_VELOCITY)
                enableNewCheckBox.text = StarWarsBundle.message(BundleConstants.ENABLE_NEW)
                solidProgressBarColorCheckBox.text = StarWarsBundle.message(BundleConstants.SOLID_PROGRESS_BAR_COLOR)
                showIconCheckBox.text = StarWarsBundle.message(BundleConstants.SHOW_ICON)
                drawSilhouettesCheckBox.text = StarWarsBundle.message(BundleConstants.DRAW_SILHOUETTES)
                changeAfterPassCheckBox.text = StarWarsBundle.message(BundleConstants.CHANGE_AFTER_PASS)
                selectionLabel.text = StarWarsBundle.message(BundleConstants.SELECTOR)
                languageLabel.text = StarWarsBundle.message(BundleConstants.LANGUAGE)
            }
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        languageComboBox.item = starWarsState.language
        showNameCheckBox.isSelected = starWarsState.showNames
        showIconCheckBox.isSelected = starWarsState.showIcon
        showToolTipsCheckBox.isSelected = starWarsState.showToolTips
        showFactionCrestsCheckBox.isSelected = starWarsState.showFactionCrests
        sameVelocityCheckBox.isSelected = starWarsState.sameVelocity
        enableNewCheckBox.isSelected = starWarsState.enableNew
        solidProgressBarColorCheckBox.isSelected = starWarsState.solidProgressBarColor
        drawSilhouettesCheckBox.isSelected = starWarsState.drawSilhouettes
        changeAfterPassCheckBox.isSelected = starWarsState.changeAfterPass
        numberOfPassesUntilChangeSpinner.value = starWarsState.numberOfPassesUntilChange
        selectorComboBox.item = starWarsState.selector

        val language = languageComboBox.selectedItem as Language? ?: DEFAULT_LANGUAGE
        StarWarsBundle.setLocale(language.toLocale())
        firePropertyChange(
            LANGUAGE_EVENT,
            oldLanguage,
            language,
        )
    }
}
