package com.christopherosthues.starwarsprogressbar.models.vehicles

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsVehicleFaction(val id: String, val vehicles: List<StarWarsVehicle>) {
    val localizationKey: String
        get() = "${BundleConstants.VEHICLES_FACTION}$id"
}
