package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder

internal object StarWarsSelector {
    fun selectEntity(
        enabledVehicles: Map<String, Boolean>?,
        enabledLightsabers: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        determinateSelectionType: SelectionType,
        indeterminateSelectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateEntitySelectionType: EntitySelectionType,
        indeterminateEntitySelectionType: EntitySelectionType,
    ): StarWarsEntity {
        var currentEnabledVehicles = enabledVehicles
        var currentEnabledLightsabers = enabledLightsabers
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state
            if (starWarsState == null) {
                return StarWarsFactionHolder.missingVehicle
            }

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        if (currentEnabledLightsabers == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state
            if (starWarsState == null) {
                return StarWarsFactionHolder.missingVehicle
            }

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        if (isIndeterminate) {
            return IndeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                indeterminateSelectionType,
                indeterminateEntitySelectionType,
            )
        } else {
            return DeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                determinateSelectionType,
                determinateEntitySelectionType,
            )
        }
    }
}
