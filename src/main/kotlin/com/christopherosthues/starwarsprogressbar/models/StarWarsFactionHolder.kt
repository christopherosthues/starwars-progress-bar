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

    lateinit var lightsabersFactions: List<StarWarsFaction<Lightsabers>>
        private set

    lateinit var defaultLightsabersFactions: List<StarWarsFaction<Lightsabers>>
        private set

    lateinit var defaultLightsabers: List<Lightsabers>
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
        defaultVehicles =
            factions.vehicles.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
                acc.addAll(starWarsFaction.data)
                acc
            }

        lightsabersFactions = factions.lightsabers
        defaultLightsabersFactions = factions.lightsabers.filter { it.id.isNotEmpty() }
        defaultLightsabers =
            factions.lightsabers.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
                acc.addAll(starWarsFaction.data)
                acc
            }
    }
}
