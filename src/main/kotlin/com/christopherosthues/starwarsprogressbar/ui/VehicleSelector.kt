package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsPersistentStateComponent
import kotlin.random.Random

internal object VehicleSelector {
    fun selectRandomVehicle(): StarWarsVehicle {
        val persistentStateComponent = StarWarsPersistentStateComponent.instance
        val starWarsState = persistentStateComponent.state ?: return StarWarsVehicle.MISSING

        val enabledVehicles = starWarsState.vehiclesEnabled
        val vehicles = StarWarsVehicle.DEFAULT_VEHICLES.filter { vehicle -> enabledVehicles.getOrDefault(vehicle.fileName, true) }

        if (vehicles.isEmpty()) {
            return StarWarsVehicle.MISSING
        }

        return vehicles[Random.nextInt(vehicles.size)]
    }
}
