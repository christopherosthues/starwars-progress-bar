package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicleFaction

internal object FactionCreationHelper {
    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdAndNoVehicles(): List<StarWarsVehicleFaction> = listOf(StarWarsVehicleFaction("", listOf()))

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndNoVehicles(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction("", listOf()),
        StarWarsVehicleFaction("", listOf()),
        StarWarsVehicleFaction("", listOf()),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction("", listOf()),
        StarWarsVehicleFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
        StarWarsVehicleFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f))),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIds(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
        StarWarsVehicleFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f))),
        StarWarsVehicleFaction("", listOf(StarWarsVehicle("3", "red", 7, 8, 9f))),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingVehicles(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction(
            "1",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsVehicleFaction(
            "",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsVehicleFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction(
            "",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsVehicleFaction(
            "2",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsVehicleFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
        StarWarsVehicleFaction(
            "",
            listOf(
                StarWarsVehicle("7", "green", 7, 8, 9f),
                StarWarsVehicle("8", "yellow", 10, 11, 12f),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction(
            "",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsVehicleFaction(
            "2",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsVehicleFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
        StarWarsVehicleFaction(
            "",
            listOf(
                StarWarsVehicle("7", "green", 7, 8, 9f),
                StarWarsVehicle("8", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsVehicleFaction("5", listOf()),
    )

    @JvmStatic
    fun createStarWarsFactions(): List<StarWarsVehicleFaction> = listOf(
        StarWarsVehicleFaction(
            "1",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsVehicleFaction(
            "2",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsVehicleFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
    )
}
