package com.christopherosthues.starwarsprogressbar.ui.configuration

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

internal class StarWarsProgressConfigurable : SearchableConfigurable {
    private var component: StarWarsProgressConfigurationComponent? = null

    override fun createComponent(): JComponent {
        component = StarWarsProgressConfigurationComponent()
        return component!!.panel
    }

    override fun isModified(): Boolean {
        val starWarsState = getStarWarsState()
        return component != null &&
                starWarsState != null &&
                isStateModified(starWarsState)
    }

    private fun isStateModified(starWarsState: StarWarsState): Boolean {
        return starWarsState.vehiclesEnabled != component!!.enabledVehicles ||
                starWarsState.showVehicleNames != component!!.showVehicleNames ||
                starWarsState.showToolTips != component!!.showToolTips ||
                starWarsState.sameVehicleVelocity != component!!.sameVehicleVelocity
    }

    override fun apply() {
        val starWarsState = getStarWarsState()
        if (starWarsState != null) {
            starWarsState.vehiclesEnabled = component!!.enabledVehicles
            starWarsState.showVehicleNames = component!!.showVehicleNames
            starWarsState.showToolTips = component!!.showToolTips
            starWarsState.sameVehicleVelocity = component!!.sameVehicleVelocity
        } else {
            throw ConfigurationException("The configuration state cannot be null!")
        }
    }

    override fun getDisplayName(): String {
        return StarWarsBundle.message(BundleConstants.PLUGIN_NAME)
    }

    override fun getId(): String {
        return PluginConstants.PluginSearchId
    }

    override fun reset() {
        val starWarsState = getStarWarsState()
        component!!.updateUI(starWarsState)
    }

    override fun disposeUIResources() {
        component = null
    }

    private fun getStarWarsState(): StarWarsState? {
        val stateComponent = StarWarsPersistentStateComponent.instance
        return stateComponent.state
    }
}
