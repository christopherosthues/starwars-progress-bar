package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENTITY_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder

internal object StarWarsSelector {
    // backward-compatible overload matching previous 4-arg signature used in tests/consumers
    fun selectEntity(
        enabledVehicles: Map<String, Boolean>?,
        enabledLightsabers: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
    ): StarWarsEntity = selectEntity(
        enabledVehicles,
        enabledLightsabers,
        defaultEnabled,
        selectionType,
        false,
        DEFAULT_ENTITY_SELECTOR,
        DEFAULT_ENTITY_SELECTOR,
    )

    fun selectEntity(
        enabledVehicles: Map<String, Boolean>?,
        enabledLightsabers: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateEntitySelectionType: EntitySelectionType,
        indeterminateEntitySelectionType: EntitySelectionType,
    ): StarWarsEntity {
        var currentEnabledVehicles = enabledVehicles
        var currentEnabledLightsabers = enabledLightsabers
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        if (currentEnabledLightsabers == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        if (isIndeterminate) {
            when (indeterminateEntitySelectionType) {
                EntitySelectionType.VEHICLES -> currentEnabledLightsabers = currentEnabledLightsabers.keys.associateWith { false }
                EntitySelectionType.LIGHTSABERS -> currentEnabledVehicles = currentEnabledVehicles.keys.associateWith { false }
                EntitySelectionType.ALL -> {
                    // do nothing, both are enabled
                }
            }
        } else {
            when (determinateEntitySelectionType) {
                EntitySelectionType.VEHICLES -> currentEnabledLightsabers = currentEnabledLightsabers.keys.associateWith { false }
                EntitySelectionType.LIGHTSABERS -> currentEnabledVehicles = currentEnabledVehicles.keys.associateWith { false }
                EntitySelectionType.ALL -> {
                    // do nothing, both are enabled
                }
            }
        }

        // select the proper selector implementation based on the provided selectionType
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
