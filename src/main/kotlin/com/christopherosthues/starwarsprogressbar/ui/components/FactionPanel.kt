package com.christopherosthues.starwarsprogressbar.ui.components

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.StarWarsResourceLoader
import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsState
import com.intellij.ui.roots.ScalableIconComponent
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import com.jetbrains.rd.util.AtomicInteger
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ItemEvent
import java.beans.PropertyChangeEvent
import java.util.stream.Collectors
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

private const val PADDING = 10

internal class FactionPanel(private val faction: StarWarsFaction) : JPanel(GridBagLayout()) {
    private val selectVehiclesCheckbox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val vehiclesCheckboxes: MutableMap<String, JCheckBox> = HashMap()
    private var vehicleRowCount: Int = 0

    val selectedVehiclesCount: AtomicInteger = AtomicInteger(0)

    val enabledVehicles: MutableMap<String, Boolean>
        get() = vehiclesCheckboxes.entries.stream()
            .collect(Collectors.toMap({ entry -> entry.key }, { entry -> entry.value.isSelected }))


    init {
        val vehiclesAvailable = faction.vehicles.any()
        if (vehiclesAvailable) {
            addFactionCheckBox()

            val localizedNameComparator = compareBy<StarWarsVehicle> { StarWarsBundle.message(it.localizationKey) }

            faction.vehicles.stream().sorted(localizedNameComparator).forEach { vehicle ->
                addVehicleCheckBox(vehicle)
            }

            updateSelectionButtons()
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        starWarsState.vehiclesEnabled.forEach { (vehicleName: String, isEnabled: Boolean) ->
            vehiclesCheckboxes.computeIfPresent(vehicleName) { _, checkbox: JCheckBox ->
                checkbox.isSelected = isEnabled
                checkbox
            }
        }
    }

    fun selectVehicles(isSelected: Boolean) {
        vehiclesCheckboxes.values.forEach { c: JCheckBox -> c.isSelected = isSelected }
    }

    private fun addFactionCheckBox() {
        selectVehiclesCheckbox.isThirdStateEnabled = false
        selectVehiclesCheckbox.addItemListener {
            val isSelected = selectVehiclesCheckbox.state == ThreeStateCheckBox.State.SELECTED
            selectVehicles(isSelected)
        }

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridwidth = 2
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = vehicleRowCount++
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.WEST
        gridBagConstraints.insets = JBUI.insets(PADDING, 0, 0, 0)

        add(selectVehiclesCheckbox, gridBagConstraints)
    }

    private fun addVehicleCheckBox(vehicle: StarWarsVehicle) {
        val localizedName = StarWarsBundle.message(vehicle.localizationKey)
        val checkBox = JCheckBox(localizedName, true)
        checkBox.addItemListener {
            val oldValue = selectedVehiclesCount.get()
            if (it.stateChange == ItemEvent.SELECTED) {
                selectedVehiclesCount.incrementAndGet()
            } else if (it.stateChange == ItemEvent.DESELECTED) {
                selectedVehiclesCount.decrementAndGet()
            }

            propertyChangeListeners.forEach { l ->
                val propertyChangeEvent = PropertyChangeEvent(
                    this,
                    FactionPanel::selectedVehiclesCount.name,
                    oldValue,
                    selectedVehiclesCount.get()
                )
                l.propertyChange(propertyChangeEvent)
            }

            updateSelectionButtons()
        }

        addLabeledComponent(
            ScalableIconComponent(StarWarsResourceLoader.getIcon(vehicle.fileName)),
            checkBox
        )
        vehiclesCheckboxes[vehicle.id] = checkBox
        selectedVehiclesCount.incrementAndGet()
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount.get()
        val numberOfVehicles = faction.vehicles.size

        if (selected == numberOfVehicles) {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfVehicles) BundleConstants.DESELECT_ALL
            else BundleConstants.SELECT_ALL
        )
        selectVehiclesCheckbox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected, numberOfVehicles,
            selectionText)
    }

    private fun addLabeledComponent(label: JComponent?, component: JComponent) {
        val gridBagConstraints = GridBagConstraints()
        if (label == null) {
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = vehicleRowCount + 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(PADDING, 0, 0, 0)

            add(component, gridBagConstraints)

            vehicleRowCount += 2
        } else {
            gridBagConstraints.gridwidth = 1
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = vehicleRowCount++
            gridBagConstraints.weightx = 0.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.NONE
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.insets = JBUI.insets(PADDING, 0, 0, PADDING)

            add(label, gridBagConstraints)

            gridBagConstraints.gridx = 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(PADDING, 0, 0, 0)

            add(component, gridBagConstraints)
        }
    }
}
