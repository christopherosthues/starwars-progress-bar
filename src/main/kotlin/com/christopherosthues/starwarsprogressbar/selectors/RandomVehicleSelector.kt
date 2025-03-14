package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object RandomVehicleSelector : IVehicleSelector {
    override fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled)
        }

        var vehicle = StarWarsFactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            vehicle = vehicles[randomInt(vehicles.size)]
        }

        return vehicle
    }
}
