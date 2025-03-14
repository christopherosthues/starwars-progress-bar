package com.christopherosthues.starwarsprogressbar.ui.events

import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle

internal interface VehicleClickListener {
    fun vehicleClicked(vehicle: StarWarsVehicle)
}
