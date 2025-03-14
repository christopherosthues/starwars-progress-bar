package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.models.lightsabers.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.lightsabers.StarWarsLightsaberFaction
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicleFaction
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader.loadFactions

internal object StarWarsFactionHolder {
    lateinit var vehicleFactions: List<StarWarsVehicleFaction>
        private set

    lateinit var defaultVehicleFactions: List<StarWarsVehicleFaction>
        private set

    lateinit var missingVehicle: StarWarsVehicle
        private set

    lateinit var defaultVehicles: List<StarWarsVehicle>
        private set

    lateinit var lightsaberFactions: List<StarWarsLightsaberFaction>
        private set

    lateinit var defaultLightsaberFactions: List<StarWarsLightsaberFaction>
        private set

    lateinit var missingLightsaber: Lightsaber
        private set

    lateinit var defaultLightsabers: List<Lightsaber>
        private set

    init {
        val starWarsFactions = loadFactions()
        updateFactions(starWarsFactions.factions)
        updateFactions(starWarsFactions.lightsabers)
    }

    fun updateFactions(factions: List<StarWarsVehicleFaction>) {
        vehicleFactions = factions
        defaultVehicleFactions = factions.filter { it.id.isNotEmpty() }
        missingVehicle =
            factions.firstOrNull { it.id.isEmpty() }?.vehicles?.firstOrNull() ?: StarWarsVehicle.missingVehicle
        defaultVehicles = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.vehicles)
            acc
        }
    }

    fun updateFactions(factions: List<StarWarsLightsaberFaction>) {
        lightsaberFactions = factions
        defaultLightsaberFactions = factions.filter { it.id.isNotEmpty() }
        missingLightsaber =
            factions.firstOrNull { it.id.isEmpty() }?.persons?.firstOrNull() ?: Lightsaber.missingLightsaber
        defaultLightsabers = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.persons)
            acc
        }
    }
}
