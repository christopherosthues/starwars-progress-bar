package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.LightsaberColor
import com.intellij.ui.JBColor
import kotlinx.serialization.Serializable

@Serializable
internal data class Lightsaber(val id: String,
    val bladeColor: String,
    val velocity: Float,
    val isShoto: Boolean,
    val isDoubleBladed: Boolean) {
    var factionId: String = ""

    val lightsaberId: String
        get() = if (factionId.isEmpty()) id else "$factionId.$id"

    val fileName: String
        get() = "lightsabers/$id"

    val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$id"

    val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan
}
