package com.christopherosthues.starwarsprogressbar.models

internal object FactionCreationHelper {
    private val blueVehicle
        get() = StarWarsVehicle("1", "blue", 1, 2, 3f)
    private val brownVehicle
        get() = StarWarsVehicle("2", "brown", 4, 5, 6f)
    private val greenVehicle
        get() = StarWarsVehicle("3", "green", 7, 8, 9f)
    private val yellowVehicle
        get() = StarWarsVehicle("4", "yellow", 10, 11, 12f)
    private val redVehicle
        get() = StarWarsVehicle("5", "red", 13, 14, 15f)
    private val purpleVehicle
        get() = StarWarsVehicle("6", "purple", 16, 17, 18f)
    private val greenVehicle2
        get() = StarWarsVehicle("7", "green", 7, 8, 9f)
    private val yellowVehicle2
        get() = StarWarsVehicle("8", "yellow", 10, 11, 12f)

    private val blueBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 1,
            listOf(Blade("blue", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val greenBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 2,
            listOf(Blade("green", isShoto = true, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val brownBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 2,
            listOf(Blade("brown", isShoto = true, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val yellowBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 4,
            listOf(Blade("yellow", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val yellowBlade2
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 8,
            listOf(Blade("yellow", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val redBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 5,
            listOf(Blade("red", isShoto = true, bladeSize = 8, xBlade = 0, yBlade = 0)),
        )

    private val redDoubleBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = true,
            yShift = 3,
            listOf(
                Blade("red", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                Blade("red", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
            ),
        )

    private val greenDoubleBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = true,
            yShift = 3,
            listOf(
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
            ),
        )

    private val greenDoubleBlade2
        get() = Lightsaber(
            1,
            isDoubleBladed = true,
            yShift = 7,
            listOf(
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
            ),
        )

    private val greenDoubleBlade3
        get() = Lightsaber(
            1,
            isDoubleBladed = true,
            yShift = 2,
            listOf(
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                Blade("green", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
            ),
        )

    private val purpleDoubleBlade
        get() = Lightsaber(
            1,
            isDoubleBladed = true,
            yShift = 6,
            listOf(
                Blade("purple", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                Blade("purple", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
            ),
        )

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
        StarWarsFaction("1", listOf(blueVehicle, brownVehicle)),
        StarWarsFaction("", listOf(greenVehicle, yellowVehicle)),
        StarWarsFaction("3", listOf(redVehicle, purpleVehicle)),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles(): List<StarWarsFaction<StarWarsVehicle>> =
        listOf(
            StarWarsFaction("", listOf(greenVehicle, yellowVehicle)),
            StarWarsFaction("2", listOf(blueVehicle, brownVehicle)),
            StarWarsFaction("3", listOf(redVehicle, purpleVehicle)),
            StarWarsFaction("", listOf(greenVehicle2, yellowVehicle2)),
        )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles(): List<StarWarsFaction<StarWarsVehicle>> =
        listOf(
            StarWarsFaction("", listOf(greenVehicle, yellowVehicle)),
            StarWarsFaction("2", listOf(blueVehicle, brownVehicle)),
            StarWarsFaction("3", listOf(redVehicle, purpleVehicle)),
            StarWarsFaction("", listOf(greenVehicle2, yellowVehicle2)),
            StarWarsFaction("5", listOf()),
        )

    @JvmStatic
    fun createStarWarsFactionsVehicles(): List<StarWarsFaction<StarWarsVehicle>> = listOf(
        StarWarsFaction("1", listOf(blueVehicle, brownVehicle)),
        StarWarsFaction("2", listOf(greenVehicle, yellowVehicle)),
        StarWarsFaction("3", listOf(redVehicle, purpleVehicle)),
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
        StarWarsFaction("", listOf(Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)))),
        StarWarsFaction("", listOf(Lightsabers("2", 6f, isJarKai = false, listOf(greenBlade)))),
    )

    @JvmStatic
    fun createStarWarsFactionsLightsabersWithEmptyIds(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction("", listOf(Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)))),
        StarWarsFaction("", listOf(Lightsabers("2", 6f, isJarKai = false, listOf(greenBlade)))),
        StarWarsFaction("", listOf(Lightsabers("3", 9f, isJarKai = false, listOf(redDoubleBlade)))),
    )

    @JvmStatic
    fun createStarWarsFactionsWithEmptyIdContainingLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)),
                Lightsabers("2", 6f, isJarKai = false, listOf(brownBlade)))
        ),
        StarWarsFaction(
            "",
            listOf(
                Lightsabers("3", 9f, isJarKai = false, listOf(greenDoubleBlade)),
                Lightsabers("4", 12f, isJarKai = false, listOf(yellowBlade)))
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsabers("5", 15f, isJarKai = false, listOf(redBlade)),
                Lightsabers("6", 18f, isJarKai = false, listOf(purpleDoubleBlade)),
            ),
        ),
    )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers(): List<StarWarsFaction<Lightsabers>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers("3", 9f, isJarKai = false, listOf(greenDoubleBlade3)),
                    Lightsabers("4", 12f, isJarKai = false, listOf(yellowBlade)),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)),
                    Lightsabers("2", 6f, isJarKai = false, listOf(brownBlade)),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsabers("5", 15f, isJarKai = false, listOf(redBlade)),
                    Lightsabers("6", 18f, isJarKai = false, listOf(purpleDoubleBlade)),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers("7", 9f, isJarKai = false, listOf(greenDoubleBlade2)),
                    Lightsabers("8", 12f, isJarKai = false, listOf(yellowBlade2)),
                ),
            ),
        )

    @JvmStatic
    fun createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoLightsabers(): List<StarWarsFaction<Lightsabers>> =
        listOf(
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers("3", 9f, isJarKai = false, listOf(greenDoubleBlade)),
                    Lightsabers("4", 12f, isJarKai = false, listOf(yellowBlade)),
                ),
            ),
            StarWarsFaction(
                "2",
                listOf(
                    Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)),
                    Lightsabers("2", 6f, isJarKai = false, listOf(brownBlade)),
                ),
            ),
            StarWarsFaction(
                "3",
                listOf(
                    Lightsabers("5", 15f, isJarKai = false, listOf(redBlade)),
                    Lightsabers("6", 18f, isJarKai = false, listOf(purpleDoubleBlade)),
                ),
            ),
            StarWarsFaction(
                "",
                listOf(
                    Lightsabers("7", 9f, isJarKai = false, listOf(yellowBlade2)),
                    Lightsabers("8", 12f, isJarKai = false, listOf(yellowBlade2)),
                ),
            ),
            StarWarsFaction("5", listOf()),
        )

    @JvmStatic
    fun createStarWarsFactionsLightsabers(): List<StarWarsFaction<Lightsabers>> = listOf(
        StarWarsFaction(
            "1",
            listOf(
                Lightsabers("1", 3f, isJarKai = false, listOf(blueBlade)),
                Lightsabers("2", 6f, isJarKai = false, listOf(brownBlade)),
            ),
        ),
        StarWarsFaction(
            "2",
            listOf(
                Lightsabers("3", 9f, isJarKai = false, listOf(greenDoubleBlade)),
                Lightsabers("4", 12f, isJarKai = false, listOf(yellowBlade)),
            ),
        ),
        StarWarsFaction(
            "3",
            listOf(
                Lightsabers("5", 15f, isJarKai = false, listOf(redBlade)),
                Lightsabers("6", 18f, isJarKai = false, listOf(purpleDoubleBlade)),
            ),
        ),
    )
}
