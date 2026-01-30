package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder

internal object StarWarsSelector {
    private val log = com.intellij.openapi.diagnostic.Logger.getInstance(StarWarsSelector::class.java)

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
            log.debug("No vehicles provided. Loading enabled vehicles from persistent state")
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state
            if (starWarsState == null) {
                log.warn("Could not load persistent state. Returning missing vehicle.")
                return StarWarsFactionHolder.missingVehicle
            }

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        if (currentEnabledLightsabers == null) {
            log.debug("No lightsabers provided. Loading enabled lightsabers from persistent state")
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent.state
            if (starWarsState == null) {
                log.warn("Could not load persistent state. Returning missing vehicle.")
                return StarWarsFactionHolder.missingVehicle
            }

            currentEnabledLightsabers = starWarsState.lightsabersEnabled
        }

        if (isIndeterminate) {
            log.debug("Selecting entities for indeterminate progress bar")
            return IndeterminateSelector.selectEntity(
                currentEnabledVehicles,
                currentEnabledLightsabers,
                defaultEnabled,
                indeterminateSelectionType,
                indeterminateEntitySelectionType,
            )
        } else {
            log.debug("Selecting entities for determinate progress bar")
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
