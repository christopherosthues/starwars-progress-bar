package com.christopherosthues.starwarsprogressbar.configuration

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
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
    }

    companion object {
        val instance: StarWarsPersistentStateComponent?
            get() = service<StarWarsPersistentStateComponent>()
    }
}
