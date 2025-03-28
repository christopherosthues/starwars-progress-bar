package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object RandomSelector : IStarWarsSelector {
    override fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean
    ): StarWarsEntity {
        val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled)
        }
        val lightsabers = StarWarsFactionHolder.defaultLightsabers.filter { lightsaber ->
            enabledLightsabers.getOrDefault(lightsaber.entityId, defaultEnabled)
        }
        val entities = vehicles + lightsabers

        if (entities.isNotEmpty()) {
            return entities[randomInt(entities.size)]
        }

        return StarWarsFactionHolder.missingVehicle
    }
}
