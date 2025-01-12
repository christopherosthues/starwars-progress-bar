package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.ui.LightsaberColor
import com.intellij.ui.JBColor
import kotlinx.serialization.Serializable

@Serializable
internal class Lightsaber(val id: String,
    val bladeColor: String,
    val velocity: Float) {
    val fileName: String
        get() = "lightsabers/$id"

    val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$id"

    val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan
}
