package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_VEHICLE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW_VEHICLES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_LANGUAGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import java.util.stream.Collectors

internal class StarWarsState {
    var languageOrdinal: Int = DEFAULT_LANGUAGE.ordinal

    internal var language: Language?
        get() = languageOrdinal.let {
            Language.entries.getOrNull(it) ?: DEFAULT_LANGUAGE
        }
        set(value) {
            languageOrdinal = value?.ordinal ?: DEFAULT_LANGUAGE.ordinal
        }

    @JvmField
    var vehiclesEnabled: MutableMap<String, Boolean> =
        StarWarsFactionHolder.defaultVehicles.stream().collect(Collectors.toMap(StarWarsVehicle::entityId) { true })

    @JvmField
    var lightsabersEnabled: MutableMap<String, Boolean> =
        StarWarsFactionHolder.defaultLightsabers.stream().collect(Collectors.toMap(Lightsabers::entityId) { true })

    var showVehicle: Boolean = DEFAULT_SHOW_VEHICLE

    var showVehicleNames: Boolean = DEFAULT_SHOW_VEHICLE_NAMES

    var showToolTips: Boolean = DEFAULT_SHOW_TOOLTIPS

    var showFactionCrests: Boolean = DEFAULT_SHOW_FACTION_CRESTS

    var sameVehicleVelocity: Boolean = DEFAULT_SAME_VEHICLE_VELOCITY

    var enableNewVehicles: Boolean = DEFAULT_ENABLE_NEW_VEHICLES

    var solidProgressBarColor: Boolean = DEFAULT_SOLID_PROGRESS_BAR_COLOR

    var drawSilhouettes: Boolean = DEFAULT_DRAW_SILHOUETTES

    var changeVehicleAfterPass: Boolean = DEFAULT_CHANGE_VEHICLE_AFTER_PASS

    var numberOfPassesUntilVehicleChange: Int = DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE

    var vehicleSelectorOrdinal: Int = DEFAULT_VEHICLE_SELECTOR.ordinal

    internal var vehicleSelector: SelectionType?
        get() = vehicleSelectorOrdinal.let {
            SelectionType.entries.getOrNull(it) ?: DEFAULT_VEHICLE_SELECTOR
        }
        set(value) {
            vehicleSelectorOrdinal = value?.ordinal ?: DEFAULT_VEHICLE_SELECTOR.ordinal
        }

    var version: String = ""

    fun copy(starWarsState: StarWarsState) {
        language = starWarsState.language
        vehiclesEnabled.clear()
        vehiclesEnabled.putAll(starWarsState.vehiclesEnabled)
        lightsabersEnabled.clear()
        lightsabersEnabled.putAll(starWarsState.lightsabersEnabled)
        showVehicle = starWarsState.showVehicle
        showVehicleNames = starWarsState.showVehicleNames
        showToolTips = starWarsState.showToolTips
        showFactionCrests = starWarsState.showFactionCrests
        sameVehicleVelocity = starWarsState.sameVehicleVelocity
        enableNewVehicles = starWarsState.enableNewVehicles
        solidProgressBarColor = starWarsState.solidProgressBarColor
        drawSilhouettes = starWarsState.drawSilhouettes
        changeVehicleAfterPass = starWarsState.changeVehicleAfterPass
        numberOfPassesUntilVehicleChange = starWarsState.numberOfPassesUntilVehicleChange
        vehicleSelector = starWarsState.vehicleSelector
        version = starWarsState.version
    }
}
