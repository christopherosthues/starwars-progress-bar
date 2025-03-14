package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
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

@TestFor(classes = [RandomVehicleSelector::class])
class RandomVehicleSelectorTests {
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
    fun `selectVehicle should return missing vehicle if provided enabled vehicles and default vehicles are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf(), defaultEnabled)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectVehicle should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()

        // Act
        val result = RandomVehicleSelector.selectVehicle(
            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
            defaultEnabled,
        )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectVehicle should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty and all values are true or false`(
        enabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()

        // Act
        val result =
            RandomVehicleSelector.selectVehicle(mapOf("2.1" to enabled, "1.2" to enabled, "1.3" to enabled), true)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectVehicle should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectVehicle should return missing vehicle if provided enabled vehicles are all false and default vehicles are not empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()

        // Act
        val result =
            RandomVehicleSelector.selectVehicle(
                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectVehicle should return correct vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledValues")
    fun `selectVehicle should return correct vehicle if provided enabled vehicles are all true and default vehicles are not empty`(
        index: Int,
        defaultEnabled: Boolean,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf("2.1" to true, "1.2" to true, "1.3" to true), defaultEnabled)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `selectVehicle should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is false`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf("1.2" to true, "1.3" to true), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index + 1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size - 1) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectVehicle should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is true`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf("1.2" to true, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectVehicle should return correct vehicle if provided enabled vehicles and default vehicles have different additional entries`(
        index: Int,
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf("1.2" to true, "1.3" to true, "1.4" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[index], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @Test
    fun `selectVehicle should return correct vehicle if default vehicles are not all enabled`() {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns 0

        // Act
        val result = RandomVehicleSelector.selectVehicle(mapOf("2.1" to false, "1.2" to true, "1.3" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(vehicles[1], result) },
            { Assertions.assertNotEquals(missingVehicle, result) },
        )
        verify(exactly = 1) { randomInt(vehicles.size - 2) }
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
    }

    //endregion
}
