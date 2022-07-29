package com.christopherosthues.starwarsprogressbar.models

internal object FactionHolder {
    lateinit var factions: List<StarWarsFaction>
        private set

    lateinit var defaultFactions: List<StarWarsFaction>
        private set

    lateinit var missingVehicle: StarWarsVehicle
        private set

    lateinit var defaultVehicles: List<StarWarsVehicle>
        private set

    init {
        updateFactions(listOf())
    }

    fun updateFactions(factions: List<StarWarsFaction>) {
        this.factions = factions
        defaultFactions = factions.filter { it.id.isNotEmpty() }
        missingVehicle =
            factions.firstOrNull { it.id.isEmpty() }?.vehicles?.firstOrNull() ?: StarWarsVehicle.missingVehicle
        defaultVehicles = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.vehicles)
            acc
        }
    }
}
