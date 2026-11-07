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
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        return selectEntity(
            enabledLightsabers,
            isIndeterminate,
            currentEnabledVehicles,
            defaultEnabled,
            indeterminateSelectionType,
            indeterminateEntitySelectionType,
            determinateSelectionType,
            determinateEntitySelectionType,
        )
    }

    private fun selectEntity(
        enabledLightsabers: Map<String, Boolean>?,
        isIndeterminate: Boolean,
        currentEnabledVehicles: Map<String, Boolean>,
        defaultEnabled: Boolean,
        indeterminateSelectionType: SelectionType,
        indeterminateEntitySelectionType: EntitySelectionType,
        determinateSelectionType: SelectionType,
        determinateEntitySelectionType: EntitySelectionType,
    ): StarWarsEntity {
        var currentEnabledLightsabers = enabledLightsabers
        if (currentEnabledLightsabers == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        return if (isIndeterminate) {
            IndeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                indeterminateSelectionType,
                indeterminateEntitySelectionType,
            )
        } else {
            DeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                determinateSelectionType,
                determinateEntitySelectionType,
            )
        }
    }
}
