package com.christopherosthues.starwarsprogressbar.models.lightsabers

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsLightsaberFaction(val id: String, val persons: List<Lightsaber>){
    val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS_FACTION}$id"
}
