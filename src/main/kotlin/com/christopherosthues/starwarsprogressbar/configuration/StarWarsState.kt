package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW_VEHICLES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import java.util.stream.Collectors

internal class StarWarsState {
    @JvmField
    var vehiclesEnabled: Map<String, Boolean> =
        FactionHolder.defaultVehicles.stream().collect(Collectors.toMap(StarWarsVehicle::id) { true })

    var showVehicleNames: Boolean = DEFAULT_SHOW_VEHICLE_NAMES

    var showToolTips: Boolean = DEFAULT_SHOW_TOOLTIPS

    var showFactionCrests: Boolean = DEFAULT_SHOW_FACTION_CRESTS

    var sameVehicleVelocity: Boolean = DEFAULT_SAME_VEHICLE_VELOCITY

    var enableNewVehicles: Boolean = DEFAULT_ENABLE_NEW_VEHICLES

    var version: String = ""
}
