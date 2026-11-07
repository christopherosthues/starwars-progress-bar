package com.christopherosthues.starwarsprogressbar.helpers

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.selectors.IStarWarsSelector

internal fun IStarWarsSelector.selectMultipleEntities(
    entities: List<StarWarsEntity>,
    enabledVehicles: Map<String, Boolean>,
    enabledLightsabers: Map<String, Boolean>,
    defaultEnabled: Boolean,
): MutableList<StarWarsEntity> {
    val result = mutableListOf<StarWarsEntity>()
    repeat(entities.indices.count()) {
        result.add(
            this.selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            ),
        )
    }

    return result
}

internal fun IStarWarsSelector.selectMultipleEntitiesTwice(
    entities: List<StarWarsEntity>,
    enabledVehicles: Map<String, Boolean>,
    enabledLightsabers: Map<String, Boolean>,
    defaultEnabled: Boolean,
): MutableList<StarWarsEntity> {
    val result = mutableListOf<StarWarsEntity>()
    repeat((0 until (2 * entities.size)).count()) {
        result.add(
            this.selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            ),
        )
    }

    return result
}
