package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object VehicleSelector {
    fun selectRandomVehicle(enabledVehicles: Map<String, Boolean>?, defaultEnabled: Boolean): StarWarsVehicle {
        var currentEnabledVehicles = enabledVehicles
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent?.state ?: return FactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        val vehicles = FactionHolder.defaultVehicles.filter { vehicle ->
            currentEnabledVehicles.getOrDefault(vehicle.vehicleId, defaultEnabled)
        }

        var vehicle = FactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            vehicle = vehicles[randomInt(vehicles.size)]
        }

        return vehicle
    }
}
