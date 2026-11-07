package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.helpers.assertEntityOrder3Items
import com.christopherosthues.starwarsprogressbar.helpers.assertEntityOrder6Items
import com.christopherosthues.starwarsprogressbar.helpers.rotateByOne
import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestFor(classes = [ReverseOrderNameSelector::class])
class ReverseOrderNameSelectorTests {
    //region Test lifecycle

    private var sut = ReverseOrderNameSelector()

    @BeforeEach
    fun setup() {
        mockkStatic(StarWarsBundle::message)
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)

        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
        sut = ReverseOrderNameSelector()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers and default vehicles and lightsabers are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = sut.selectEntity(mapOf(), mapOf(), defaultEnabled)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = sut.selectEntity(
            mapOf("1.1" to true, "1.2" to false, "1.3" to true),
            mapOf(),
            defaultEnabled,
        )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are not empty and default lightsabers are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = sut.selectEntity(
            mapOf(),
            mapOf("3.1" to true, "3.2" to false, "3.3" to true),
            defaultEnabled,
        )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are not empty and default vehicles and lightsabers are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = sut.selectEntity(
            mapOf("1.1" to true, "1.2" to false, "1.3" to true),
            mapOf("3.1" to true, "3.2" to false, "3.3" to true),
            defaultEnabled,
        )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectEntity should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty and all values are true or false`(
        enabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result =
            sut.selectEntity(
                mapOf("1.1" to enabled, "1.2" to enabled, "1.3" to enabled),
                mapOf(),
                true,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are not empty and default lightsabers are empty and all values are true or false`(
        enabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result =
            sut.selectEntity(
                mapOf(),
                mapOf("3.1" to enabled, "3.2" to enabled, "3.3" to enabled),
                true,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are not empty and default vehicles and lightsabers are empty and all values are true or false`(
        enabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result =
            sut.selectEntity(
                mapOf("1.1" to enabled, "1.2" to enabled, "1.3" to enabled),
                mapOf("3.1" to enabled, "3.2" to enabled, "3.3" to enabled),
                true,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = sut.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are empty and default lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = sut.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are empty and default vehicles and lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = sut.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles are all false and default vehicles are not empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result =
            sut.selectEntity(
                mapOf("1.1" to false, "1.2" to false, "1.3" to false),
                mapOf(),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are all false and default lightsabers are not empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result =
            sut.selectEntity(
                mapOf(),
                mapOf("3.1" to false, "3.2" to false, "3.3" to false),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are all false and default vehicles and lightsabers are not empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result =
            sut.selectEntity(
                mapOf("1.1" to false, "1.2" to false, "1.3" to false),
                mapOf("3.1" to false, "3.2" to false, "3.3" to false),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return vehicles in sorted order`() {
        // Arrange
        val vehicles = createStarWarsVehicles().toMutableList()
        val entities: MutableList<StarWarsEntity> = vehicles.toMutableList()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        var result = mutableListOf<StarWarsEntity>()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to true, "1.3" to true),
                    mapOf(),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 1, 0, entities, result)

        // Arrange
        rotateByOne(entities)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to true, "1.3" to true),
                    mapOf(),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(1, 0, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false, "1.3" to true),
                    mapOf(),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(1, 2, 1, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false),
                    mapOf(),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 1, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false),
                    mapOf(),
                    false,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 2, 2, entities, result)
    }

    @Test
    fun `selectEntity should return lightsabers in sorted order`() {
        // Arrange
        val lightsabers = createLightsabers().toMutableList()
        val entities: MutableList<StarWarsEntity> = lightsabers.toMutableList()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers

        // Act
        var result = mutableListOf<StarWarsEntity>()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf(),
                    mapOf("3.1" to true, "3.2" to true, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 1, 0, entities, result)

        // Arrange
        rotateByOne(entities)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf(),
                    mapOf("3.1" to true, "3.2" to true, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(1, 0, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf(),
                    mapOf("3.1" to true, "3.2" to false, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(1, 2, 1, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf(),
                    mapOf("3.1" to true, "3.2" to false),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 1, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf(),
                    mapOf("3.1" to true, "3.2" to false),
                    false,
                ),
            )
        }

        // Assert
        assertEntityOrder3Items(2, 2, 2, entities, result)
    }

    @Test
    fun `selectEntity should return vehicles and lightsabers in sorted order`() {
        // Arrange
        val vehicles = createStarWarsVehicles().toMutableList()
        val lightsabers = createLightsabers().toMutableList()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val entities = (lightsabers + vehicles).toMutableList()

        // Act
        var result = mutableListOf<StarWarsEntity>()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to true, "1.3" to true),
                    mapOf("3.1" to true, "3.2" to true, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder6Items(5, 2, 4, 1, 3, 0, entities, result)

        // Arrange
        rotateByOne(entities)
        rotateByOne(entities, 3)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to true, "1.3" to true),
                    mapOf("3.1" to true, "3.2" to true, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder6Items(4, 1, 3, 0, 5, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false, "1.3" to true),
                    mapOf("3.1" to true, "3.2" to false, "3.3" to true),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder6Items(4, 1, 5, 2, 4, 1, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false),
                    mapOf("3.1" to true, "3.2" to false),
                    true,
                ),
            )
        }

        // Assert
        assertEntityOrder6Items(5, 2, 4, 1, 5, 2, entities, result)

        // Act
        result = mutableListOf()
        repeat(entities.indices.count()) {
            result.add(
                sut.selectEntity(
                    mapOf("1.1" to true, "1.2" to false),
                    mapOf("3.1" to true, "3.2" to false),
                    false,
                ),
            )
        }

        // Assert
        assertEntityOrder6Items(5, 2, 5, 2, 5, 2, entities, result)
    }

    //endregion

    //region Helper methods

    private fun createStarWarsVehicles(): List<StarWarsVehicle> {
        val vehicles = listOf(
            StarWarsVehicle("1", "a", 1, 1, 1f).apply { factionId = "1" },
            StarWarsVehicle("2", "b", 2, 2, 2f).apply { factionId = "1" },
            StarWarsVehicle("3", "c", 3, 3, 3f).apply { factionId = "1" },
        )
        for (vehicle in vehicles) {
            every { StarWarsBundle.message(vehicle.localizationKey) } returns vehicle.id
        }

        return vehicles
    }

    private fun createLightsabers(): List<Lightsabers> {
        val lightsabers = listOf(
            Lightsabers(
                "1",
                4f,
                isJarKai = false,
                listOf(
                    Lightsaber(
                        1,
                        isDoubleBladed = false,
                        yShift = 1,
                        listOf(Blade("a", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0)),
                    ),
                ),
            ).apply { factionId = "3" },
            Lightsabers(
                "2",
                5f,
                isJarKai = false,
                listOf(
                    Lightsaber(
                        1,
                        isDoubleBladed = false,
                        yShift = 2,
                        listOf(Blade("b", isShoto = true, bladeSize = 8, xBlade = 0, yBlade = 0)),
                    ),
                ),
            ).apply { factionId = "3" },
            Lightsabers(
                "3",
                6f,
                isJarKai = false,
                listOf(
                    Lightsaber(
                        1,
                        isDoubleBladed = true,
                        yShift = 3,
                        listOf(
                            Blade("c", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                            Blade("c", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                        ),
                    ),
                ),
            ).apply { factionId = "3" },
        )
        for (lightsaber in lightsabers) {
            every { StarWarsBundle.message(lightsaber.localizationKey) } returns lightsaber.id
        }

        return lightsabers
    }

    //endregion

    //region Test data

    private val missingVehicle = StarWarsVehicle("missing", "green", 0, 0, 0f)

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun defaultEnabledValues(): Stream<Arguments> = Stream.of(Arguments.of(true), Arguments.of(false))
    }

    //endregion
}
