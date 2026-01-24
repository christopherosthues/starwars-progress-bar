package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.DETERMINATE_ENTITY_SELECTOR_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.DETERMINATE_ORDER_SELECTOR_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.INDETERMINATE_ENTITY_SELECTOR_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.INDETERMINATE_ORDER_SELECTOR_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENTITY_SELECTOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SELECTOR
import com.christopherosthues.starwarsprogressbar.selectors.EntitySelectionType
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.intellij.openapi.ui.ComboBox
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel

private const val GAP = 5
private const val NUMBER_OF_ROWS = 2
private const val HORIZONTAL_GAP = 8

internal class SelectionOptionsPanel(starWarsState: StarWarsState) : JTitledPanel(StarWarsBundle.message(BundleConstants.SELECTION_OPTIONS)) {
    private val determinateSelectorLabel = JLabel(StarWarsBundle.message(BundleConstants.ORDER_SELECTOR))
    private val determinateSelectorComboBox = ComboBox(SelectionType.entries.toTypedArray())
    private val indeterminateSelectorLabel = JLabel(StarWarsBundle.message(BundleConstants.ORDER_SELECTOR))
    private val indeterminateOrderSelectorComboBox = ComboBox(SelectionType.entries.toTypedArray())

    private val determinateEntitySelectorLabel = JLabel(StarWarsBundle.message(BundleConstants.ENTITY_SELECTOR))
    private val determinateEntitySelectorComboBox = ComboBox(EntitySelectionType.entries.toTypedArray())
    private val indeterminateEntitySelectorLabel = JLabel(StarWarsBundle.message(BundleConstants.ENTITY_SELECTOR))
    private val indeterminateEntitySelectorComboBox = ComboBox(EntitySelectionType.entries.toTypedArray())

    init {
        layout = GridLayout(NUMBER_OF_ROWS, 2, GAP, GAP)

        determinateSelectorComboBox.addItemListener {
            val oldValue = starWarsState.determinateOrderSelector
            val newValue = determinateSelectorComboBox.selectedItem as SelectionType? ?: DEFAULT_SELECTOR
            firePropertyChange(
                DETERMINATE_ORDER_SELECTOR_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.determinateOrderSelector = newValue
        }
        indeterminateOrderSelectorComboBox.addItemListener {
            val oldValue = starWarsState.indeterminateOrderSelector
            val newValue = indeterminateOrderSelectorComboBox.selectedItem as SelectionType? ?: DEFAULT_SELECTOR
            firePropertyChange(
                INDETERMINATE_ORDER_SELECTOR_EVENT,
                oldValue,
                newValue,
            )
            starWarsState.indeterminateOrderSelector = newValue
        }
        determinateEntitySelectorComboBox.addItemListener {
            val oldValue = starWarsState.determinateEntitySelector
            val newValue = determinateEntitySelectorComboBox.selectedItem as EntitySelectionType? ?: DEFAULT_ENTITY_SELECTOR
            firePropertyChange(DETERMINATE_ENTITY_SELECTOR_EVENT, oldValue, newValue)
            starWarsState.determinateEntitySelector = newValue
        }
        indeterminateEntitySelectorComboBox.addItemListener {
            val oldValue = starWarsState.indeterminateEntitySelector
            val newValue = indeterminateEntitySelectorComboBox.selectedItem as EntitySelectionType? ?: DEFAULT_ENTITY_SELECTOR
            firePropertyChange(INDETERMINATE_ENTITY_SELECTOR_EVENT, oldValue, newValue)
            starWarsState.indeterminateEntitySelector = newValue
        }

        val determinatePanelForSelection = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        determinatePanelForSelection.add(determinateSelectorLabel)
        determinatePanelForSelection.add(determinateSelectorComboBox)
        val indeterminatePanelForSelection = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        indeterminatePanelForSelection.add(indeterminateSelectorLabel)
        indeterminatePanelForSelection.add(indeterminateOrderSelectorComboBox)

        val determinatePanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        determinatePanel.add(determinateEntitySelectorLabel)
        determinatePanel.add(determinateEntitySelectorComboBox)
        val indeterminatePanel = JPanel(FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0))
        indeterminatePanel.add(indeterminateEntitySelectorLabel)
        indeterminatePanel.add(indeterminateEntitySelectorComboBox)

        add(determinatePanelForSelection)
        add(indeterminatePanelForSelection)
        add(determinatePanel)
        add(indeterminatePanel)
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            determinateSelectorLabel.text = StarWarsBundle.message(BundleConstants.ORDER_SELECTOR)
            indeterminateSelectorLabel.text = StarWarsBundle.message(BundleConstants.ORDER_SELECTOR)
            determinateEntitySelectorLabel.text = StarWarsBundle.message(BundleConstants.ENTITY_SELECTOR)
            indeterminateEntitySelectorLabel.text = StarWarsBundle.message(BundleConstants.ENTITY_SELECTOR)
            title = StarWarsBundle.message(BundleConstants.SELECTION_OPTIONS)
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        determinateSelectorComboBox.item = starWarsState.determinateOrderSelector
        indeterminateOrderSelectorComboBox.item = starWarsState.indeterminateOrderSelector
        determinateEntitySelectorComboBox.item = starWarsState.determinateEntitySelector
        indeterminateEntitySelectorComboBox.item = starWarsState.indeterminateEntitySelector
    }
}
