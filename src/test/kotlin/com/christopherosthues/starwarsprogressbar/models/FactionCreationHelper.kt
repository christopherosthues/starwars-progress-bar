package com.christopherosthues.starwarsprogressbar.models

internal object FactionCreationHelper {
    // TODO: lightsabers
    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdAndNoVehicles(): List<StarWarsFaction<StarWarsVehicle>> =
        listOf(StarWarsFaction("", listOf()))

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndNoVehicles(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
        StarWarsFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f))),
    )

    @JvmStatic
    fun createStarWarsFactionsVehiclesWithEmptyIds(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction("", listOf(StarWarsVehicle("1", "blue", 0, 2, 3f))),
        StarWarsFaction("", listOf(StarWarsVehicle("2", "green", 4, 5, 6f))),
        StarWarsFaction("", listOf(StarWarsVehicle("3", "red", 7, 8, 9f))),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingVehicles(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsFaction(
            "",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles(): List<StarWarsFaction<StarWarsVehicle>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("7", "green", 7, 8, 9f),
                    StarWarsVehicle("8", "yellow", 10, 11, 12f),
                ),
            ),
        )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles(): List<StarWarsFaction<StarWarsVehicle>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("3", "green", 7, 8, 9f),
                    StarWarsVehicle("4", "yellow", 10, 11, 12f),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    StarWarsVehicle("1", "blue", 1, 2, 3f),
                    StarWarsVehicle("2", "brown", 4, 5, 6f),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    StarWarsVehicle("5", "red", 13, 14, 15f),
                    StarWarsVehicle("6", "purple", 16, 17, 18f),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    StarWarsVehicle("7", "green", 7, 8, 9f),
                    StarWarsVehicle("8", "yellow", 10, 11, 12f),
                ),
            ),
            StarWarsFaction("5", listOf()),
        )

    @JvmStatic
    fun createStarWarsFactionsVehicles(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                StarWarsVehicle("1", "blue", 1, 2, 3f),
                StarWarsVehicle("2", "brown", 4, 5, 6f),
            ),
        ),
        StarWarsFaction(
            "2",
            listOf(
                StarWarsVehicle("3", "green", 7, 8, 9f),
                StarWarsVehicle("4", "yellow", 10, 11, 12f),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                StarWarsVehicle("5", "red", 13, 14, 15f),
                StarWarsVehicle("6", "purple", 16, 17, 18f),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdAndNoLightsabers(): List<StarWarsFaction<Lightsaber>> =
        listOf(StarWarsFaction("", listOf()))

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndNoLightsabers(): List<StarWarsFaction<Lightsaber>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndFirstHasNoLightsabers(): List<StarWarsFaction<Lightsaber>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf(Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false))),
        StarWarsFaction("", listOf(Lightsaber("2", "green", 6f, isShoto = true, isDoubleBladed = false))),
    )

    @JvmStatic
    fun createStarWarsFactionsLightsabersWithEmptyIds(): List<StarWarsFaction<Lightsaber>> = listOf(
        StarWarsFaction("", listOf(Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false))),
        StarWarsFaction("", listOf(Lightsaber("2", "green", 6f, isShoto = true, isDoubleBladed = false))),
        StarWarsFaction("", listOf(Lightsaber("3", "red", 9f, isShoto = false, isDoubleBladed = true))),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingLightsabers(): List<StarWarsFaction<Lightsaber>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false),
                Lightsaber("2", "brown", 6f, isShoto = true, isDoubleBladed = false),
            ),
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsaber("3", "green", 9f, isShoto = false, isDoubleBladed = true),
                Lightsaber("4", "yellow", 12f, isShoto = false, isDoubleBladed = false),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsaber("5", "red", 15f, isShoto = true, isDoubleBladed = false),
                Lightsaber("6", "purple", 18f, isShoto = false, isDoubleBladed = true),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers(): List<StarWarsFaction<Lightsaber>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsaber("3", "green", 9f, isShoto = false, isDoubleBladed = true),
                    Lightsaber("4", "yellow", 12f, isShoto = false, isDoubleBladed = false),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false),
                    Lightsaber("2", "brown", 6f, isShoto = true, isDoubleBladed = false),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsaber("5", "red", 15f, isShoto = true, isDoubleBladed = false),
                    Lightsaber("6", "purple", 18f, isShoto = false, isDoubleBladed = true),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsaber("7", "green", 9f, isShoto = false, isDoubleBladed = true),
                    Lightsaber("8", "yellow", 12f, isShoto = false, isDoubleBladed = false),
                ),
            ),
        )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoLightsabers(): List<StarWarsFaction<Lightsaber>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsaber("3", "green", 9f, isShoto = false, isDoubleBladed = true),
                    Lightsaber("4", "yellow", 12f, isShoto = false, isDoubleBladed = false),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false),
                    Lightsaber("2", "brown", 6f, isShoto = true, isDoubleBladed = false),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsaber("5", "red", 15f, isShoto = true, isDoubleBladed = false),
                    Lightsaber("6", "purple", 18f, isShoto = false, isDoubleBladed = true),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsaber("7", "green", 9f, isShoto = false, isDoubleBladed = true),
                    Lightsaber("8", "yellow", 12f, isShoto = false, isDoubleBladed = false),
                ),
            ),
            StarWarsFaction("5", listOf()),
        )

    @JvmStatic
    fun createStarWarsFactionsLightsabers(): List<StarWarsFaction<Lightsaber>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsaber("1", "blue", 3f, isShoto = false, isDoubleBladed = false),
                Lightsaber("2", "brown", 6f, isShoto = true, isDoubleBladed = false),
            ),
        ),
        StarWarsFaction(
            "2",
            listOf(
                Lightsaber("3", "green", 9f, isShoto = false, isDoubleBladed = true),
                Lightsaber("4", "yellow", 12f, isShoto = false, isDoubleBladed = false),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsaber("5", "red", 15f, isShoto = true, isDoubleBladed = false),
                Lightsaber("6", "purple", 18f, isShoto = false, isDoubleBladed = true),
            ),
        ),
    )
}
