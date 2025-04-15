package com.christopherosthues.starwarsprogressbar.models

import kotlinx.serialization.Serializable

@Serializable
internal data class Lightsaber(
    val id: Int,
    val isDoubleBladed: Boolean,
    val yShift: Int,
    val blades: List<Blade>,
)
