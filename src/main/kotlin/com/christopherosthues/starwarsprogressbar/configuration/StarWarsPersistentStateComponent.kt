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
    private val state = StarWarsState()

    override fun getState(): StarWarsState? = state

    override fun loadState(state: StarWarsState) {
        XmlSerializerUtil.copyBean(state, this.state)
        val version = SemVer.parseFromText(this.state.version)
        if (version != null && version < SemVer("2.0.0", 2, 0, 0)) {
            this.state.showIcon = this.state.showVehicle
            this.state.showNames = this.state.showVehicleNames
            this.state.sameVelocity = this.state.sameVehicleVelocity
            this.state.enableNew = this.state.enableNewVehicles
            this.state.changeAfterPass = this.state.changeVehicleAfterPass
            this.state.numberOfPassesUntilChange = this.state.numberOfPassesUntilVehicleChange
            this.state.selectorOrdinal = this.state.vehicleSelectorOrdinal
        }
    }

    companion object {
        val instance: StarWarsPersistentStateComponent?
            get() = service<StarWarsPersistentStateComponent>()
    }
}
