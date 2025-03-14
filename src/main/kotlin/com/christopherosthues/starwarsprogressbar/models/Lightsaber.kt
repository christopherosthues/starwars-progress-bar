package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.LightsaberColor
import com.intellij.ui.JBColor
import kotlinx.serialization.Serializable

@Serializable
internal data class Lightsaber(
    override val id: String,
    val bladeColor: String,
    override val velocity: Float,
    val isShoto: Boolean,
    val isDoubleBladed: Boolean) : StarWarsEntity {

    override var factionId: String = ""

    override val fileName: String
        get() = "lightsabers/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$entityId"

    override val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan

    companion object {
        val missingLightsaber = Lightsaber("missing", "green", 2.5f, isShoto = false, isDoubleBladed = false)
    }
}
