package com.github.christopherosthues.starwarsprogressbar.ui.components

import com.github.christopherosthues.starwarsprogressbar.BundleConstants
import com.github.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.github.christopherosthues.starwarsprogressbar.ui.Faction
import com.github.christopherosthues.starwarsprogressbar.ui.StarWarsResourceLoader
import com.github.christopherosthues.starwarsprogressbar.ui.StarWarsVehicle
import com.github.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsState
import com.intellij.ui.roots.ScalableIconComponent
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import com.jetbrains.rd.util.AtomicInteger
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ItemEvent
import java.util.*
import java.util.stream.Collectors
import javax.swing.*
import javax.swing.border.EmptyBorder
import kotlin.collections.HashMap

internal class VehiclesPanel() : JTitledPanel(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE)) {
    private val selectedVehiclesCheckBox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)

    private val factionCheckboxes: MutableMap<Faction, ThreeStateCheckBox> = EnumMap(Faction::class.java)
    private val vehicleCheckboxes: MutableMap<String, JCheckBox> = HashMap()

    private val selectedVehiclesCount: AtomicInteger = AtomicInteger(0)

    private var vehicleRowCount: Int = 0
    private var factionRowCount: Int = 0
    private var factionCount: Int = 0

    val enabledVehicles: Map<String, Boolean>
        get() = vehicleCheckboxes.entries.stream()
            .collect(Collectors.toMap({ entry -> entry.key }, { entry -> entry.value.isSelected }))

    init {
        val vehiclesPanelLayout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        layout = vehiclesPanelLayout

        createSelectionPanel()
        createVehiclePanel()

        updateSelectionButtons()
    }

    fun updateUI(starWarsState: StarWarsState) {
        starWarsState.vehiclesEnabled.forEach { (vehicleName: String, isEnabled: Boolean) ->
            vehicleCheckboxes.computeIfPresent(vehicleName) { _, checkbox: JCheckBox ->
                checkbox.isSelected = isEnabled
                checkbox
            }
        }
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
        vehicleCheckboxes.values.forEach { c: JCheckBox -> c.isSelected = isSelected }
    }

    private fun createVehiclePanel() {
        val vehiclePanel = JPanel(GridBagLayout())
        factionCount = 0
        factionRowCount = 0

        Faction.DEFAULT_FACTIONS.forEach { faction ->
            vehicleRowCount = 0

            val vehiclesAvailable = StarWarsVehicle.DEFAULT_VEHICLES.any { it.faction == faction }
            if (vehiclesAvailable) {
                val factionPanel = JPanel(GridBagLayout())

                addFactionCheckBox(faction, factionPanel)

                StarWarsVehicle.DEFAULT_VEHICLES.stream().filter { vehicle ->
                    vehicle.faction == faction
                }.sorted(Comparator.comparing(StarWarsVehicle::vehicleName)).forEach { vehicle ->
                    addVehicleCheckBox(vehicle, factionPanel)
                }

                addFactionPanel(factionPanel, faction, vehiclePanel)
            }
        }

        add(vehiclePanel)
    }

    private fun addFactionCheckBox(faction: Faction, factionPanel: JPanel) {
        val factionCheckbox = ThreeStateCheckBox()
        factionCheckbox.isThirdStateEnabled = false
        factionCheckbox.addItemListener {

            // TODO select/deselect all vehicles of this faction
        }
        factionCheckboxes[faction] = factionCheckbox

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridwidth = 2
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = vehicleRowCount++
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.WEST
        gridBagConstraints.insets = JBUI.insets(10, 0, 0, 0)

        factionPanel.add(factionCheckbox, gridBagConstraints)
    }

    private fun addVehicleCheckBox(vehicle: StarWarsVehicle, factionPanel: JPanel) {
        val checkBox = JCheckBox(vehicle.vehicleName, true)
        checkBox.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                selectedVehiclesCount.incrementAndGet()
            } else if (it.stateChange == ItemEvent.DESELECTED) {
                selectedVehiclesCount.decrementAndGet()
            }

            updateSelectionButtons()
        }

        addLabeledComponent(
            factionPanel,
            ScalableIconComponent(StarWarsResourceLoader.getIcon(vehicle.fileName)),
            checkBox
        )
        vehicleCheckboxes[vehicle.fileName] = checkBox
        selectedVehiclesCount.incrementAndGet()
    }

    private fun addFactionPanel(factionPanel: JPanel, faction: Faction, vehiclePanel: JPanel) {
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

        vehiclePanel.add(factionPanel, gridBagConstraints)
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount.get()
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

        factionCheckboxes.forEach { (faction, factionCheckbox) ->
            val numberOfVehiclesInFaction = StarWarsVehicle.DEFAULT_VEHICLES.count { vehicle -> vehicle.faction == faction }
            val numberOfSelectedVehiclesInFaction = vehicleCheckboxes.count { (fileName, checkbox) ->
                val vehicle = StarWarsVehicle.DEFAULT_VEHICLES.find { vehicle -> vehicle.fileName == fileName }
                vehicle != null && vehicle.faction == faction && checkbox.isSelected
            }

            if (numberOfSelectedVehiclesInFaction == numberOfVehiclesInFaction) {
                factionCheckbox.state = ThreeStateCheckBox.State.SELECTED
            } else if (numberOfSelectedVehiclesInFaction > 0) {
                factionCheckbox.state = ThreeStateCheckBox.State.DONT_CARE
            } else {
                factionCheckbox.state = ThreeStateCheckBox.State.NOT_SELECTED
            }

            val factionSelectionText =
                StarWarsBundle.message(if (numberOfSelectedVehiclesInFaction == numberOfVehiclesInFaction) BundleConstants.DESELECT_ALL else BundleConstants.SELECT_ALL)
            factionCheckbox.text =
                StarWarsBundle.message(BundleConstants.SELECTED, numberOfSelectedVehiclesInFaction, numberOfVehiclesInFaction, factionSelectionText)
        }
    }

    private fun addLabeledComponent(panel: JPanel, label: JComponent?, component: JComponent) {
        val gridBagConstraints = GridBagConstraints()
        if (label == null) {
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = vehicleRowCount + 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(10, 0, 0, 0)

            panel.add(component, gridBagConstraints)

            vehicleRowCount += 2
        } else {
            gridBagConstraints.gridwidth = 1
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = vehicleRowCount++
            gridBagConstraints.weightx = 0.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.NONE
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.insets = JBUI.insets(10, 0, 0, 10)

            panel.add(label, gridBagConstraints)

            gridBagConstraints.gridx = 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(10, 0, 0, 0)

            panel.add(component, gridBagConstraints)
        }
    }
}