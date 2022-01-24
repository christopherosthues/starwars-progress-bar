package com.christopherosthues.readme.models

import com.christopherosthues.readme.ReadmePluginConstants

internal data class StarWarsVehicle(
    val id: String,
    val ionEngine: String,
    val xShift: Int,
    val yShift: Int,
    val velocity: Float
) {
    var faction: StarWarsFaction? = null

    val fileName: String
        get() = "${faction!!.id}/${id}"

    val localizationKey: String
        get() = "${ReadmePluginConstants.VEHICLES}${id}"
}
