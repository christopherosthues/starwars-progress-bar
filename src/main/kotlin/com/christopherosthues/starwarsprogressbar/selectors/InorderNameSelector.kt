package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import java.util.concurrent.atomic.AtomicInteger

internal object InorderNameSelector : IStarWarsSelector {
    private val index = AtomicInteger()

    override fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): StarWarsEntity {
        val vehicles = StarWarsFactionHolder.defaultVehicles.filter { vehicle ->
            enabledVehicles.getOrDefault(vehicle.entityId, defaultEnabled)
        }
        val lightsabers = StarWarsFactionHolder.defaultLightsabers.filter { lightsaber ->
            enabledLightsabers.getOrDefault(lightsaber.entityId, defaultEnabled)
        }
        val entities = (vehicles + lightsabers).sortedBy { StarWarsBundle.message(it.localizationKey).lowercase() }

        if (entities.isNotEmpty()) {
            var entityIndex = getIndex(entities.size)
            if (entityIndex >= entities.size) {
                entityIndex = 0
            }
            return entities[entityIndex]
        }

        return StarWarsFactionHolder.missingVehicle
    }

    private fun getIndex(entitiesSize: Int): Int {
        var entityIndex = index.getAndIncrement()
        if (entityIndex >= entitiesSize) {
            index.set(0)
            entityIndex = index.getAndIncrement()
        }

        return entityIndex
    }

    /**
     * For testing only
     */
    internal fun reset() {
        index.set(0)
    }
}
