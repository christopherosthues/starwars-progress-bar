package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity

internal abstract class AbstractEntitySelector(
    private val inorderFactionSelector: InorderFactionSelector = InorderFactionSelector(),
    private val inorderNameSelector: InorderNameSelector = InorderNameSelector(),
    private val randomSelector: RandomSelector = RandomSelector(),
    private val rollingRandomSelector: RollingRandomSelector = RollingRandomSelector(),
    private val reverseOrderFactionSelector: ReverseOrderFactionSelector = ReverseOrderFactionSelector(),
    private val reverseOrderNameSelector: ReverseOrderNameSelector = ReverseOrderNameSelector(),
) {


    fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
        entitySelectionType: EntitySelectionType,
    ): StarWarsEntity {
        var currentEnabledVehicles = enabledVehicles
        var currentEnabledLightsabers = enabledLightsabers

        when (entitySelectionType) {
            EntitySelectionType.VEHICLES ->
                currentEnabledLightsabers = currentEnabledLightsabers.keys.associateWith { false }

            EntitySelectionType.LIGHTSABERS ->
                currentEnabledVehicles = currentEnabledVehicles.keys.associateWith { false }

            EntitySelectionType.ALL -> {
                // do nothing, both are enabled
            }
        }

        val selector = when (selectionType) {
            SelectionType.INORDER_FACTION -> inorderFactionSelector
            SelectionType.INORDER_NAME -> inorderNameSelector
            SelectionType.RANDOM_ALL -> randomSelector
            SelectionType.RANDOM_NOT_DISPLAYED -> rollingRandomSelector
            SelectionType.REVERSE_ORDER_FACTION -> reverseOrderFactionSelector
            SelectionType.REVERSE_ORDER_NAME -> reverseOrderNameSelector
        }

        return selector.selectEntity(currentEnabledVehicles, currentEnabledLightsabers, defaultEnabled)
    }
}
