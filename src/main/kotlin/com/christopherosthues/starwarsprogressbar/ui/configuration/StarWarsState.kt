package com.christopherosthues.starwarsprogressbar.ui.configuration

import com.christopherosthues.starwarsprogressbar.ui.StarWarsVehicle
import java.util.stream.Collectors

internal class StarWarsState {
    @JvmField
    var vehiclesEnabled: Map<String, Boolean> =
        StarWarsVehicle.DEFAULT_VEHICLES.stream().collect(Collectors.toMap(StarWarsVehicle::fileName) { true })

    var showVehicleNames: Boolean = false

    var showToolTips: Boolean = true

    var sameVehicleVelocity: Boolean = false
}
