package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle

internal object VehicleSelector {
    fun selectVehicle(
        enabledVehicles: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
    ): StarWarsVehicle {
        var currentEnabledVehicles = enabledVehicles
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent?.state ?: return FactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        val selector = when (selectionType) {
            SelectionType.INORDER_FACTION -> InorderFactionVehicleSelector
            SelectionType.INORDER_VEHICLE_NAME -> InorderVehicleNameVehicleSelector
            SelectionType.RANDOM_ALL -> RandomVehicleSelector
            SelectionType.RANDOM_NOT_DISPLAYED -> RollingRandomVehicleSelector
            SelectionType.REVERSE_ORDER_FACTION -> ReverseOrderFactionVehicleSelector
            SelectionType.REVERSE_ORDER_VEHICLE_NAME -> ReverseOrderVehicleNameVehicleSelector
        }

        return selector.selectVehicle(currentEnabledVehicles, defaultEnabled)
    }
}
