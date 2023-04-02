package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object RandomVehicleSelector : IVehicleSelector {
    override fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        val vehicles = FactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.vehicleId, defaultEnabled)
        }

        var vehicle = FactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            vehicle = vehicles[randomInt(vehicles.size)]
        }

        return vehicle
    }
}
