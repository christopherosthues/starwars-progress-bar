package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.ide.plugins.PluginDetailsService
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.text.SemVer
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(
    name = "com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent",
    storages = [Storage("StarWarsProgress.xml")],
)
internal class StarWarsPersistentStateComponent : PersistentStateComponent<StarWarsState> {
    private val state = StarWarsState()

    override fun getState(): StarWarsState? = state

    override fun loadState(state: StarWarsState) {
        XmlSerializerUtil.copyBean(state, this.state)
        val version = SemVer.parseFromText(this.state.version)
        if (version != null) {
            migrationToVersion2(version)
            migrationToVersion3(version)
        }
        val pluginDetails = PluginDetailsService.getInstance().findDetails(PluginId.getId(PluginConstants.PLUGIN_ID))
        val installedVersion = pluginDetails?.version
        if (installedVersion != null && installedVersion != this.state.version) {
            // Record the version that was active before this update so PluginUpdatedActivity can detect it.
            this.state.previousVersion = this.state.version
            this.state.version = installedVersion
        }
    }

    private fun migrationToVersion2(version: SemVer) {
        if (version < SemVer("2.0.0", 2, 0, 0)) {
            this.state.showIcon = this.state.showVehicle
            this.state.showNames = this.state.showVehicleNames
            this.state.sameVelocity = this.state.sameVehicleVelocity
            this.state.enableNew = this.state.enableNewVehicles
            this.state.changeAfterPass = this.state.changeVehicleAfterPass
            this.state.numberOfPassesUntilChange = this.state.numberOfPassesUntilVehicleChange
            this.state.selectorOrdinal = this.state.vehicleSelectorOrdinal
        }
    }

    private fun migrationToVersion3(version: SemVer) {
        if (version < SemVer("3.0.0", 3, 0, 0)) {
            this.state.determinateOrderSelectorOrdinal = this.state.selectorOrdinal
            this.state.indeterminateOrderSelectorOrdinal = this.state.selectorOrdinal
        }
    }

    companion object {
        val instance: StarWarsPersistentStateComponent
            get() = service<StarWarsPersistentStateComponent>()
    }
}
