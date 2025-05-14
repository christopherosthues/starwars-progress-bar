package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.randomInt
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestFor(classes = [RandomSelector::class])
class RandomSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(StarWarsBundle::message)
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)

        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles and enabled lightsabers and default vehicles and default lightsabers are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), defaultEnabled)

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
        val result = RandomSelector.selectEntity(
            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
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
        val result = RandomSelector.selectEntity(
            mapOf(),
            mapOf("4.1" to true, "3.2" to false, "3.3" to true),
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
        val result = RandomSelector.selectEntity(
            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
            mapOf("4.1" to true, "3.2" to false, "3.3" to true),
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
            RandomSelector.selectEntity(mapOf("2.1" to enabled, "1.2" to enabled, "1.3" to enabled), mapOf(), true)

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
            RandomSelector.selectEntity(mapOf(), mapOf("4.1" to enabled, "3.2" to enabled, "3.3" to enabled), true)

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
            RandomSelector.selectEntity(
                mapOf("2.1" to enabled, "1.2" to enabled, "1.3" to enabled),
                mapOf("4.1" to enabled, "3.2" to enabled, "3.3" to enabled),
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
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are empty and default lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are empty and default vehicles and lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), false)

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
            RandomSelector.selectEntity(
                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
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
            RandomSelector.selectEntity(
                mapOf(),
                mapOf("4.1" to false, "3.2" to false, "3.3" to false),
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
            RandomSelector.selectEntity(
                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
                mapOf("4.1" to false, "3.2" to false, "3.3" to false),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct vehicle if provided enabled lightsabers are empty and default lightsabers are not empty and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `selectEntity should return correct vehicle and lightsaber if provided enabled vehicles and lightsabers are empty and default vehicles and lightsabers are not empty and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size) }
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledValues")
    fun `selectEntity should return correct vehicle if provided enabled vehicles are all true and default vehicles are not empty`(
        index: Int,
        defaultEnabled: Boolean,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { randomInt(any()) } returns index

        // Act
        val result =
            RandomSelector.selectEntity(mapOf("2.1" to true, "1.2" to true, "1.3" to true), mapOf(), defaultEnabled)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledValues")
    fun `selectEntity should return correct lightsaber if provided enabled lightsabers are all true and default lightsabers are not empty`(
        index: Int,
        defaultEnabled: Boolean,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index

        // Act
        val result =
            RandomSelector.selectEntity(mapOf(), mapOf("4.1" to true, "3.2" to true, "3.3" to true), defaultEnabled)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size) }
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledVehiclesAndLightsabersValues")
    fun `selectEntity should return correct vehicle and lightsaber if provided enabled vehicles and lightsabers are all true and default vehicles and lightsabers are not empty`(
        index: Int,
        defaultEnabled: Boolean,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result = RandomSelector.selectEntity(
            mapOf("2.1" to true, "1.2" to true, "1.3" to true),
            mapOf("4.1" to true, "3.2" to true, "3.3" to true),
            defaultEnabled,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `selectEntity should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is false`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf("1.2" to true, "1.3" to true), mapOf(), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index + 1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size - 1) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `selectEntity should return correct lightsaber if provided enabled lightsabers do not contain all default lightsabers and default enabled is false`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf("3.2" to true, "3.3" to true), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[index + 1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size - 1) }
    }

    @ParameterizedTest
    @MethodSource("indexAndExpectedIndexValues")
    fun `selectEntity should return correct vehicle and lightsaber if provided enabled vehicles and lightsabers do not contain all default vehicles and lightsabers and default enabled is false`(
        index: Int,
        expectedIndex: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result =
            RandomSelector.selectEntity(mapOf("1.2" to true, "1.3" to true), mapOf("3.2" to true, "3.3" to true), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[expectedIndex], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size - 2) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf("1.2" to true, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct lightsaber if provided enabled lightsabers do not contain all default lightsabers and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf("3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `selectEntity should return correct vehicle and lightsaber if provided enabled vehicles and lightsabers do not contain all default vehicles and lightsabers and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result =
            RandomSelector.selectEntity(mapOf("1.2" to true, "1.3" to true), mapOf("3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct vehicle if provided enabled vehicles and default vehicles have different additional entries`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf("1.2" to true, "1.3" to true, "1.4" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectEntity should return correct lightsaber if provided enabled lightsabers and default lightsabers have different additional entries`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf("3.2" to true, "3.3" to true, "3.4" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `selectEntity should return correct vehicle and lightsaber if provided enabled vehicles and lightsabers and default vehicles and lightsabers have different additional entries`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result = RandomSelector.selectEntity(
            mapOf("1.2" to true, "1.3" to true, "1.4" to true),
            mapOf("3.2" to true, "3.3" to true, "3.4" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size) }
    }

    @Test
    fun `selectEntity should return correct vehicle if default vehicles are not all enabled`() {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { randomInt(any()) } returns 0

        // Act
        val result = RandomSelector.selectEntity(mapOf("2.1" to false, "1.2" to true, "1.3" to false), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size - 2) }
    }

    @Test
    fun `selectEntity should return correct lightsaber if default lightsabers are not all enabled`() {
        // Arrange
        mockkStatic(::randomInt)
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns 0

        // Act
        val result = RandomSelector.selectEntity(mapOf(), mapOf("4.1" to false, "3.2" to true, "3.3" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(lightsabers[1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(lightsabers.size - 2) }
    }

    @ParameterizedTest
    @MethodSource("indexAndExpectedIndexNotAllEnabledValues")
    fun `selectEntity should return correct vehicle and lightsaber if default vehicles and lightsabers are not all enabled`(
        index: Int,
        expectedIndex: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { randomInt(any()) } returns index
        val entities = vehicles + lightsabers

        // Act
        val result = RandomSelector.selectEntity(
            mapOf("2.1" to false, "1.2" to true, "1.3" to false),
            mapOf("4.1" to false, "3.2" to true, "3.3" to false),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(entities[expectedIndex], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(entities.size - 4) }
    }

    //endregion

    //region Helper methods

    private fun createStarWarsVehicles(): List<StarWarsVehicle> {
        val vehicles = listOf(
            StarWarsVehicle("1", "a", 1, 1, 1f).apply { factionId = "2" },
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
                        listOf(Blade("d", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0)),
                    ),
                ),
            ).apply { factionId = "4" },
            Lightsabers(
                "2",
                5f,
                isJarKai = false,
                listOf(
                    Lightsaber(
                        1,
                        isDoubleBladed = false,
                        yShift = 2,
                        listOf(Blade("e", isShoto = true, bladeSize = 8, xBlade = 0, yBlade = 0)),
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
                            Blade("f", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
                            Blade("f", isShoto = false, bladeSize = 8, xBlade = 0, yBlade = 0),
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

        @JvmStatic
        fun indexAndDefaultEnabledValues(): Stream<Arguments> = Stream.of(
            Arguments.of(0, true),
            Arguments.of(0, false),
            Arguments.of(1, true),
            Arguments.of(1, false),
            Arguments.of(2, true),
            Arguments.of(2, false),
        )

        @JvmStatic
        fun indexAndDefaultEnabledVehiclesAndLightsabersValues(): Stream<Arguments> = Stream.of(
            Arguments.of(0, true),
            Arguments.of(0, false),
            Arguments.of(1, true),
            Arguments.of(1, false),
            Arguments.of(2, true),
            Arguments.of(2, false),
            Arguments.of(3, true),
            Arguments.of(3, false),
            Arguments.of(4, true),
            Arguments.of(4, false),
            Arguments.of(5, true),
            Arguments.of(5, false),
        )

        @JvmStatic
        fun indexAndExpectedIndexValues(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 1),
            Arguments.of(1, 2),
            Arguments.of(2, 4),
            Arguments.of(3, 5),
        )

        @JvmStatic
        fun indexAndExpectedIndexNotAllEnabledValues(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 1),
            Arguments.of(1, 4),
        )
    }

    //endregion
}
