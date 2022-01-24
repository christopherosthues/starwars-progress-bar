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
    var faction: StarWarsFaction? = null

    val fileName: String
        get() = "${faction!!.id}/${id}"

    val localizationKey: String
        get() = "${BundleConstants.VEHICLES}${id}"

    val color: JBColor
        get() = IonEngineColor.colors[ionEngine] ?: IonEngineColor.BlueEngine
}
