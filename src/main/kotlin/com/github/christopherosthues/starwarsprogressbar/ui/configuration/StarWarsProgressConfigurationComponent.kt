package com.github.christopherosthues.starwarsprogressbar.ui.configuration

import com.github.christopherosthues.starwarsprogressbar.BundleConstants
import com.github.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.github.christopherosthues.starwarsprogressbar.ui.*
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.roots.ScalableIconComponent
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.util.AtomicInteger
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.util.stream.Collectors
import javax.swing.*

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel : JPanel

    private val selectAllButton = JButton(StarWarsBundle.message(BundleConstants.SELECT_ALL))
    private val deselectAllButton = JButton(StarWarsBundle.message(BundleConstants.DESELECT_ALL))

    private val vehicleCheckboxes : MutableMap<String, JCheckBox> = HashMap()

    private val selectedVehiclesCount : AtomicInteger = AtomicInteger(0)

    private var vehicleRowCount : Int = 0
    private var factionRowCount : Int = 0
    private var factionCount : Int = 0

    val panel: JPanel
        get() = mainPanel

    val enabledVehicles: Map<String, Boolean>
        get() = vehicleCheckboxes.entries.stream().collect(Collectors.toMap({entry -> entry.key}, { entry -> entry.value.isSelected }))

    fun updateUI(starWarsState: StarWarsState?) {
        starWarsState?.vehiclesEnabled?.forEach { (vehicleName: String, isEnabled: Boolean) ->
            vehicleCheckboxes.computeIfPresent(vehicleName) { _, checkbox: JCheckBox ->
                checkbox.isSelected = isEnabled
                checkbox
            }
        }
    }

    init {
        createUI()
    }

    private fun createUI() {
        mainPanel = JPanel(BorderLayout())
        val formBuilder = FormBuilder.createFormBuilder()

        createPreviewSection(formBuilder)

        createSelectionButtonsSection(formBuilder)

        createVehicleSection(formBuilder)

        updateSelectionButtons()

        mainPanel.add(formBuilder.panel, BorderLayout.NORTH)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        formBuilder.addLabeledComponent(StarWarsBundle.message(BundleConstants.PREVIEW), createPreviewPanel(), true)
        formBuilder.addSeparator()
    }

    private fun createPreviewPanel() : JComponent {
        val panel = JPanel()
        panel.layout = GridLayout(1, 2, 10, 0)

        val determinateProgressBar = JProgressBar(0, 2)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 1
        determinateProgressBar.setUI(StarWarsProgressBarUI(VehiclePicker.get()))

        val indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true
        indeterminateProgressBar.setUI(StarWarsProgressBarUI(VehiclePicker.get()))

        panel.add(LabeledComponent.create(determinateProgressBar, StarWarsBundle.message(BundleConstants.DETERMINATE), BorderLayout.NORTH))
        panel.add(LabeledComponent.create(indeterminateProgressBar, StarWarsBundle.message(BundleConstants.INDETERMINATE), BorderLayout.NORTH))

        return panel
    }

    private fun createSelectionButtonsSection(formBuilder: FormBuilder) {
        val selectionButtonPanel = JPanel()
        selectAllButton.addActionListener {
            if (it.id == ActionEvent.ACTION_PERFORMED) {
                vehicleCheckboxes.values.forEach { c: JCheckBox -> c.isSelected = true }
            }
        }
        deselectAllButton.addActionListener {
            if (it.id == ActionEvent.ACTION_PERFORMED) {
                vehicleCheckboxes.values.forEach { c : JCheckBox -> c.isSelected = false }
            }
        }

        selectionButtonPanel.layout = GridLayout(1, 2, 10, 0)
        selectionButtonPanel.add(selectAllButton)
        selectionButtonPanel.add(deselectAllButton)

        formBuilder.addComponent(selectionButtonPanel)
    }

    private fun createVehicleSection(formBuilder: FormBuilder) {
        val vehiclePanel = JPanel(GridBagLayout())
        factionCount = 0
        factionRowCount = 0

        Faction.DEFAULT_FACTIONS.forEach { faction ->
            vehicleRowCount = 0

            val vehiclesAvailable = StarWarsVehicle.DEFAULT_VEHICLES.any { it.faction == faction }
            if (vehiclesAvailable) {
                val factionPanel = JPanel(GridBagLayout())
                StarWarsVehicle.DEFAULT_VEHICLES.stream().filter { vehicle ->
                    vehicle.faction == faction
                }.sorted(Comparator.comparing(StarWarsVehicle::vehicleName)).forEach { vehicle ->
                    addVehicleCheckBox(vehicle, factionPanel)
                }

                addFactionPanel(factionPanel, faction, vehiclePanel)
            }
        }

        formBuilder.addComponent(vehiclePanel)
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

        addLabeledComponent(factionPanel, ScalableIconComponent(StarWarsResourceLoader.getIcon(vehicle.fileName)), checkBox)
        vehicleCheckboxes[vehicle.fileName] = checkBox
        selectedVehiclesCount.incrementAndGet()
    }

    private fun addFactionPanel(factionPanel: JPanel, faction: Faction, vehiclePanel: JPanel) {
        factionPanel.border = BorderFactory.createTitledBorder(faction.factionName)

        val isFactionCountEven = factionCount++ % 2 == 0
        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridx = if (isFactionCountEven) 0 else 1
        gridBagConstraints.gridy = factionRowCount
        gridBagConstraints.weightx = 0.5
        gridBagConstraints.weighty = 0.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        gridBagConstraints.insets = JBUI.insets(0, 5, 0, 5)

        if (!isFactionCountEven) {
            factionRowCount++
        }
        vehiclePanel.add(factionPanel, gridBagConstraints)
    }

    private fun updateSelectionButtons() {
        val selected = selectedVehiclesCount.get()
        val numberOfVehicles = StarWarsVehicle.DEFAULT_VEHICLES.size
        selectAllButton.isEnabled = numberOfVehicles > 0 && selected < numberOfVehicles
        deselectAllButton.isEnabled = numberOfVehicles > 0 && selected > 0
    }

    private fun addLabeledComponent(panel: JPanel, label: JComponent?, component: JComponent) {
        val gridBagConstraints = GridBagConstraints()
        if (label == null) {
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = vehicleRowCount
            gridBagConstraints.weightx = 0.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.NONE
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(10, 0, UIUtil.DEFAULT_VGAP, 0)
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
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(10, 0, 0, 0)

            panel.add(component, gridBagConstraints)
        }
    }
}
