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
                // TODO: add test for lightsabers
                starWarsState.vehiclesEnabled != comp.starWarsState.vehiclesEnabled ||
                    starWarsState.lightsabersEnabled != comp.starWarsState.lightsabersEnabled ||
                    starWarsState.showVehicle != comp.starWarsState.showVehicle ||
                    starWarsState.showVehicleNames != comp.starWarsState.showVehicleNames ||
                    starWarsState.showToolTips != comp.starWarsState.showToolTips ||
                    starWarsState.showFactionCrests != comp.starWarsState.showFactionCrests ||
                    starWarsState.sameVehicleVelocity != comp.starWarsState.sameVehicleVelocity ||
                    starWarsState.enableNewVehicles != comp.starWarsState.enableNewVehicles ||
                    starWarsState.solidProgressBarColor != comp.starWarsState.solidProgressBarColor ||
                    starWarsState.drawSilhouettes != comp.starWarsState.drawSilhouettes ||
                    starWarsState.vehicleSelector != comp.starWarsState.vehicleSelector ||
                    starWarsState.language != comp.starWarsState.language ||
                    isVehiclePassesModified(starWarsState, comp)
                )
    }

    private fun isVehiclePassesModified(
        starWarsState: StarWarsState,
        component: StarWarsProgressConfigurationComponent,
    ): Boolean = starWarsState.changeVehicleAfterPass != component.starWarsState.changeVehicleAfterPass ||
        (
            starWarsState.numberOfPassesUntilVehicleChange != component.starWarsState.numberOfPassesUntilVehicleChange &&
                component.starWarsState.changeVehicleAfterPass
            )

    override fun apply() {
        val starWarsState = getStarWarsState()
        val component = this.component
        if (starWarsState == null) {
            throw ConfigurationException("The configuration state cannot be null!")
        } else if (component != null) {
            // TODO: add tests for lightsabers
            starWarsState.vehiclesEnabled = component.starWarsState.vehiclesEnabled
            starWarsState.lightsabersEnabled = component.starWarsState.lightsabersEnabled
            starWarsState.showVehicle = component.starWarsState.showVehicle
            starWarsState.showVehicleNames = component.starWarsState.showVehicleNames
            starWarsState.showToolTips = component.starWarsState.showToolTips
            starWarsState.showFactionCrests = component.starWarsState.showFactionCrests
            starWarsState.sameVehicleVelocity = component.starWarsState.sameVehicleVelocity
            starWarsState.enableNewVehicles = component.starWarsState.enableNewVehicles
            starWarsState.solidProgressBarColor = component.starWarsState.solidProgressBarColor
            starWarsState.drawSilhouettes = component.starWarsState.drawSilhouettes
            starWarsState.changeVehicleAfterPass = component.starWarsState.changeVehicleAfterPass
            starWarsState.vehicleSelector = component.starWarsState.vehicleSelector
            starWarsState.language = component.starWarsState.language
            if (component.starWarsState.changeVehicleAfterPass) {
                starWarsState.numberOfPassesUntilVehicleChange = component.starWarsState.numberOfPassesUntilVehicleChange
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
