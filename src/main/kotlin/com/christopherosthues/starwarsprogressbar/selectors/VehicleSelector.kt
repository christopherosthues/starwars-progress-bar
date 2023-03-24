package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle

internal object VehicleSelector {
    fun selectVehicle(
        enabledVehicles: Map<String, Boolean>?,
        defaultEnabled: Boolean,
        selectionType: SelectionType = SelectionType.RANDOM_ALL,
    ): StarWarsVehicle {
        var currentEnabledVehicles = enabledVehicles
        if (currentEnabledVehicles == null) {
            val persistentStateComponent = StarWarsPersistentStateComponent.instance
            val starWarsState = persistentStateComponent?.state ?: return FactionHolder.missingVehicle

            currentEnabledVehicles = starWarsState.vehiclesEnabled
        }

        val selector = when (selectionType) {
            SelectionType.RANDOM_ALL -> RandomVehicleSelector
            SelectionType.RANDOM_NOT_DISPLAYED -> RollingRandomVehicleSelector
            SelectionType.INORDER_VEHICLE_NAME -> InorderVehicleNameVehicleSelector
            SelectionType.REVERSE_ORDER_VEHICLE_NAME -> ReverseOrderVehicleNameVehicleSelector
            SelectionType.INORDER_FACTION -> InorderFactionVehicleSelector
            SelectionType.REVERSE_ORDER_FACTION -> ReverseOrderFactionVehicleSelector
            else -> RandomVehicleSelector
        }

        return selector.selectVehicle(currentEnabledVehicles, defaultEnabled)
    }
}
