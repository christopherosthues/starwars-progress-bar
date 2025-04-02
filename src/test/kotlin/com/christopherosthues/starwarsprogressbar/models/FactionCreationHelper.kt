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
    fun createStarWarsFactionsWithEmptyIdAndNoLightsabers(): List<StarWarsFaction<Lightsabers>> =
        listOf(StarWarsFaction("", listOf()))

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndNoLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
        StarWarsFaction("", listOf()),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdsAndFirstHasNoLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction("", listOf()),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "1",
                    3f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                )
            )
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "2",
                    6f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "green", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                )
            )
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsLightsabersWithEmptyIds(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "1",
                    3f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                )
            )
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "2",
                    6f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "green", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                )
            )
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "3",
                    9f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "red", isShoto = false, isDoubleBladed = true, yShift = 3, bladeSize = 8, xBlade = 0, yBlade = 0))
                )
            )
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsabers(
                    "1",
                    3f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "2",
                    6f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "brown", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers(
                    "3",
                    9f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 3, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "4",
                    12f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 4, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsabers(
                    "5",
                    15f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "red", isShoto = true, isDoubleBladed = false, yShift = 5, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "6",
                    18f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "purple", isShoto = false, isDoubleBladed = true, yShift = 6, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers(): List<StarWarsFaction<Lightsabers>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers(
                        "3",
                        9f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "4",
                        12f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 4, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsabers(
                        "1",
                        3f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "2",
                        6f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "brown", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsabers(
                        "5",
                        15f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "red", isShoto = true, isDoubleBladed = false, yShift = 5, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "6",
                        18f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "purple", isShoto = false, isDoubleBladed = true, yShift = 6, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers(
                        "7",
                        9f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 7, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "8",
                        12f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 8, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
        )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoLightsabers(): List<StarWarsFaction<Lightsabers>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers(
                        "3",
                        9f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 3, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "4",
                        12f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 4, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsabers(
                        "1",
                        3f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "2",
                        6f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "brown", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsabers(
                        "5",
                        15f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "red", isShoto = true, isDoubleBladed = false, yShift = 5, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "6",
                        18f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "purple", isShoto = false, isDoubleBladed = true, yShift = 6, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers(
                        "7",
                        9f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 7, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                    Lightsabers(
                        "8",
                        12f,
                        isJarKai = false,
                        listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 8, bladeSize = 8, xBlade = 0, yBlade = 0))
                    ),
                ),
            ),
            StarWarsFaction("5", listOf()),
        )

    @JvmStatic
    fun createStarWarsFactionsLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsabers(
                    "1",
                    3f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "blue", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "2",
                    6f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "brown", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
        StarWarsFaction(
            "2",
            listOf(
                Lightsabers(
                    "3",
                    9f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "green", isShoto = false, isDoubleBladed = true, yShift = 3, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "4",
                    12f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "yellow", isShoto = false, isDoubleBladed = false, yShift = 4, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsabers(
                    "5",
                    15f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "red", isShoto = true, isDoubleBladed = false, yShift = 5, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
                Lightsabers(
                    "6",
                    18f,
                    isJarKai = false,
                    listOf(Lightsaber(1, "purple", isShoto = false, isDoubleBladed = true, yShift = 6, bladeSize = 8, xBlade = 0, yBlade = 0))
                ),
            ),
        ),
    )
}
