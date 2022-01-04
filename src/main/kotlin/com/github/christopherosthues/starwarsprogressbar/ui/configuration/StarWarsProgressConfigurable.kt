package com.github.christopherosthues.starwarsprogressbar.ui.configuration

import com.github.christopherosthues.starwarsprogressbar.BundleConstants
import com.github.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import javax.swing.JComponent

internal class StarWarsProgressConfigurable : Configurable {
    private var component : StarWarsProgressConfigurationComponent? = null

    override fun createComponent(): JComponent? {
        component = StarWarsProgressConfigurationComponent()
        return component!!.panel
    }

    override fun isModified(): Boolean {
        val starWarsState = getStarWarsState()
        return component != null && starWarsState != null && starWarsState.vehiclesEnabled != component!!.enabledVehicles
    }

    override fun apply() {
        val starWarsState = getStarWarsState()
        if (starWarsState != null) {
            starWarsState.vehiclesEnabled = component!!.enabledVehicles
        } else {
            throw ConfigurationException("The configuration state cannot be null!")
        }
    }

    override fun getDisplayName(): String {
        return StarWarsBundle.message(BundleConstants.PLUGIN_NAME)
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
