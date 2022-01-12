package com.christopherosthues.starwarsprogressbar.ui.components

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.ui.Faction
import com.christopherosthues.starwarsprogressbar.ui.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsState
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

internal class VehiclesPanel() : JTitledPanel(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE)) {
    private val selectedVehiclesCheckBox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val factionPanels: MutableList<FactionPanel> = mutableListOf()

    private var vehicleRowCount: Int = 0
    private var factionRowCount: Int = 0
    private var factionCount: Int = 0

    val enabledVehicles: Map<String, Boolean>
        get() = factionPanels.map { it.enabledVehicles }.fold(hashMapOf()) { acc, map ->
            map.forEach {
                acc.merge(it.key, it.value) { new, _ -> new }
            }
            acc
        }

    private val selectedVehiclesCount: Int
        get() = factionPanels.sumBy { it.selectedVehiclesCount.get() }

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

    private fun createSelectionPanel() {
        val selectionPanel = JPanel(BorderLayout())
        selectionPanel.border = EmptyBorder(0, 0, 10, 0)

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

        Faction.DEFAULT_FACTIONS.forEach { faction ->
            vehicleRowCount = 0

            val vehiclesAvailable = StarWarsVehicle.DEFAULT_VEHICLES.any { it.faction == faction }
            if (vehiclesAvailable) {
                val factionPanel = FactionPanel(faction)
                factionPanel.addPropertyChangeListener(FactionPanel::selectedVehiclesCount.name) { updateSelectionButtons() }

                addFactionPanel(factionPanel, faction, vehiclePanel)
            }
        }

        add(vehiclePanel)
    }

    private fun addFactionPanel(factionPanel: FactionPanel, faction: Faction, vehiclePanel: JPanel) {
        factionPanel.border = BorderFactory.createTitledBorder(faction.factionName)

        val isFactionCountEven = factionCount++ % 2 == 0
        val gridBagConstraints = GridBagConstraints()
        val leftPadding = if (isFactionCountEven) 0 else 5
        val rightPadding = if (isFactionCountEven) 5 else 0
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridx = if (isFactionCountEven) 0 else 1
        gridBagConstraints.gridy = factionRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 0.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        gridBagConstraints.insets = JBUI.insets(0, leftPadding, 5, rightPadding)

        if (!isFactionCountEven) {
            factionRowCount++
        }

        factionPanels.add(factionPanel)
        vehiclePanel.add(factionPanel, gridBagConstraints)
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount
        val numberOfVehicles = StarWarsVehicle.DEFAULT_VEHICLES.size

        if (selected == numberOfVehicles) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText =
            StarWarsBundle.message(if (selected == numberOfVehicles) BundleConstants.DESELECT_ALL else BundleConstants.SELECT_ALL)
        selectedVehiclesCheckBox.text =
            StarWarsBundle.message(BundleConstants.SELECTED, selected, numberOfVehicles, selectionText)
    }
}
