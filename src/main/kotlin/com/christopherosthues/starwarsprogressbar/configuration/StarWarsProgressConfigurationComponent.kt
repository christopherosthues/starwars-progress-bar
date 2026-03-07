package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.components.LightsaberPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.PreviewPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.SelectionOptionsPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.UiOptionsPanel
import com.christopherosthues.starwarsprogressbar.configuration.components.VehiclesPanel
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.selectors.EntitySelectionType
import com.christopherosthues.starwarsprogressbar.selectors.StarWarsSelector
import com.christopherosthues.starwarsprogressbar.ui.events.StarWarsEntityClickListener
import com.intellij.ui.components.JBTabbedPane
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JPanel

internal class StarWarsProgressConfigurationComponent {
    private lateinit var mainPanel: JPanel

    private lateinit var previewPanel: PreviewPanel

    val starWarsState: StarWarsState = StarWarsState()

    private val uiOptionsPanel = UiOptionsPanel(starWarsState)
    private val selectionOptionsPanel = SelectionOptionsPanel(starWarsState)

    private val vehiclesPanel = VehiclesPanel(starWarsState)

    private val lightsabersPanel = LightsaberPanel(starWarsState)

    private val tabbedPane = JBTabbedPane()

    val panel: JPanel
        get() = mainPanel

    init {
        createUI()
    }

    fun updateUI(starWarsState: StarWarsState?) {
        if (starWarsState != null) {
            this.starWarsState.copy(starWarsState)
            uiOptionsPanel.updateUI(starWarsState)
            selectionOptionsPanel.updateUI(starWarsState)

            vehiclesPanel.updateUI(starWarsState)
            lightsabersPanel.updateUI(starWarsState)
            previewPanel.updateUI(starWarsState)
            repaintProgressBar()
        }
    }

    private fun createUI() {
        mainPanel = JPanel(BorderLayout())
        val formBuilder = FormBuilder.createFormBuilder()

        createPreviewSection(formBuilder)

        createUiOptionsSection(formBuilder)

        tabbedPane.addTab(StarWarsBundle.message(BundleConstants.VEHICLES_TITLE), vehiclesPanel)
        tabbedPane.addTab(StarWarsBundle.message(BundleConstants.LIGHTSABERS_TITLE), lightsabersPanel)
        formBuilder.addComponent(tabbedPane)

        createLightsaberSection()

        createVehicleSection()

        mainPanel.add(formBuilder.panel, BorderLayout.NORTH)

        previewPanel.addPropertyChangeListener(uiOptionsPanel)
        vehiclesPanel.addPropertyChangeListener(uiOptionsPanel)
        lightsabersPanel.addPropertyChangeListener(uiOptionsPanel)
        selectionOptionsPanel.addPropertyChangeListener(uiOptionsPanel)
    }

    private fun createPreviewSection(formBuilder: FormBuilder) {
        previewPanel = PreviewPanel(
            starWarsState,
        )

        formBuilder.addComponent(previewPanel)
    }

    private fun createUiOptionsSection(formBuilder: FormBuilder) {
        uiOptionsPanel.addPropertyChangeListener {
            if (isProgressBarTextEvent(it.propertyName) ||
                isProgressBarDrawEvent(it.propertyName) ||
                isVehicleChangeEvent(it.propertyName) ||
                it.propertyName == DETERMINATE_ENTITY_SELECTOR_EVENT ||
                it.propertyName == INDETERMINATE_ENTITY_SELECTOR_EVENT ||
                it.propertyName == DETERMINATE_ORDER_SELECTOR_EVENT ||
                it.propertyName == INDETERMINATE_ORDER_SELECTOR_EVENT
            ) {
                repaintProgressBar()
            }
        }
        selectionOptionsPanel.addPropertyChangeListener {
            if (isProgressBarTextEvent(it.propertyName) ||
                isProgressBarDrawEvent(it.propertyName) ||
                isVehicleChangeEvent(it.propertyName) ||
                it.propertyName == DETERMINATE_ENTITY_SELECTOR_EVENT ||
                it.propertyName == INDETERMINATE_ENTITY_SELECTOR_EVENT ||
                it.propertyName == DETERMINATE_ORDER_SELECTOR_EVENT ||
                it.propertyName == INDETERMINATE_ORDER_SELECTOR_EVENT
            ) {
                when (it.propertyName) {
                    DETERMINATE_ENTITY_SELECTOR_EVENT -> {
                        val oldSelector = it.oldValue as? EntitySelectionType
                        val newSelector = it.newValue as? EntitySelectionType
                        if (shouldUpdateEntityOnSelectorChange(oldSelector, newSelector)) {
                            val determinateEntity = StarWarsSelector.selectEntity(
                                starWarsState.vehiclesEnabled,
                                starWarsState.lightsabersEnabled,
                                starWarsState.enableNew,
                                starWarsState.determinateOrderSelector!!,
                                starWarsState.indeterminateOrderSelector!!,
                                false,
                                newSelector ?: starWarsState.determinateEntitySelector!!,
                                starWarsState.indeterminateEntitySelector!!,
                            )
                            previewPanel.setDeterminateEntity(determinateEntity)
                        }
                    }

                    INDETERMINATE_ENTITY_SELECTOR_EVENT -> {
                        val oldSelector = it.oldValue as? EntitySelectionType
                        val newSelector = it.newValue as? EntitySelectionType
                        if (shouldUpdateEntityOnSelectorChange(oldSelector, newSelector)) {
                            val indeterminateEntity = StarWarsSelector.selectEntity(
                                starWarsState.vehiclesEnabled,
                                starWarsState.lightsabersEnabled,
                                starWarsState.enableNew,
                                starWarsState.determinateOrderSelector!!,
                                starWarsState.indeterminateOrderSelector!!,
                                true,
                                starWarsState.determinateEntitySelector!!,
                                newSelector ?: starWarsState.indeterminateEntitySelector!!,
                            )
                            previewPanel.setIndeterminateEntity(indeterminateEntity)
                        }
                    }

                    else -> {}
                }

                repaintProgressBar()
            }
        }
        uiOptionsPanel.addPropertyChangeListener(LANGUAGE_EVENT) {
            tabbedPane.setTitleAt(0, StarWarsBundle.message(BundleConstants.VEHICLES_TITLE))
            tabbedPane.setTitleAt(1, StarWarsBundle.message(BundleConstants.LIGHTSABERS_TITLE))
        }

        formBuilder.addComponent(uiOptionsPanel)
        formBuilder.addComponent(selectionOptionsPanel)
    }

    private fun isProgressBarTextEvent(propertyName: String): Boolean {
        return propertyName == SHOW_VEHICLE_NAMES_EVENT || propertyName == SHOW_TOOL_TIPS_EVENT
    }

    private fun isProgressBarDrawEvent(propertyName: String): Boolean {
        return propertyName == SHOW_FACTION_CRESTS_EVENT ||
            propertyName == SAME_VELOCITY_EVENT ||
            propertyName == SOLID_PROGRESS_BAR_COLOR_EVENT ||
            propertyName == SHOW_VEHICLE_EVENT ||
            propertyName == DRAW_SILHOUETTES_EVENT
    }

    private fun isVehicleChangeEvent(propertyName: String): Boolean {
        return propertyName == CHANGE_VEHICLE_AFTER_PASS_EVENT ||
            propertyName == NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE_EVENT
    }

    private fun repaintProgressBar() {
        previewPanel.repaintProgressBar()
    }

    private fun createVehicleSection() {
        vehiclesPanel.addStarWarsEntityListener(object : StarWarsEntityClickListener {
            override fun starWarsEntityClicked(starWarsEntity: StarWarsEntity) {
                previewPanel.selectEntity(starWarsEntity)
            }
        })
    }

    private fun createLightsaberSection() {
        lightsabersPanel.addStarWarsEntityListener(object : StarWarsEntityClickListener {
            override fun starWarsEntityClicked(starWarsEntity: StarWarsEntity) {
                previewPanel.selectEntity(starWarsEntity)
            }
        })
    }

    private fun shouldUpdateEntityOnSelectorChange(oldSel: EntitySelectionType?, newSel: EntitySelectionType?): Boolean {
        // Only update preview entity when the new selector is specific (VEHICLES or LIGHTSABERS)
        // and the selection actually changed to a different specific kind. Changes that set selector to ALL
        // should NOT trigger a new entity selection.
        if (newSel == null) return false
        if (newSel == EntitySelectionType.ALL) return false
        if (oldSel == newSel) return false
        return true
    }
}
