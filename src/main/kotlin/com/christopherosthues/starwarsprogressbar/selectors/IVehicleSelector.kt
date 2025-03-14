package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle

internal interface IVehicleSelector {
    fun selectVehicle(enabledVehicles: Map<String, Boolean>, defaultEnabled: Boolean): StarWarsVehicle
}
