package com.christopherosthues.starwarsprogressbar.models

internal object FactionCreationHelper {
    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdAndNoVehicles(): List<StarWarsFaction> {
        return listOf(StarWarsFaction("", listOf()))
    }

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndNoVehicles(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction("", listOf()),
            StarWarsFaction("", listOf()),
            StarWarsFaction("", listOf())
        )
    }

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction("", listOf()),
            StarWarsFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
            StarWarsFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f)))
        )
    }

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIds(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
            StarWarsFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f))),
            StarWarsFaction("", listOf(StarWarsVehicle("3", "red", 7, 8, 9f)))
        )
    }

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingVehicles(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction(
                "1",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f)
                )
            ),
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f)
                )
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f)
                )
            )
        )
    }

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f)
                )
            ),
            StarWarsFaction(
                "2",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f)
                )
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f)
                )
            ),
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("7", "green", 7, 8, 9f),
                    StarWarsVehicle("8", "yellow", 10, 11, 12f)
                )
            )
        )
    }

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f)
                )
            ),
            StarWarsFaction(
                "2",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f)
                )
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f)
                )
            ),
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("7", "green", 7, 8, 9f),
                    StarWarsVehicle("8", "yellow", 10, 11, 12f)
                )
            ),
            StarWarsFaction("5", listOf())
        )
    }

    @JvmStatic
    fun createStarWarsFactions(): List<StarWarsFaction> {
        return listOf(
            StarWarsFaction(
                "1",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f)
                )
            ),
            StarWarsFaction(
                "2",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f)
                )
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f)
                )
            )
        )
    }
}
