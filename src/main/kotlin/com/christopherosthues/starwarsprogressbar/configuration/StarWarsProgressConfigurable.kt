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
                    starWarsState.showIcon != comp.starWarsState.showIcon ||
                    starWarsState.showNames != comp.starWarsState.showNames ||
                    starWarsState.showToolTips != comp.starWarsState.showToolTips ||
                    starWarsState.showFactionCrests != comp.starWarsState.showFactionCrests ||
                    starWarsState.sameVelocity != comp.starWarsState.sameVelocity ||
                    starWarsState.enableNew != comp.starWarsState.enableNew ||
                    starWarsState.solidProgressBarColor != comp.starWarsState.solidProgressBarColor ||
                    starWarsState.drawSilhouettes != comp.starWarsState.drawSilhouettes ||
                    starWarsState.selector != comp.starWarsState.selector ||
                    starWarsState.language != comp.starWarsState.language ||
                    isVehiclePassesModified(starWarsState, comp)
                )
    }

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
            // TODO: add tests for lightsabers
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
            starWarsState.selector = component.starWarsState.selector
            starWarsState.language = component.starWarsState.language
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
        return stateComponent?.state
    }
}
