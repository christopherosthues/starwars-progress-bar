package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENTITY_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder

internal object StarWarsSelector {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(StarWarsSelector::class.java)

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
            logger.warn("No vehicles provided. Loading enabled vehicles from persistent state")
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        if (currentEnabledLightsabers == null) {
            logger.warn("No lightsabers provided. Loading enabled lightsabers from persistent state")
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state ?: return StarWarsFactionHolder.missingVehicle

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        if (isIndeterminate) {
            logger.warn("Selecting entities for indeterminate progress bar")
            return IndeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                selectionType,
                indeterminateEntitySelectionType
            )
        } else {
            logger.warn("Selecting entities for determinate progress bar")
            return DeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                selectionType,
                determinateEntitySelectionType
            )
        }
    }
}
