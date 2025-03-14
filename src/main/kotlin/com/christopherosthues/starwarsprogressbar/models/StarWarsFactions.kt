package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.models.lightsabers.StarWarsLightsaberFaction
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicleFaction
import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsFactions(val lightsabers: List<StarWarsLightsaberFaction>, val factions: List<StarWarsVehicleFaction>)
