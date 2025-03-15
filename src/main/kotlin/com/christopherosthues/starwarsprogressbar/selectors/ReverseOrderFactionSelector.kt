package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import java.util.concurrent.atomic.AtomicInteger

internal object ReverseOrderFactionSelector : IStarWarsSelector {
    private val index = AtomicInteger()

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
        val entities =
            (vehicles + lightsabers).sortedWith(
                compareByDescending<StarWarsEntity> {
                    when (it) {
                        is StarWarsVehicle -> StarWarsBundle.message("${BundleConstants.VEHICLES_FACTION}${it.factionId}")
                        else -> StarWarsBundle.message("${BundleConstants.LIGHTSABERS_FACTION}${it.factionId}")
                    }
                }.thenByDescending {
                    StarWarsBundle.message(it.localizationKey).lowercase()
                },
            )

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
}
