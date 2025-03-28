package com.christopherosthues.starwarsprogressbar.models

import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsFactions(
    val lightsabers: List<StarWarsFaction<Lightsabers>>,
    val vehicles: List<StarWarsFaction<StarWarsVehicle>>
)
