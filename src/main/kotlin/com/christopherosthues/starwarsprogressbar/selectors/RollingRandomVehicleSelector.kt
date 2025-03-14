package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object RollingRandomVehicleSelector : IVehicleSelector {
    private val displayedVehicles = mutableSetOf<String>()
    private val lock = Any()

    override fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        var vehicle = StarWarsFactionHolder.missingVehicle
        synchronized(lock) {
            val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
                enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled) &&
                    !displayedVehicles.contains(vehicle.entityId)
            }

            if (vehicles.isNotEmpty()) {
                vehicle = vehicles[randomInt(vehicles.size)]
            }

            displayedVehicles.add(vehicle.entityId)

            if (allVehiclesDisplayed(enabledVehicles, defaultEnabled)) {
                displayedVehicles.clear()
            }
        }

        return vehicle
    }

    private fun allVehiclesDisplayed(
        enabledVehicles: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): Boolean = displayedVehicles.containsAll(
        StarWarsFactionHolder.defaultVehicles.filter { v ->
            enabledVehicles.getOrDefault(v.entityId, defaultEnabled)
        }.map { v -> v.entityId },
    )
}
