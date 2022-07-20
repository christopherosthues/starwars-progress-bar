package com.christopherosthues.starwarsprogressbar.ui.events

import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle

internal interface VehicleClickListener {
    fun vehicleClicked(vehicle: StarWarsVehicle)
}