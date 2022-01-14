package com.christopherosthues.starwarsprogressbar.models

internal object FactionHolder {
    var factions: List<StarWarsFaction> = listOf()

    val defaultFactions: List<StarWarsFaction>
        get() = factions.filter { it.id.isNotEmpty() }

    val missingVehicle: StarWarsVehicle
        get() = factions.first { it.id.isEmpty() }.vehicles.first()

    val defaultVehicles: List<StarWarsVehicle>
        get() = factions.filter { it.id.isNotEmpty() }.fold(mutableListOf()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.vehicles)
            acc
        }
}
