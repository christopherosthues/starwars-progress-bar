package com.christopherosthues.starwarsprogressbar.models

import kotlinx.serialization.Serializable

@Serializable
internal data class StarWarsFactions(val lightsabers: List<Lightsaber>, val factions: List<StarWarsFaction>)
