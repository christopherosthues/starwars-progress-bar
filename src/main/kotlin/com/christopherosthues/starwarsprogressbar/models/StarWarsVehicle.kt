package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.IonEngineColor
import com.intellij.ui.JBColor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StarWarsVehicle")
internal data class StarWarsVehicle(
    override val id: String,
    val ionEngine: String,
    val xShift: Int,
    val yShift: Int,
    override val velocity: Float,
) : StarWarsEntity {
    override var factionId: String = ""

    override val fileName: String
        get() = "vehicles/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.VEHICLES}$entityId"

    override val color: JBColor
        get() = IonEngineColor.colors[ionEngine] ?: IonEngineColor.BlueEngine

    companion object {
        val missingVehicle = StarWarsVehicle("missing", "green", -4, -6, 2.5f)
    }
}
