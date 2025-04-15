package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.ui.LightsaberColor
import com.intellij.ui.JBColor
import kotlinx.serialization.Serializable

@Serializable
internal data class Blade(
    val bladeColor: String,
    val isShoto: Boolean,
    val bladeSize: Int,
    val xBlade: Int,
    val yBlade: Int,
) {
    val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan
}
