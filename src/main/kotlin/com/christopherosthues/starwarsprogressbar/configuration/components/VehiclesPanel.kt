package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.ui.events.StarWarsEntityClickListener
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

private const val FACTION_PADDING = 5
private const val SELECTION_PANEL_BOTTOM_PADDING = 10

internal class VehiclesPanel(private val starWarsState: StarWarsState) : JTitledPanel(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE)) {
    private val selectedVehiclesCheckBox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val factionPanels: MutableList<VehicleFactionPanel> = mutableListOf()

    private var vehicleRowCount: Int = 0
    private var factionRowCount: Int = 0
    private var factionCount: Int = 0

    private val selectedVehiclesCount: Int
        get() = factionPanels.sumOf { it.selectedVehiclesCount.get() }

    init {
        val vehiclesPanelLayout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        layout = vehiclesPanelLayout

        createSelectionPanel()
        createVehiclePanel()

        updateSelectionButtons()
    }

    fun updateUI(starWarsState: StarWarsState) {
        factionPanels.forEach { it.updateUI(starWarsState) }
    }

    fun addStarWarsEntityListener(listener: StarWarsEntityClickListener) {
        factionPanels.forEach { it.addStarWarsEntityListener(listener) }
    }

    private fun createSelectionPanel() {
        val selectionPanel = JPanel(BorderLayout())
        selectionPanel.border = JBUI.Borders.emptyBottom(SELECTION_PANEL_BOTTOM_PADDING)

        selectedVehiclesCheckBox.isThirdStateEnabled = false
        selectedVehiclesCheckBox.addItemListener {
            val isSelected = selectedVehiclesCheckBox.state == ThreeStateCheckBox.State.SELECTED
            selectVehicles(isSelected)
        }

        selectionPanel.add(selectedVehiclesCheckBox, BorderLayout.WEST)

        add(selectionPanel)
    }

    private fun selectVehicles(isSelected: Boolean) {
        factionPanels.forEach { it.selectVehicles(isSelected) }
    }

    private fun createVehiclePanel() {
        val vehiclePanel = JPanel(GridBagLayout())
        factionCount = 0
        factionRowCount = 0

        StarWarsFactionHolder.defaultVehicleFactions.forEach { faction ->
            vehicleRowCount = 0

            val vehiclesAvailable = faction.data.any()
            if (vehiclesAvailable) {
                val factionPanel = VehicleFactionPanel(starWarsState, faction)
                factionPanel.addPropertyChangeListener(VehicleFactionPanel::selectedVehiclesCount.name) {
                    updateSelectionButtons()
                }

                addFactionPanel(factionPanel, vehiclePanel)
            }
        }

        add(vehiclePanel)
    }

    private fun addFactionPanel(factionPanel: VehicleFactionPanel, vehiclePanel: JPanel) {
        val isFactionCountEven = factionCount++ % 2 == 0
        val gridBagConstraints = GridBagConstraints()
        val leftPadding = if (isFactionCountEven) 0 else FACTION_PADDING
        val rightPadding = if (isFactionCountEven) FACTION_PADDING else 0
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridx = if (isFactionCountEven) 0 else 1
        gridBagConstraints.gridy = factionRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.fill = GridBagConstraints.BOTH
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        gridBagConstraints.insets = JBUI.insets(0, leftPadding, FACTION_PADDING, rightPadding)

        if (!isFactionCountEven) {
            factionRowCount++
        }

        factionPanels.add(factionPanel)
        vehiclePanel.add(factionPanel, gridBagConstraints)
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount
        val numberOfVehicles = StarWarsFactionHolder.defaultVehicles.size

        if (selected == numberOfVehicles) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfVehicles) {
                BundleConstants.DESELECT_ALL
            } else {
                BundleConstants.SELECT_ALL
            },
        )
        selectedVehiclesCheckBox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected,
            numberOfVehicles,
            selectionText,
        )
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            title = StarWarsBundle.message(BundleConstants.VEHICLES_TITLE)
            updateSelectionButtons()
        }
        factionPanels.forEach {
            it.addPropertyChangeListener(uiOptionsPanel)
        }
    }
}
