package com.christopherosthues.starwarsprogressbar.configuration.components

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.LANGUAGE_EVENT
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.configuration.borders.TitledIconBorder
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
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

internal class LightsaberFactionPanel(
    private val starWarsState: StarWarsState,
    private val faction: StarWarsFaction<Lightsabers>
) : JPanel(GridBagLayout()) {
    private val selectLightsabersCheckbox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val lightsabersCheckboxes: MutableMap<String, JCheckBox> = HashMap()
    private var lightsaberRowCount: Int = 0
    private var lightsaberClickListener: StarWarsEntityClickListener? = null

    val selectedLightsabersCount: AtomicInteger = AtomicInteger(0)

    init {
        initFactionPanel()
    }

    private fun initFactionPanel() {
        val localizedName = StarWarsBundle.message("${BundleConstants.LIGHTSABERS_FACTION}${faction.id}")
        border = TitledIconBorder(localizedName, faction.id, "lightsabers")
        val lightsabersAvailable = faction.data.any()
        if (lightsabersAvailable) {
            addFactionCheckBox()

            val localizedNameComparator =
                compareBy<Lightsabers> { StarWarsBundle.message(it.localizationKey).lowercase() }

            faction.data.stream().sorted(localizedNameComparator).forEach { lightsaber ->
                addLightsaberCheckBox(lightsaber)
            }

            updateSelectionButtons()

            addEmptyBottomElement()
        }
    }

    fun updateUI(starWarsState: StarWarsState) {
        starWarsState.lightsabersEnabled.forEach { (lightsaberName: String, isEnabled: Boolean) ->
            lightsabersCheckboxes.computeIfPresent(lightsaberName) { _, checkbox: JCheckBox ->
                checkbox.isSelected = isEnabled
                checkbox
            }
        }
        lightsabersCheckboxes.forEach { (lightsaberName, checkbox) ->
            if (!starWarsState.lightsabersEnabled.containsKey(lightsaberName)) {
                checkbox.isSelected = starWarsState.enableNewVehicles
            }
        }
    }

    fun selectLightsabers(isSelected: Boolean) {
        lightsabersCheckboxes.values.forEach { c: JCheckBox ->
            c.isSelected = isSelected
            faction.data.forEach {
                starWarsState.lightsabersEnabled[it.entityId] = isSelected
            }
        }
    }

    fun addStarWarsEntityListener(listener: StarWarsEntityClickListener) {
        lightsaberClickListener = listener
    }

    private fun addFactionCheckBox() {
        selectLightsabersCheckbox.isThirdStateEnabled = false
        selectLightsabersCheckbox.addItemListener {
            val isSelected = selectLightsabersCheckbox.state == ThreeStateCheckBox.State.SELECTED
            selectLightsabers(isSelected)
        }

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridwidth = 2
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = lightsaberRowCount++
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.anchor = GridBagConstraints.WEST
        gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, 0)

        add(selectLightsabersCheckbox, gridBagConstraints)
    }

    private fun addLightsaberCheckBox(lightsabers: Lightsabers) {
        val localizedName = StarWarsBundle.message(lightsabers.localizationKey)
        val checkBox = JCheckBox(localizedName, true)
        checkBox.addItemListener {
            val oldValue = selectedLightsabersCount.get()
            if (it.stateChange == ItemEvent.SELECTED) {
                selectedLightsabersCount.incrementAndGet()
            } else if (it.stateChange == ItemEvent.DESELECTED) {
                selectedLightsabersCount.decrementAndGet()
            }
            starWarsState.lightsabersEnabled[lightsabers.entityId] = checkBox.isSelected

            propertyChangeListeners.forEach { l ->
                val propertyChangeEvent = PropertyChangeEvent(
                    this,
                    LightsaberFactionPanel::selectedLightsabersCount.name,
                    oldValue,
                    selectedLightsabersCount.get(),
                )
                l.propertyChange(propertyChangeEvent)
            }

            updateSelectionButtons()
        }

        val iconComponent = ScalableIconComponent(StarWarsResourceLoader.getIcon(lightsabers.fileName))
        iconComponent.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                lightsaberClickListener?.starWarsEntityClicked(lightsabers)
            }
        })
        addLabeledComponent(
            iconComponent,
            checkBox,
        )
        lightsabersCheckboxes[lightsabers.entityId] = checkBox
        selectedLightsabersCount.incrementAndGet()
    }

    private fun updateSelectionButtons() {
        val selected = selectedLightsabersCount.get()
        val numberOfLightsabers = faction.data.size

        if (selected == numberOfLightsabers) {
            selectLightsabersCheckbox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectLightsabersCheckbox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectLightsabersCheckbox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfLightsabers) {
                BundleConstants.DESELECT_ALL
            } else {
                BundleConstants.SELECT_ALL
            },
        )
        selectLightsabersCheckbox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected,
            numberOfLightsabers,
            selectionText,
        )
    }

    private fun addLabeledComponent(label: JComponent?, component: JComponent) {
        val gridBagConstraints = GridBagConstraints()
        if (label == null) {
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = lightsaberRowCount + 1
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.insets = JBUI.insets(TOP_PADDING, LEFT_PADDING, 0, 0)

            add(component, gridBagConstraints)

            lightsaberRowCount += 2
        } else {
            gridBagConstraints.gridwidth = 1
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridy = lightsaberRowCount++
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
        gridBagConstraints.gridy = lightsaberRowCount
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST
        add(element, gridBagConstraints)
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            val enabledLightsabers = starWarsState.lightsabersEnabled
            removeAll()
            selectedLightsabersCount.set(0)
            initFactionPanel()
            lightsabersCheckboxes.forEach {
                it.value.isSelected = enabledLightsabers[it.key]!!
            }
        }
    }
}
