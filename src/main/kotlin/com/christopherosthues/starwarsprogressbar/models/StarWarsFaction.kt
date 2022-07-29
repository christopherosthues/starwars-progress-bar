package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants

internal data class StarWarsFaction(val id: String, val vehicles: List<StarWarsVehicle>) {
    val localizationKey: String
        get() = "${BundleConstants.FACTION}$id"
}
