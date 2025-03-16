package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
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
import java.util.stream.Stream

@TestFor(classes = [StarWarsSelector::class])
class StarWarsSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(InorderFactionSelector)
        mockkObject(InorderNameSelector)
        mockkObject(RandomSelector)
        mockkObject(ReverseOrderFactionSelector)
        mockkObject(ReverseOrderNameSelector)
        mockkObject(RollingRandomSelector)

        setupStarWarsState(null)
        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `selectEntity should not update factions of faction holder if factions of faction holder are not empty`() {
        // Arrange
        every { StarWarsFactionHolder.vehicleFactions } returns listOf(StarWarsFaction("1", listOf()))
        every { StarWarsFactionHolder.lightsaberFactions } returns listOf(StarWarsFaction("2", listOf()))

        // Act
        StarWarsSelector.selectEntity(null, null, false, SelectionType.RANDOM_ALL)

        // Assert
        verify(exactly = 0) { StarWarsFactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { InorderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RandomSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RollingRandomSelector.selectEntity(any(), any(), any()) }
    }

    @Test
    fun `selectEntity should return missing vehicle if persistent state component is null`() {
        // Arrange
        every { StarWarsFactionHolder.vehicleFactions } returns listOf(StarWarsFaction("1", listOf()))
        every { StarWarsFactionHolder.lightsaberFactions } returns listOf(StarWarsFaction("2", listOf()))
        setupStarWarsState(null)

        // Act
        val result = StarWarsSelector.selectEntity(null, null, false, SelectionType.RANDOM_ALL)

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { StarWarsFactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { InorderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RandomSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RollingRandomSelector.selectEntity(any(), any(), any()) }
    }

    @Test
    fun `selectEntity should return missing vehicle if state is null`() {
        // Arrange
        every { StarWarsFactionHolder.vehicleFactions } returns listOf(StarWarsFaction("1", listOf()))
        every { StarWarsFactionHolder.lightsaberFactions } returns listOf(StarWarsFaction("2", listOf()))
        setupStarWarsState(null)

        // Act
        val result = StarWarsSelector.selectEntity(null, null, false, SelectionType.RANDOM_ALL)

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { StarWarsFactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { InorderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RandomSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { ReverseOrderNameSelector.selectEntity(any(), any(), any()) }
        verify(exactly = 0) { RollingRandomSelector.selectEntity(any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles are not all enabled and provided enabled vehicles are null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        val enabledVehicles = mutableMapOf("1" to false, "2" to true, "3" to false)
        val enabledLightsabers = mutableMapOf<String, Boolean>()
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { InorderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { RandomSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { RollingRandomSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(null, null, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default lightsabers are not all enabled and provided enabled lightsabers are null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mutableMapOf<String, Boolean>()
        val enabledLightsabers = mutableMapOf("4" to false, "5" to true, "6" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { InorderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { RandomSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { RollingRandomSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(null, null, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mutableMapOf("1" to false, "2" to true, "3" to false)
        val enabledLightsabers = mutableMapOf("4" to false, "5" to true, "6" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { InorderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { RandomSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { RollingRandomSelector.selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(null, null, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mutableMapOf("1" to false, "2" to true, "3" to false)
        val enabledLightsabers = mutableMapOf("4" to false, "5" to true, "6" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { InorderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { RandomSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderFactionSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderNameSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { RollingRandomSelector.selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(null, null, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles are not all enabled and provided enabled vehicles are not null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        val enabledVehicles = mapOf("1" to false, "2" to true, "3" to false)
        val vehiclesEnabledState = mutableMapOf("1" to true, "2" to true, "3" to false)
        val enabledLightsabers = mapOf<String, Boolean>()
        val lightsabersEnabledState = mutableMapOf<String, Boolean>()
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            lightsabersEnabled = lightsabersEnabledState
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default lightsabers are not all enabled and provided enabled lightsabers are not null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mapOf<String, Boolean>()
        val vehiclesEnabledState = mutableMapOf<String, Boolean>()
        val enabledLightsabers = mapOf("4" to false, "5" to true, "6" to false)
        val lightsabersEnabledState = mutableMapOf("4" to true, "5" to true, "6" to false)
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            lightsabersEnabled = lightsabersEnabledState
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are not null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mapOf("1" to false, "2" to true, "3" to false)
        val vehiclesEnabledState = mutableMapOf("1" to true, "2" to true, "3" to false)
        val enabledLightsabers = mapOf("4" to false, "5" to true, "6" to false)
        val lightsabersEnabledState = mutableMapOf("4" to true, "5" to true, "6" to false)
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            lightsabersEnabled = lightsabersEnabledState
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are not null`(
        selectionType: SelectionType,
        factionSelector: Int,
        vehicleSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseVehicleSelector: Int,
    ) {
        // Arrange
        val vehicles = createStarWarsVehicles()
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mapOf("1" to false, "2" to true, "3" to false)
        val vehiclesEnabledState = mutableMapOf("1" to true, "2" to true, "3" to false)
        val enabledLightsabers = mapOf("4" to false, "5" to true, "6" to false)
        val lightsabersEnabledState = mutableMapOf("4" to true, "5" to true, "6" to false)
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            lightsabersEnabled = lightsabersEnabledState
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]
        every { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = randomSelector) { RandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderNameSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) }
    }

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
        StarWarsVehicle("3", "c", 3, 3, 3f),
    )

    private fun createLightsabers() = listOf(
        Lightsaber("4", "a", 1f, isShoto = false, isDoubleBladed = false),
        Lightsaber("5", "b", 2f, isShoto = true, isDoubleBladed = false),
        Lightsaber("6", "c", 3f, isShoto = false, isDoubleBladed = true),
    )

    //endregion

    //region Test data

    private val missingVehicle = StarWarsVehicle("missing", "green", 0, 0, 0f)

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun selectorValues(): Stream<Arguments> = Stream.of(
            Arguments.of(SelectionType.INORDER_FACTION, 1, 0, 0, 0, 0, 0),
            Arguments.of(SelectionType.INORDER_NAME, 0, 1, 0, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_ALL, 0, 0, 1, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, 0, 0, 0, 1, 0, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_FACTION, 0, 0, 0, 0, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, 0, 0, 0, 0, 0, 1),
        )
    }

    //endregion
}
