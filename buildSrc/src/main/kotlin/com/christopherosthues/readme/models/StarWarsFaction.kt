package com.christopherosthues.readme.models

import com.christopherosthues.readme.ReadmePluginConstants

internal data class StarWarsFaction(val id: String, val vehicles: List<StarWarsVehicle>) {
    val localizationKey: String
        get() = "${ReadmePluginConstants.FACTION}${id}"
}
