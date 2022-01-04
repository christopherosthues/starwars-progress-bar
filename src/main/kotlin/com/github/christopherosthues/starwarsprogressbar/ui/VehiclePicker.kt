package com.github.christopherosthues.starwarsprogressbar.ui

import com.github.christopherosthues.starwarsprogressbar.ui.configuration.StarWarsPersistentStateComponent
import java.util.stream.Collectors
import kotlin.random.Random

internal object VehiclePicker {
    fun get(): StarWarsVehicle {
        val persistentStateComponent = StarWarsPersistentStateComponent.instance
        val starWarsState = persistentStateComponent.state ?: return StarWarsVehicle.MISSING

        val enabledVehicles = starWarsState.vehiclesEnabled
        val vehicles = StarWarsVehicle.DEFAULT_VEHICLES.stream().filter { vehicle -> enabledVehicles.getOrDefault(vehicle.fileName, true) }.collect(Collectors.toList())

        if (vehicles.isEmpty()) {
            return StarWarsVehicle.MISSING
        }

        return vehicles[Random.nextInt(vehicles.size)]
    }
}
