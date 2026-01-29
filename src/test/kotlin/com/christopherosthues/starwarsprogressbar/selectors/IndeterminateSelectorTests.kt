package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import io.mockk.*
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestFor(classes = [IndeterminateSelector::class])
class IndeterminateSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        // mock constructors for selector implementations so we can stub instance methods
        mockkConstructor(InorderFactionSelector::class)
        mockkConstructor(InorderNameSelector::class)
        mockkConstructor(RandomSelector::class)
        mockkConstructor(ReverseOrderFactionSelector::class)
        mockkConstructor(ReverseOrderNameSelector::class)
        mockkConstructor(RollingRandomSelector::class)

        setupStarWarsState(null)
        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { anyConstructed<InorderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<InorderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<RandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<ReverseOrderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<ReverseOrderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<RollingRandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { anyConstructed<InorderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<InorderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RollingRandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { anyConstructed<InorderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<InorderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<RandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<ReverseOrderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<ReverseOrderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]
        every { anyConstructed<RollingRandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns vehicles[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { anyConstructed<InorderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<InorderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RollingRandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every { anyConstructed<RandomSelector>().selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]
        every {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]
        every {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]
        every {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]
        every {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]
        every {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns lightsabers[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every { anyConstructed<RandomSelector>().selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled) } returns vehicles[1]
        every {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]
        every {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        } returns vehicles[1]

        // Act
        val result = IndeterminateSelector.selectEntity(
            emptyMap(),
            emptyMap(),
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
        val lightsabers = createLightsabers()
        val vehicles = createStarWarsVehicles()
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
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { anyConstructed<InorderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<InorderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderFactionSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<ReverseOrderNameSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]
        every { anyConstructed<RollingRandomSelector>().selectEntity(any(), any(), defaultEnabled) } returns lightsabers[1]

        // Act
        val result = IndeterminateSelector.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled, selectionType, EntitySelectionType.ALL)

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) {
            anyConstructed<InorderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = vehicleSelector) {
            anyConstructed<InorderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = randomSelector) {
            anyConstructed<RandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseFactionSelector) {
            anyConstructed<ReverseOrderFactionSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = reverseVehicleSelector) {
            anyConstructed<ReverseOrderNameSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
        verify(exactly = rollingRandomSelector) {
            anyConstructed<RollingRandomSelector>().selectEntity(
                enabledVehicles,
                enabledLightsabers,
                defaultEnabled,
            )
        }
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
        Lightsabers(
            "4",
            1f,
            isJarKai = false,
            listOf(
                Lightsaber(
                    1,
                    isDoubleBladed = false,
                    yShift = 1,
                    listOf(
                        Blade(
                            "a",
                            isShoto = false,
                            bladeSize = 8,
                            xBlade = 0,
                            yBlade = 0,
                        ),
                    ),
                ),
            ),
        ),
        Lightsabers(
            "5",
            2f,
            isJarKai = false,
            listOf(
                Lightsaber(
                    1,
                    isDoubleBladed = false,
                    yShift = 2,
                    listOf(
                        Blade(
                            "b",
                            isShoto = true,
                            bladeSize = 8,
                            xBlade = 0,
                            yBlade = 0,
                        ),
                    ),
                ),
            ),
        ),
        Lightsabers(
            "6",
            3f,
            isJarKai = false,
            listOf(
                Lightsaber(
                    1,
                    isDoubleBladed = true,
                    yShift = 3,
                    listOf(
                        Blade(
                            "c",
                            isShoto = false,
                            bladeSize = 8,
                            xBlade = 0,
                            yBlade = 0,
                        ),
                    ),
                ),
            ),
        ),
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
