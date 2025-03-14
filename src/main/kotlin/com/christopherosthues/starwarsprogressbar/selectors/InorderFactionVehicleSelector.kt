package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import java.util.concurrent.atomic.AtomicInteger

internal object InorderFactionVehicleSelector : IVehicleSelector {
    private val index = AtomicInteger()

    override fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.vehicleId, defaultEnabled)
        }.sortedWith(
            compareBy(
                { StarWarsBundle.message("${BundleConstants.VEHICLES_FACTION}${it.factionId}") },
                { StarWarsBundle.message(it.localizationKey).lowercase() },
            ),
        )

        var vehicle = StarWarsFactionHolder.missingVehicle
        if (vehicles.isNotEmpty()) {
            var vehicleIndex = getIndex(vehicles.size)
            if (vehicleIndex >= vehicles.size) {
                vehicleIndex = 0
            }
            vehicle = vehicles[vehicleIndex]
        }

        return vehicle
    }

    private fun getIndex(vehicleSize: Int): Int {
        var vehicleIndex = index.getAndIncrement()
        if (vehicleIndex >= vehicleSize) {
            index.set(0)
            vehicleIndex = index.getAndIncrement()
        }

        return vehicleIndex
    }
}
