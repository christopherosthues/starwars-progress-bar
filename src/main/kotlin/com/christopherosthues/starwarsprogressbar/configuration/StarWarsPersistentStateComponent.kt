package com.christopherosthues.starwarsprogressbar.configuration

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.text.SemVer
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(
    name = "com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent",
    storages = [Storage("StarWarsProgress.xml")],
)
internal class StarWarsPersistentStateComponent : PersistentStateComponent<StarWarsState> {
    private val log = com.intellij.openapi.diagnostic.Logger.getInstance(StarWarsPersistentStateComponent::class.java)
    private val state = StarWarsState()

    override fun getState(): StarWarsState? = state

    override fun loadState(state: StarWarsState) {
        log.warn("Loading StarWarsState")
        XmlSerializerUtil.copyBean(state, this.state)
        val version = SemVer.parseFromText(this.state.version)
        if (version != null) {
            if (version < SemVer("2.0.0", 2, 0, 0)) {
                log.warn("Migrating StarWarsState from version ${this.state.version}")
                this.state.showIcon = this.state.showVehicle
                this.state.showNames = this.state.showVehicleNames
                this.state.sameVelocity = this.state.sameVehicleVelocity
                this.state.enableNew = this.state.enableNewVehicles
                this.state.changeAfterPass = this.state.changeVehicleAfterPass
                this.state.numberOfPassesUntilChange = this.state.numberOfPassesUntilVehicleChange
            } else if (version < SemVer("3.0.0", 3, 0, 0)) {
                log.warn("Migrating StarWarsState from version ${this.state.version}")
                this.state.determinateOrderSelectorOrdinal = this.state.vehicleSelectorOrdinal
                this.state.indeterminateOrderSelectorOrdinal = this.state.vehicleSelectorOrdinal
                this.state.selectorOrdinal = this.state.vehicleSelectorOrdinal
            }
        }
        log.warn("StarWarsState loaded: $this.state")
    }

    companion object {
        val instance: StarWarsPersistentStateComponent
            get() = service<StarWarsPersistentStateComponent>()
    }
}
