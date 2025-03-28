package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.configuration.borders.TitledIconBorder
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.events.StarWarsEntityClickListener
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.roots.ScalableIconComponent
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import com.jetbrains.rd.util.AtomicInteger
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ItemEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.beans.PropertyChangeEvent
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

private const val TOP_PADDING = 10
private const val LEFT_PADDING = 5

internal class VehicleFactionPanel(
    private val starWarsState: StarWarsState,
    private val faction: StarWarsFaction<StarWarsVehicle>
) : JPanel(GridBagLayout()) {
    private val selectVehiclesCheckbox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val vehiclesCheckboxes: MutableMap<String, JCheckBox> = HashMap()
    private var vehicleRowCount: Int = 0
    private var vehicleClickListener: StarWarsEntityClickListener? = null

    val selectedVehiclesCount: AtomicInteger = AtomicInteger(0)

    init {
        initFactionPanel()
    }

    private fun initFactionPanel() {
        val localizedName = StarWarsBundle.message("${BundleConstants.VEHICLES_FACTION}${faction.id}")
        border = TitledIconBorder(localizedName, faction.id, "vehicles")
        val vehiclesAvailable = faction.data.any()
        if (vehiclesAvailable) {
            addFactionCheckBox()

            val localizedNameComparator =
                compareBy<StarWarsVehicle> { StarWarsBundle.message(it.localizationKey).lowercase() }

            faction.data.stream().sorted(localizedNameComparator).forEach { vehicle ->
                addVehicleCheckBox(vehicle)
            }

            updateSelectionButtons()

            addEmptyBottomElement()
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        starWarsState.vehiclesEnabled.forEach { (vehicleName: String, isEnabled: Boolean) ->
            vehiclesCheckboxes.computeIfPresent(vehicleName) { _, checkbox: JCheckBox ->
                checkbox.isSelected = isEnabled
                checkbox
            }
        }
        vehiclesCheckboxes.forEach { (vehicleName, checkbox) ->
            if (!starWarsState.vehiclesEnabled.containsKey(vehicleName)) {
                checkbox.isSelected = starWarsState.enableNewVehicles
            }
        }
    }

    fun selectVehicles(isSelected: Boolean) {
        vehiclesCheckboxes.values.forEach { c: JCheckBox ->
            c.isSelected = isSelected
            faction.data.forEach {
                starWarsState.vehiclesEnabled[it.entityId] = isSelected
            }
        }
    }

    fun addStarWarsEntityListener(listener: StarWarsEntityClickListener) {
        vehicleClickListener = listener
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
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.anchor = GridBagConstraints.WEST
        gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, 0)

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
            starWarsState.vehiclesEnabled[vehicle.entityId] = checkBox.isSelected

            propertyChangeListeners.forEach { l ->
                val propertyChangeEvent = PropertyChangeEvent(
                    this,
                    VehicleFactionPanel::selectedVehiclesCount.name,
                    oldValue,
                    selectedVehiclesCount.get(),
                )
                l.propertyChange(propertyChangeEvent)
            }

            updateSelectionButtons()
        }

        val iconComponent = ScalableIconComponent(StarWarsResourceLoader.getIcon(vehicle.fileName))
        iconComponent.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                vehicleClickListener?.starWarsEntityClicked(vehicle)
            }
        })
        addLabeledComponent(
            iconComponent,
            checkBox,
        )
        vehiclesCheckboxes[vehicle.entityId] = checkBox
        selectedVehiclesCount.incrementAndGet()
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount.get()
        val numberOfVehicles = faction.data.size

        if (selected == numberOfVehicles) {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectVehiclesCheckbox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfVehicles) {
                BundleConstants.DESELECT_ALL
            } else {
                BundleConstants.SELECT_ALL
            },
        )
        selectVehiclesCheckbox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected,
            numberOfVehicles,
            selectionText,
        )
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
            gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, 0)

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
            gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, TOP_PADDING)

            add(label, gridBagConstraints)

            gridBagConstraints.gridx = 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, 0)

            add(component, gridBagConstraints)
        }
    }

    private fun addEmptyBottomElement() {
        val element = JPanel()
        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridwidth = 2
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = vehicleRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST
        add(element, gridBagConstraints)
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            val enabledVehicles = starWarsState.vehiclesEnabled
            removeAll()
            selectedVehiclesCount.set(0)
            initFactionPanel()
            vehiclesCheckboxes.forEach {
                it.value.isSelected = enabledVehicles[it.key]!!
            }
        }
    }
}
