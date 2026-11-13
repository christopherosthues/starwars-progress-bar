package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade

internal data class BladeGeometry(
    val blade: Blade,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val borderX: Int,
    val borderWidth: Int,
)

internal data class HiltGeometry(val x: Int, val y: Int)

internal data class LightsaberLayout(val blades: List<BladeGeometry>, val hilt: HiltGeometry?)
