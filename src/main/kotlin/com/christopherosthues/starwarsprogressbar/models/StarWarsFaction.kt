package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsFaction(val id: String, val vehicles: List<StarWarsVehicle>) {
    val localizationKey: String
        get() = "${BundleConstants.FACTION}$id"
}
