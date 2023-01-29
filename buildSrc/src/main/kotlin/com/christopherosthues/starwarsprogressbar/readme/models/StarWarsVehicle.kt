package com.christopherosthues.starwarsprogressbar.readme.models

import com.christopherosthues.starwarsprogressbar.StarWarsPluginConstants

internal data class StarWarsVehicle(
    val id: String,
    val ionEngine: String,
    val xShift: Int,
    val yShift: Int,
    val velocity: Float
) {
    var faction: StarWarsFaction? = null

    val fileName: String
        get() = "${faction!!.id}/$id"

    val localizationKey: String
        get() = "${StarWarsPluginConstants.VEHICLES}${faction!!.id}.$id"
}
