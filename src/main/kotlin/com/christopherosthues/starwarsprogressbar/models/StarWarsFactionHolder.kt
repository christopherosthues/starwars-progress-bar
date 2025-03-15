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
        updateFactions(starWarsFactions)
    }

    fun updateFactions(factions: StarWarsFactions) {
        vehicleFactions = factions.vehicles
        defaultVehicleFactions = factions.vehicles.filter { it.id.isNotEmpty() }
        missingVehicle =
            factions.vehicles.firstOrNull { it.id.isEmpty() }?.data?.firstOrNull() ?: StarWarsVehicle.missingVehicle
        defaultVehicles = factions.vehicles.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }

        lightsaberFactions = factions.lightsabers
        defaultLightsaberFactions = factions.lightsabers.filter { it.id.isNotEmpty() }
        missingLightsaber =
            factions.lightsabers.firstOrNull { it.id.isEmpty() }?.data?.firstOrNull() ?: Lightsaber.missingLightsaber
        defaultLightsabers = factions.lightsabers.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }
    }
}
