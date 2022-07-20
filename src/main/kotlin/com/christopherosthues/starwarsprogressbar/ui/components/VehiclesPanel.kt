package com.christopherosthues.starwarsprogressbar.ui.components

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.ui.events.VehicleClickListener
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

private const val FACTION_PADDING = 5
private const val SELECTION_PANEL_BOTTOM_PADDING = 10

internal class VehiclesPanel : JTitledPanel(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE)) {
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

    fun addVehicleListener(listener: VehicleClickListener) {
        factionPanels.forEach { it.addVehicleListener(listener) }
    }

    private fun createSelectionPanel() {
        val selectionPanel = JPanel(BorderLayout())
        selectionPanel.border = EmptyBorder(0, 0, SELECTION_PANEL_BOTTOM_PADDING, 0)

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

        FactionHolder.defaultFactions.forEach { faction ->
            vehicleRowCount = 0

            val vehiclesAvailable = faction.vehicles.any()
            if (vehiclesAvailable) {
                val factionPanel = FactionPanel(faction)
                factionPanel.addPropertyChangeListener(FactionPanel::selectedVehiclesCount.name) {
                    updateSelectionButtons()
                }

                addFactionPanel(factionPanel, faction, vehiclePanel)
            }
        }

        add(vehiclePanel)
    }

    private fun addFactionPanel(factionPanel: FactionPanel, faction: StarWarsFaction, vehiclePanel: JPanel) {
        val localizedName = StarWarsBundle.message(faction.localizationKey)
        factionPanel.border = BorderFactory.createTitledBorder(localizedName)

        val isFactionCountEven = factionCount++ % 2 == 0
        val gridBagConstraints = GridBagConstraints()
        val leftPadding = if (isFactionCountEven) 0 else FACTION_PADDING
        val rightPadding = if (isFactionCountEven) FACTION_PADDING else 0
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridx = if (isFactionCountEven) 0 else 1
        gridBagConstraints.gridy = factionRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 0.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
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
        val numberOfVehicles = FactionHolder.defaultVehicles.size

        if (selected == numberOfVehicles) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectedVehiclesCheckBox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfVehicles) BundleConstants.DESELECT_ALL
            else BundleConstants.SELECT_ALL
        )
        selectedVehiclesCheckBox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected, numberOfVehicles,
            selectionText
        )
    }
}
