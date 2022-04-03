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
        val vehicles = FactionHolder.defaultVehicles.filter {
                vehicle -> enabledVehicles.getOrDefault(vehicle.id, true)
        }

        if (vehicles.isEmpty()) {
            return FactionHolder.missingVehicle
        }

        return vehicles[Random.nextInt(vehicles.size)]
    }
}
