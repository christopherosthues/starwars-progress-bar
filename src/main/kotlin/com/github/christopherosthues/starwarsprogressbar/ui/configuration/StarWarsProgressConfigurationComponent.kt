package com.github.christopherosthues.starwarsprogressbar.ui.configuration

import com.github.christopherosthues.starwarsprogressbar.BundleConstants
import com.github.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.github.christopherosthues.starwarsprogressbar.ui.*
import com.github.christopherosthues.starwarsprogressbar.ui.components.JTitledPanel
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.roots.ScalableIconComponent
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.ThreeStateCheckBox
import com.jetbrains.rd.util.AtomicInteger
import java.awt.*
import java.awt.event.ItemEvent
import java.util.stream.Collectors
import javax.swing.*
import javax.swing.border.EmptyBorder

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel: JPanel

    private val showVehicleNameCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_VEHICLE_NAME), false)
    private val showToolTipsCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SHOW_TOOL_TIPS), true)
    private val sameVehicleVelocityCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.SAME_VEHICLE_VELOCITY), false)

    private val selectedVehiclesCheckBox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)

    private val vehicleCheckboxes: MutableMap<String, JCheckBox> = HashMap()

    private val selectedVehiclesCount: AtomicInteger = AtomicInteger(0)

    private var vehicleRowCount: Int = 0
    private var factionRowCount: Int = 0
    private var factionCount: Int = 0

    val panel: JPanel
        get() = mainPanel

    val enabledVehicles: Map<String, Boolean>
        get() = vehicleCheckboxes.entries.stream()
            .collect(Collectors.toMap({ entry -> entry.key }, { entry -> entry.value.isSelected }))

    val showVehicleNames: Boolean
        get() = showVehicleNameCheckBox.isSelected

    val showToolTips: Boolean
        get() = showToolTipsCheckBox.isSelected

    val sameVehicleVelocity: Boolean
        get() = sameVehicleVelocityCheckBox.isSelected

    init {
        createUI()
    }

    fun updateUI(starWarsState: StarWarsState?) {
        if (starWarsState != null) {
            showVehicleNameCheckBox.isSelected = starWarsState.showVehicleNames
            showToolTipsCheckBox.isSelected = starWarsState.showToolTips
            sameVehicleVelocityCheckBox.isSelected = starWarsState.sameVehicleVelocity

            starWarsState.vehiclesEnabled.forEach { (vehicleName: String, isEnabled: Boolean) ->
                vehicleCheckboxes.computeIfPresent(vehicleName) { _, checkbox: JCheckBox ->
                    checkbox.isSelected = isEnabled
                    checkbox
                }
            }
        }
    }

    private fun createUI() {
        mainPanel = JPanel(BorderLayout())
        val formBuilder = FormBuilder.createFormBuilder()

        createPreviewSection(formBuilder)

        createUiOptionsSection(formBuilder)

        createVehicleSection(formBuilder)

        updateSelectionButtons()

        mainPanel.add(formBuilder.panel, BorderLayout.NORTH)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        val previewPanel = JTitledPanel(StarWarsBundle.message(BundleConstants.PREVIEW_TITLE))
        previewPanel.layout = GridLayout(1, 2, 10, 0)

        val determinateProgressBar = JProgressBar(0, 2)
        determinateProgressBar.isIndeterminate = false
        determinateProgressBar.value = 1
        determinateProgressBar.setUI(
            StarWarsProgressBarUI(
                VehiclePicker.get(),
                showVehicleNameCheckBox::isSelected,
                showToolTipsCheckBox::isSelected,
                sameVehicleVelocityCheckBox::isSelected
            )
        )

        val indeterminateProgressBar = JProgressBar()
        indeterminateProgressBar.isIndeterminate = true
        indeterminateProgressBar.setUI(
            StarWarsProgressBarUI(
                VehiclePicker.get(),
                showVehicleNameCheckBox::isSelected,
                showToolTipsCheckBox::isSelected,
                sameVehicleVelocityCheckBox::isSelected
            )
        )

        previewPanel.add(
            LabeledComponent.create(
                determinateProgressBar,
                StarWarsBundle.message(BundleConstants.DETERMINATE),
                BorderLayout.NORTH
            )
        )
        previewPanel.add(
            LabeledComponent.create(
                indeterminateProgressBar,
                StarWarsBundle.message(BundleConstants.INDETERMINATE),
                BorderLayout.NORTH
            )
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        val uiOptionsPanel = JTitledPanel(StarWarsBundle.message(BundleConstants.UI_OPTIONS))
        uiOptionsPanel.layout = GridLayout(2, 2, 5, 5)

        uiOptionsPanel.add(showVehicleNameCheckBox)
        uiOptionsPanel.add(sameVehicleVelocityCheckBox)
        uiOptionsPanel.add(showToolTipsCheckBox)

        formBuilder.addComponent(uiOptionsPanel)
    }

    private fun createVehicleSection(formBuilder: FormBuilder) {
        val vehiclesPanel = JTitledPanel(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE))
        val vehiclesPanelLayout = BoxLayout(vehiclesPanel.contentPanel, BoxLayout.Y_AXIS)
        vehiclesPanel.layout = vehiclesPanelLayout

        val selectionPanel = createSelectionPanel()
        val vehiclePanel = createVehiclePanel()

        vehiclesPanel.add(selectionPanel)
        vehiclesPanel.add(vehiclePanel)

        formBuilder.addComponent(vehiclesPanel)
    }

    private fun createSelectionPanel(): JPanel {
        val selectionPanel = JPanel(BorderLayout())
        selectionPanel.border = EmptyBorder(0, 0, 10, 0)

        selectedVehiclesCheckBox.isThirdStateEnabled = false
        selectedVehiclesCheckBox.addItemListener {
            val isSelected = selectedVehiclesCheckBox.state == ThreeStateCheckBox.State.SELECTED
            selectVehicles(isSelected)
        }

        selectionPanel.add(selectedVehiclesCheckBox, BorderLayout.WEST)

        return selectionPanel
    }

    private fun selectVehicles(isSelected: Boolean) {
        vehicleCheckboxes.values.forEach { c: JCheckBox -> c.isSelected = isSelected }
    }

    private fun createVehiclePanel(): JPanel {
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

        return vehiclePanel
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
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridx = if (isFactionCountEven) 0 else 1
        gridBagConstraints.gridy = factionRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 0.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        gridBagConstraints.insets = JBUI.insets(0, 0, 0, 0)

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
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(10, 0, 0, 0)

            panel.add(component, gridBagConstraints)
        }
    }
}
