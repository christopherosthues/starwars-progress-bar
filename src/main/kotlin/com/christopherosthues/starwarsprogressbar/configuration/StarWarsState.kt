package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import java.util.stream.Collectors

internal class StarWarsState {
    @JvmField
    var vehiclesEnabled: Map<String, Boolean> =
        FactionHolder.defaultVehicles.stream().collect(Collectors.toMap(StarWarsVehicle::id) { true })

    var showVehicleNames: Boolean = false

    var showToolTips: Boolean = true

    var showFactionCrests: Boolean = false

    var sameVehicleVelocity: Boolean = false

    var enableNewVehicles: Boolean = true

    var version: String = ""
}
