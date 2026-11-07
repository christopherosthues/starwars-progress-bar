package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

internal class StarWarsProgressConfigurable : SearchableConfigurable {
    private var component: StarWarsProgressConfigurationComponent? = null

    override fun createComponent(): JComponent {
        val comp = createStarWarsProgressConfigurationComponent()
        component = comp
        return comp.panel
    }

    override fun isModified(): Boolean {
        val starWarsState = getStarWarsState()
        return component != null &&
            starWarsState != null &&
            isStateModified(starWarsState)
    }

    private fun isStateModified(starWarsState: StarWarsState): Boolean {
        val comp = component ?: return false
        // TODO: add test for lightsabers
        return isFactionOptionsModified(starWarsState, comp) ||
            isUiOptionsModified(starWarsState, comp) ||
            isSelectionOptionsModified(starWarsState, comp) ||
            isVehiclePassesModified(starWarsState, comp)
    }

    private fun isFactionOptionsModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.vehiclesEnabled != component.starWarsState.vehiclesEnabled ||
        starWarsState.lightsabersEnabled != component.starWarsState.lightsabersEnabled

    private fun isUiOptionsModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.showIcon != component.starWarsState.showIcon ||
        starWarsState.showNames != component.starWarsState.showNames ||
        starWarsState.showToolTips != component.starWarsState.showToolTips ||
        starWarsState.showFactionCrests != component.starWarsState.showFactionCrests ||
        starWarsState.sameVelocity != component.starWarsState.sameVelocity ||
        starWarsState.enableNew != component.starWarsState.enableNew ||
        starWarsState.solidProgressBarColor != component.starWarsState.solidProgressBarColor ||
        starWarsState.drawSilhouettes != component.starWarsState.drawSilhouettes ||
        starWarsState.language != component.starWarsState.language

    private fun isSelectionOptionsModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.determinateOrderSelector != component.starWarsState.determinateOrderSelector ||
        starWarsState.indeterminateOrderSelector != component.starWarsState.indeterminateOrderSelector ||
        starWarsState.determinateEntitySelector != component.starWarsState.determinateEntitySelector ||
        starWarsState.indeterminateEntitySelector != component.starWarsState.indeterminateEntitySelector

    private fun isVehiclePassesModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.changeAfterPass != component.starWarsState.changeAfterPass ||
        (
            starWarsState.numberOfPassesUntilChange != component.starWarsState.numberOfPassesUntilChange &&
                component.starWarsState.changeAfterPass
            )

    override fun apply() {
        val starWarsState = getStarWarsState()
        val component = this.component
        if (starWarsState == null) {
            throw ConfigurationException("The configuration state cannot be null!")
        } else if (component != null) {
            starWarsState.vehiclesEnabled = component.starWarsState.vehiclesEnabled.toMutableMap()
            starWarsState.lightsabersEnabled = component.starWarsState.lightsabersEnabled.toMutableMap()
            starWarsState.showIcon = component.starWarsState.showIcon
            starWarsState.showNames = component.starWarsState.showNames
            starWarsState.showToolTips = component.starWarsState.showToolTips
            starWarsState.showFactionCrests = component.starWarsState.showFactionCrests
            starWarsState.sameVelocity = component.starWarsState.sameVelocity
            starWarsState.enableNew = component.starWarsState.enableNew
            starWarsState.solidProgressBarColor = component.starWarsState.solidProgressBarColor
            starWarsState.drawSilhouettes = component.starWarsState.drawSilhouettes
            starWarsState.changeAfterPass = component.starWarsState.changeAfterPass
            starWarsState.determinateOrderSelector = component.starWarsState.determinateOrderSelector
            starWarsState.indeterminateOrderSelector = component.starWarsState.indeterminateOrderSelector
            starWarsState.language = component.starWarsState.language
            starWarsState.determinateEntitySelector = component.starWarsState.determinateEntitySelector
            starWarsState.indeterminateEntitySelector = component.starWarsState.indeterminateEntitySelector
            if (component.starWarsState.changeAfterPass) {
                starWarsState.numberOfPassesUntilChange = component.starWarsState.numberOfPassesUntilChange
            }
        }
    }

    override fun getDisplayName(): String = StarWarsBundle.message(BundleConstants.PLUGIN_NAME)

    override fun getId(): String = PluginConstants.PLUGIN_SEARCH_ID

    override fun reset() {
        val starWarsState = getStarWarsState()
        component?.updateUI(starWarsState)
    }

    override fun disposeUIResources() {
        component = null
    }

    private fun getStarWarsState(): StarWarsState? {
        val stateComponent = StarWarsPersistentStateComponent.instance
        return stateComponent.state
    }
}
