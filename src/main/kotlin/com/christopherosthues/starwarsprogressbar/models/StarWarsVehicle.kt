package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.IonEngineColor
import com.intellij.ui.JBColor

internal data class StarWarsVehicle(
    val id: String,
    val ionEngine: String,
    val xShift: Int,
    val yShift: Int,
    val velocity: Float
) {
    var factionId: String = ""

    val vehicleId: String
        get() = if (factionId.isEmpty()) id else "$factionId.$id"

    val fileName: String
        get() = if (factionId.isEmpty()) id else "$factionId/$id"

    val localizationKey: String
        get() = "${BundleConstants.VEHICLES}$vehicleId"

    val color: JBColor
        get() = IonEngineColor.colors[ionEngine] ?: IonEngineColor.BlueEngine

    companion object {
        val missingVehicle = StarWarsVehicle("missing", "green", -4, -6, 2.5f)
    }
}
