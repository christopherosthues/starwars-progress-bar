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
import javax.swing.JPanel

private const val FACTION_PADDING = 5
private const val SELECTION_PANEL_BOTTOM_PADDING = 10

internal class LightsaberPanel(private val starWarsState: StarWarsState) : JPanel(GridBagLayout()) {
    private val selectedLightsabersCheckBox = ThreeStateCheckBox(ThreeStateCheckBox.State.SELECTED)
    private val factionPanels: MutableList<LightsaberFactionPanel> = mutableListOf()

    private var lightsaberRowCount: Int = 0
    private var factionRowCount: Int = 0
    private var factionCount: Int = 0

    private val selectedLightsabersCount: Int
        get() = factionPanels.sumOf { it.selectedLightsabersCount.get() }

    init {
        createSelectionPanel()
        createLightsaberPanel()

        updateSelectionButtons()

        val fillerPanel = JPanel()
        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.BOTH
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 2
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.weighty = 1.0
        gridBagConstraints.anchor = GridBagConstraints.SOUTH
        add(fillerPanel, gridBagConstraints)
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

        selectedLightsabersCheckBox.isThirdStateEnabled = false
        selectedLightsabersCheckBox.addItemListener {
            val isSelected = selectedLightsabersCheckBox.state == ThreeStateCheckBox.State.SELECTED
            selectLightsabers(isSelected)
        }

        selectionPanel.add(selectedLightsabersCheckBox, BorderLayout.WEST)

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 0
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        add(selectionPanel, gridBagConstraints)
    }

    private fun selectLightsabers(isSelected: Boolean) {
        factionPanels.forEach { it.selectLightsabers(isSelected) }
    }

    private fun createLightsaberPanel() {
        val lightsaberPanel = JPanel(GridBagLayout())
        factionCount = 0
        factionRowCount = 0

        StarWarsFactionHolder.defaultLightsabersFactions.forEach { faction ->
            lightsaberRowCount = 0

            val lightsabersAvailable = faction.data.any()
            if (lightsabersAvailable) {
                val factionPanel = LightsaberFactionPanel(starWarsState, faction)
                factionPanel.addPropertyChangeListener(LightsaberFactionPanel::selectedLightsabersCount.name) {
                    updateSelectionButtons()
                }

                addFactionPanel(factionPanel, lightsaberPanel)
            }
        }

        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 2
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.weightx = 1.0
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST
        add(lightsaberPanel, gridBagConstraints)
    }

    private fun addFactionPanel(factionPanel: LightsaberFactionPanel, lightsaberPanel: JPanel) {
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
        lightsaberPanel.add(factionPanel, gridBagConstraints)
    }

    private fun updateSelectionButtons() {
        val selected = selectedLightsabersCount
        val numberOfLightsabers = StarWarsFactionHolder.defaultLightsabers.size

        if (selected == numberOfLightsabers) {
            selectedLightsabersCheckBox.state = ThreeStateCheckBox.State.SELECTED
        } else if (selected > 0) {
            selectedLightsabersCheckBox.state = ThreeStateCheckBox.State.DONT_CARE
        } else {
            selectedLightsabersCheckBox.state = ThreeStateCheckBox.State.NOT_SELECTED
        }

        val selectionText = StarWarsBundle.message(
            if (selected == numberOfLightsabers) {
                BundleConstants.DESELECT_ALL
            } else {
                BundleConstants.SELECT_ALL
            },
        )
        selectedLightsabersCheckBox.text = StarWarsBundle.message(
            BundleConstants.SELECTED,
            selected,
            numberOfLightsabers,
            selectionText,
        )
    }

    fun addPropertyChangeListener(uiOptionsPanel: UiOptionsPanel) {
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            updateSelectionButtons()
        }
        factionPanels.forEach {
            it.addPropertyChangeListener(uiOptionsPanel)
        }
    }
}
