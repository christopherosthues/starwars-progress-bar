package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import java.util.concurrent.atomic.AtomicInteger

internal object ReverseOrderFactionVehicleSelector : IVehicleSelector {
    // TODO unit test me
    private val index = AtomicInteger()

    override fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle {
        val vehicles = FactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.vehicleId, defaultEnabled)
        }.sortedWith(
            compareByDescending<StarWarsVehicle> { StarWarsBundle.message("${BundleConstants.FACTION}${it.factionId}") }.thenByDescending {
                StarWarsBundle.message(
                    it.localizationKey,
                ).lowercase()
            },
        )

        var vehicle = FactionHolder.missingVehicle
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
