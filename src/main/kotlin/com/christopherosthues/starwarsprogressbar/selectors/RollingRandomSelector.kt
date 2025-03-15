package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal object RollingRandomSelector : IStarWarsSelector {
    private val displayedEntities = mutableSetOf<String>()
    private val lock = Any()

    override fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean
    ): StarWarsEntity {
        synchronized(lock) {
            val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
                enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled) &&
                    !displayedEntities.contains(vehicle.entityId)
            }
            val lightsabers = StarWarsFactionHolder.defaultLightsabers.filter { lightsaber ->
                enabledLightsabers.getOrDefault(lightsaber.entityId, defaultEnabled) &&
                    !displayedEntities.contains(lightsaber.entityId)
            }
            val entities = vehicles + lightsabers

            if (entities.isNotEmpty()) {
                val entity = entities[randomInt(entities.size)]

                displayedEntities.add(entity.entityId)

                if (allEntitiesDisplayed(enabledVehicles, enabledLightsabers, defaultEnabled)) {
                    displayedEntities.clear()
                }
            }

        }

        return StarWarsFactionHolder.missingVehicle
    }

    private fun allEntitiesDisplayed(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): Boolean = displayedEntities.containsAll(
        (StarWarsFactionHolder.defaultVehicles.filter { v ->
            enabledVehicles.getOrDefault(
                v.entityId,
                defaultEnabled
            )
        } + StarWarsFactionHolder.defaultLightsabers.filter { v ->
            enabledLightsabers.getOrDefault(
                v.entityId,
                defaultEnabled
            )
        }).map { v -> v.entityId },
    )
}
