package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.FactionHolder.factions
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.StarWarsResourceLoader.loadFactions
import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsPersistentStateComponent
import kotlin.random.Random

internal object VehicleSelector {
    fun selectRandomVehicle(): StarWarsVehicle {
        if (factions.isEmpty()) {
            factions = loadFactions().factions
        }
        val persistentStateComponent = StarWarsPersistentStateComponent.instance
        val starWarsState = persistentStateComponent.state ?: return FactionHolder.missingVehicle

        val enabledVehicles = starWarsState.vehiclesEnabled
        return selectRandomVehicle(enabledVehicles)
    }

    fun selectRandomVehicle(enabledVehicles: Map<String, Boolean>): StarWarsVehicle {
        val vehicles = FactionHolder.defaultVehicles.filter {
                vehicle -> enabledVehicles.getOrDefault(vehicle.id, true)
        }

        var vehicle = FactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            vehicle = vehicles[Random.nextInt(vehicles.size)]
        }

        return vehicle
    }
}
