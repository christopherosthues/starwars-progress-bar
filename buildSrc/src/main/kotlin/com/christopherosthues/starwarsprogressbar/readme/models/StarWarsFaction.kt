package com.christopherosthues.starwarsprogressbar.readme.models

import com.christopherosthues.starwarsprogressbar.StarWarsPluginConstants

internal data class StarWarsFaction(val id: String, val vehicles: List<StarWarsVehicle>) {
    val localizationKey: String
        get() = "${StarWarsPluginConstants.FACTION}$id"
}
