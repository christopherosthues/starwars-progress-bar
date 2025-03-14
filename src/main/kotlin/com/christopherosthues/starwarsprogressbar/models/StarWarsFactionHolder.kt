package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader.loadFactions

internal object StarWarsFactionHolder {
    lateinit var vehicleFactions: List<StarWarsFaction<StarWarsVehicle>>
        private set

    lateinit var defaultVehicleFactions: List<StarWarsFaction<StarWarsVehicle>>
        private set

    lateinit var missingVehicle: StarWarsVehicle
        private set

    lateinit var defaultVehicles: List<StarWarsVehicle>
        private set

    lateinit var lightsaberFactions: List<StarWarsFaction<Lightsaber>>
        private set

    lateinit var defaultLightsaberFactions: List<StarWarsFaction<Lightsaber>>
        private set

    lateinit var missingLightsaber: Lightsaber
        private set

    lateinit var defaultLightsabers: List<Lightsaber>
        private set

    init {
        val starWarsFactions = loadFactions()
        updateFactions(starWarsFactions.vehicles)
        updateFactions(starWarsFactions.lightsabers)
    }

    fun updateFactions(factions: List<StarWarsFaction<StarWarsVehicle>>) {
        vehicleFactions = factions
        defaultVehicleFactions = factions.filter { it.id.isNotEmpty() }
        missingVehicle =
            factions.firstOrNull { it.id.isEmpty() }?.data?.firstOrNull() ?: StarWarsVehicle.missingVehicle
        defaultVehicles = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }
    }

    fun updateFactions(factions: List<StarWarsFaction<Lightsaber>>) {
        lightsaberFactions = factions
        defaultLightsaberFactions = factions.filter { it.id.isNotEmpty() }
        missingLightsaber =
            factions.firstOrNull { it.id.isEmpty() }?.data?.firstOrNull() ?: Lightsaber.missingLightsaber
        defaultLightsabers = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }
    }
}
