package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.util.randomInt

internal class RollingRandomSelector : IStarWarsSelector {
    private val displayedEntities = mutableSetOf<String>()
    private var lastEnabledEntityIds: Set<String>? = null
    private val lock = Any()

    override fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): StarWarsEntity {
        synchronized(lock) {
            val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
                enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled)
            }
            val lightsabers = StarWarsFactionHolder.defaultLightsabers.filter { lightsaber ->
                enabledLightsabers.getOrDefault(lightsaber.entityId, defaultEnabled)
            }
            val enabledEntities = vehicles + lightsabers

            resetIfSettingsChanged(enabledEntities)

            val entities = enabledEntities.filterNot { displayedEntities.contains(it.entityId) }

            if (entities.isNotEmpty()) {
                val entity = entities[randomInt(entities.size)]

                displayedEntities.add(entity.entityId)

                if (allEntitiesDisplayed(enabledEntities)) {
                    displayedEntities.clear()
                }

                return entity
            }
        }

        return StarWarsFactionHolder.missingVehicle
    }

    private fun resetIfSettingsChanged(enabledEntities: List<StarWarsEntity>) {
        val enabledEntityIds = enabledEntities.mapTo(mutableSetOf()) { it.entityId }
        if (lastEnabledEntityIds != enabledEntityIds) {
            lastEnabledEntityIds = enabledEntityIds
            displayedEntities.clear()
        }
    }

    private fun allEntitiesDisplayed(enabledEntities: List<StarWarsEntity>): Boolean =
        displayedEntities.containsAll(enabledEntities.map { it.entityId })
}
