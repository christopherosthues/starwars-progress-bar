package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestFor(classes = [VehicleSelector::class])
class VehicleSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(FactionHolder)
        mockkObject(StarWarsPersistentStateComponent)

        setupStarWarsState(null)
        every { FactionHolder.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    //region selectRandomVehicle with parameters tests

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle with parameters should return missing vehicle if provided enabled vehicles and default vehicles are empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf(), defaultEnabled)

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle with parameters should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("1" to true, "2" to false, "3" to true), defaultEnabled)

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectRandomVehicle with parameters should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty and all values are true or false`(
        enabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("1" to enabled, "2" to enabled, "3" to enabled), true)

        // Assert
        assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectRandomVehicle with parameters should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
        // Arrange
        every { FactionHolder.defaultVehicles } returns createStarWarsVehicles()

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf(), false)

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle with parameters should return correct vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is true`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf(), true)

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle with parameters should return missing vehicle if provided enabled vehicles are all false and default vehicles are not empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns createStarWarsVehicles()

        // Act
        val result =
            VehicleSelector.selectRandomVehicle(mapOf("1" to false, "2" to false, "3" to false), defaultEnabled)

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledValues")
    fun `selectRandomVehicle with parameters should return correct vehicle if provided enabled vehicles are all true and default vehicles are not empty`(
        index: Int,
        defaultEnabled: Boolean
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("1" to true, "2" to true, "3" to true), defaultEnabled)

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `selectRandomVehicle with parameters should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is false`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("2" to true, "3" to true), false)

        // Assert
        assertAll(
            { assertEquals(vehicles[index + 1], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size - 1) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle with parameters should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is true`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("2" to true, "3" to true), true)

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle with parameters should return correct vehicle if provided enabled vehicles and default vehicles have different additional entries`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("2" to true, "3" to true, "4" to true), true)

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @Test
    fun `selectRandomVehicle with parameters should return correct vehicle if default vehicles are not all enabled`() {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        every { randomInt(any()) } returns 0

        // Act
        val result = VehicleSelector.selectRandomVehicle(mapOf("1" to false, "2" to true, "3" to false), true)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size - 2) }
    }

    //endregion

    //region selectRandomVehicles tests

    @Test
    fun `selectRandomVehicles should not update factions of faction holder if factions of faction holder are not empty`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))

        // Act
        VehicleSelector.selectRandomVehicle()

        // Assert
        verify(exactly = 0) { FactionHolder.updateFactions(any()) }
    }

    @Test
    fun `selectRandomVehicles should return missing vehicle if persistent state component is null`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))
        setupStarWarsState(null)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectRandomVehicles should return missing vehicle if state is null`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))
        setupStarWarsState(null)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle should return missing vehicle if provided enabled vehicles and default vehicles are empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf()
            enableNewVehicles = defaultEnabled
        }
        setupStarWarsState(starWarsState)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("1" to true, "2" to false, "3" to true)
            enableNewVehicles = defaultEnabled
        }
        setupStarWarsState(starWarsState)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `selectRandomVehicle should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty and all values are true or false`(
        enabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns listOf()
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("1" to enabled, "2" to enabled, "3" to enabled)
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectRandomVehicle should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
        // Arrange
        every { FactionHolder.defaultVehicles } returns createStarWarsVehicles()
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf()
            enableNewVehicles = false
        }
        setupStarWarsState(starWarsState)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle should return correct vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is true`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf()
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectRandomVehicle should return missing vehicle if provided enabled vehicles are all false and default vehicles are not empty`(
        defaultEnabled: Boolean
    ) {
        // Arrange
        every { FactionHolder.defaultVehicles } returns createStarWarsVehicles()
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("1" to false, "2" to false, "3" to false)
            enableNewVehicles = defaultEnabled
        }
        setupStarWarsState(starWarsState)

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("indexAndDefaultEnabledValues")
    fun `selectRandomVehicle should return correct vehicle if provided enabled vehicles are all true and default vehicles are not empty`(
        index: Int,
        defaultEnabled: Boolean
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("1" to true, "2" to true, "3" to true)
            enableNewVehicles = defaultEnabled
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `selectRandomVehicle should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is false`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("2" to true, "3" to true)
            enableNewVehicles = false
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[index + 1], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size - 1) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle should return correct vehicle if provided enabled vehicles do not contain all default vehicles and default enabled is true`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("2" to true, "3" to true)
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2])
    fun `selectRandomVehicle should return correct vehicle if provided enabled vehicles and default vehicles have different additional entries`(
        index: Int
    ) {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("2" to true, "3" to true, "4" to true)
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns index

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[index], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size) }
    }

    @Test
    fun `selectRandomVehicle should return correct vehicle if default vehicles are not all enabled`() {
        // Arrange
        mockkStatic(::randomInt)
        val vehicles = createStarWarsVehicles()
        every { FactionHolder.defaultVehicles } returns vehicles
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = mapOf("1" to false, "2" to true, "3" to false)
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { randomInt(any()) } returns 0

        // Act
        val result = VehicleSelector.selectRandomVehicle()

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) }
        )
        verify(exactly = 1) { randomInt(vehicles.size - 2) }
    }

    //endregion

    //endregion

    //region Helper methods

    private fun setupStarWarsState(starWarsState: StarWarsState?) {
        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns starWarsState
    }

    private fun createStarWarsVehicles() = listOf(
        StarWarsVehicle("1", "a", 1, 1, 1f),
        StarWarsVehicle("2", "b", 2, 2, 2f),
        StarWarsVehicle("3", "c", 3, 3, 3f)
    )

    //endregion

    //region Test data

    private val missingVehicle = StarWarsVehicle("missing", "green", 0, 0, 0f)

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun defaultEnabledValues(): Stream<Arguments> {
            return Stream.of(Arguments.of(true), Arguments.of(false))
        }

        @JvmStatic
        fun indexAndDefaultEnabledValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, true),
                Arguments.of(0, false),
                Arguments.of(1, true),
                Arguments.of(1, false),
                Arguments.of(2, true),
                Arguments.of(2, false)
            )
        }
    }

    //endregion
}
