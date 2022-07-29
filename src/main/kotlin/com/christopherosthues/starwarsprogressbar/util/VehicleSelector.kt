package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.FactionHolder.factions
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader.loadFactions

internal object VehicleSelector {
    fun selectRandomVehicle(): StarWarsVehicle {
        if (factions.isEmpty()) {
            // TODO: move this to FactionHolder
            FactionHolder.updateFactions(loadFactions().factions)
        }
        val persistentStateComponent = StarWarsPersistentStateComponent.instance
        val starWarsState = persistentStateComponent.state ?: return FactionHolder.missingVehicle

        val enabledVehicles = starWarsState.vehiclesEnabled
        return selectRandomVehicle(enabledVehicles, starWarsState.enableNewVehicles)
    }

    fun selectRandomVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        val vehicles = FactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.id, defaultEnabled)
        }

        var vehicle = FactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            vehicle = vehicles[randomInt(vehicles.size)]
        }

        return vehicle
    }
}
