package com.github.christopherosthues.starwarsprogressbar.ui.configuration

import com.github.christopherosthues.starwarsprogressbar.ui.StarWarsVehicle
import java.util.stream.Collectors

internal class StarWarsState {
    @JvmField
    var vehiclesEnabled : Map<String, Boolean> = StarWarsVehicle.DEFAULT_VEHICLES.stream().collect(Collectors.toMap(StarWarsVehicle::fileName) { true })
}
