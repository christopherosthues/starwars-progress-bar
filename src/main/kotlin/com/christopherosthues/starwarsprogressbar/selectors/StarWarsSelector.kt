package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder

internal object StarWarsSelector {
    fun selectEntity(
        enabledVehicles: Map<String, Boolean>?,
        enabledLightsabers: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
    ): StarWarsEntity {
        var currentEnabledVehicles = enabledVehicles
        var currentEnabledLightsabers = enabledLightsabers
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent?.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        if (currentEnabledLightsabers == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent?.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        val selector = when (selectionType) {
            SelectionType.INORDER_FACTION -> InorderFactionSelector
            SelectionType.INORDER_NAME -> InorderNameSelector
            SelectionType.RANDOM_ALL -> RandomSelector
            SelectionType.RANDOM_NOT_DISPLAYED -> RollingRandomSelector
            SelectionType.REVERSE_ORDER_FACTION -> ReverseOrderFactionSelector
            SelectionType.REVERSE_ORDER_NAME -> ReverseOrderNameSelector
        }

        return selector.selectEntity(currentEnabledVehicles, currentEnabledLightsabers, defaultEnabled)
    }
}
