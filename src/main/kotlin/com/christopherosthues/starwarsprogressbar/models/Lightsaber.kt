package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.ui.LightsaberColor
import com.intellij.ui.JBColor
import kotlinx.serialization.Serializable

@Serializable
internal data class Lightsaber(
    val id: Int,
    val bladeColor: String,
    val isShoto: Boolean,
    val isDoubleBladed: Boolean,
    val xShift: Int,
    val yShift: Int
) {
    val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan
}
