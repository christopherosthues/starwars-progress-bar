package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity

internal abstract class AbstractEntitySelector {
    protected val inorderFactionSelector = InorderFactionSelector()
    protected val inorderNameSelector = InorderNameSelector()
    protected val randomSelector = RandomSelector()
    protected val rollingRandomSelector = RollingRandomSelector()
    protected val reverseOrderFactionSelector = ReverseOrderFactionSelector()
    protected val reverseOrderNameSelector = ReverseOrderNameSelector()
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(this::class.java)

    fun selectEntity(
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
        selectionType: SelectionType,
        entitySelectionType: EntitySelectionType
    ): StarWarsEntity {
        logger.debug("Selecting entity with defaultEnabled=$defaultEnabled and selectionType=$selectionType and entitySelectionType=$entitySelectionType")
        var currentEnabledVehicles = enabledVehicles
        var currentEnabledLightsabers = enabledLightsabers

        when (entitySelectionType) {
            EntitySelectionType.VEHICLES -> currentEnabledLightsabers =
                currentEnabledLightsabers.keys.associateWith { false }

            EntitySelectionType.LIGHTSABERS -> currentEnabledVehicles =
                currentEnabledVehicles.keys.associateWith { false }

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
