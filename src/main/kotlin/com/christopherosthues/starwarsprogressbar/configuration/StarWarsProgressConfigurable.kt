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
        val comp = component
        return comp != null &&
            (
                starWarsState.vehiclesEnabled != comp.enabledVehicles ||
                    starWarsState.showVehicle != comp.showVehicle ||
                    starWarsState.showVehicleNames != comp.showVehicleNames ||
                    starWarsState.showToolTips != comp.showToolTips ||
                    starWarsState.showFactionCrests != comp.showFactionCrests ||
                    starWarsState.sameVehicleVelocity != comp.sameVehicleVelocity ||
                    starWarsState.enableNewVehicles != comp.enableNewVehicles ||
                    starWarsState.solidProgressBarColor != comp.solidProgressBarColor ||
                    starWarsState.drawSilhouettes != comp.drawSilhouettes ||
                    starWarsState.vehicleSelector != comp.vehicleSelector ||
                    starWarsState.language != comp.language ||
                    isVehiclePassesModified(starWarsState, comp)
                )
    }

    private fun isVehiclePassesModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.changeVehicleAfterPass != component.changeVehicleAfterPass ||
        (
            starWarsState.numberOfPassesUntilVehicleChange != component.numberOfPassesUntilVehicleChange &&
                component.changeVehicleAfterPass
            )

    override fun apply() {
        val starWarsState = getStarWarsState()
        val component = this.component
        if (starWarsState == null) {
            throw ConfigurationException("The configuration state cannot be null!")
        } else if (component != null) {
            starWarsState.vehiclesEnabled = component.enabledVehicles
            starWarsState.showVehicle = component.showVehicle
            starWarsState.showVehicleNames = component.showVehicleNames
            starWarsState.showToolTips = component.showToolTips
            starWarsState.showFactionCrests = component.showFactionCrests
            starWarsState.sameVehicleVelocity = component.sameVehicleVelocity
            starWarsState.enableNewVehicles = component.enableNewVehicles
            starWarsState.solidProgressBarColor = component.solidProgressBarColor
            starWarsState.drawSilhouettes = component.drawSilhouettes
            starWarsState.changeVehicleAfterPass = component.changeVehicleAfterPass
            starWarsState.vehicleSelector = component.vehicleSelector
            starWarsState.language = component.language
            if (component.changeVehicleAfterPass) {
                starWarsState.numberOfPassesUntilVehicleChange = component.numberOfPassesUntilVehicleChange
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
        return stateComponent?.state
    }
}
