package com.christopherosthues.starwarsprogressbar.models

import kotlinx.serialization.Serializable

@Serializable
data class StarWarsFaction<E : StarWarsEntity>(val id: String, val data: List<E>)
