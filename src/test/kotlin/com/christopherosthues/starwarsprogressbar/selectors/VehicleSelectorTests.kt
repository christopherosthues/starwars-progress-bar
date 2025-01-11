package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
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

@TestFor(classes = [VehicleSelector::class])
class VehicleSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(FactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(InorderFactionVehicleSelector)
        mockkObject(InorderVehicleNameVehicleSelector)
        mockkObject(RandomVehicleSelector)
        mockkObject(ReverseOrderFactionVehicleSelector)
        mockkObject(ReverseOrderVehicleNameVehicleSelector)
        mockkObject(RollingRandomVehicleSelector)

        setupStarWarsState(null)
        every { FactionHolder.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `selectVehicle should not update factions of faction holder if factions of faction holder are not empty`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))

        // Act
        VehicleSelector.selectVehicle(null, false, SelectionType.RANDOM_ALL)

        // Assert
        verify(exactly = 0) { FactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { InorderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RandomVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RollingRandomVehicleSelector.selectVehicle(any(), any()) }
    }

    @Test
    fun `selectVehicle should return missing vehicle if persistent state component is null`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))
        setupStarWarsState(null)

        // Act
        val result = VehicleSelector.selectVehicle(null, false, SelectionType.RANDOM_ALL)

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { FactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { InorderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RandomVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RollingRandomVehicleSelector.selectVehicle(any(), any()) }
    }

    @Test
    fun `selectVehicle should return missing vehicle if state is null`() {
        // Arrange
        every { FactionHolder.factions } returns listOf(StarWarsFaction("1", listOf()))
        setupStarWarsState(null)

        // Act
        val result = VehicleSelector.selectVehicle(null, false, SelectionType.RANDOM_ALL)

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { FactionHolder.updateFactions(any()) }
        verify(exactly = 0) { InorderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { InorderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RandomVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderFactionVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { ReverseOrderVehicleNameVehicleSelector.selectVehicle(any(), any()) }
        verify(exactly = 0) { RollingRandomVehicleSelector.selectVehicle(any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectVehicle should return correct vehicle if default vehicles are not all enabled and provided enabled vehicles are null`(
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
        every { FactionHolder.defaultVehicles } returns vehicles
        val enabledVehicles = mutableMapOf("1" to false, "2" to true, "3" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]
        every { InorderVehicleNameVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]
        every { RandomVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]
        every { ReverseOrderVehicleNameVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]
        every { RollingRandomVehicleSelector.selectVehicle(any(), defaultEnabled) } returns vehicles[1]

        // Act
        val result = VehicleSelector.selectVehicle(null, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = randomSelector) { RandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectVehicle should return correct vehicle if default vehicles are not all enabled and provided enabled vehicles are not null`(
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
        every { FactionHolder.defaultVehicles } returns vehicles
        val enabledVehicles = mapOf("1" to false, "2" to true, "3" to false)
        val vehiclesEnabledState = mutableMapOf("1" to true, "2" to true, "3" to false)
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            enableNewVehicles = true
        }
        setupStarWarsState(starWarsState)
        every { InorderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]
        every { InorderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]
        every { RandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]
        every { ReverseOrderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]
        every { RollingRandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) } returns vehicles[1]

        // Act
        val result = VehicleSelector.selectVehicle(enabledVehicles, defaultEnabled, selectionType)

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = factionSelector) { InorderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = vehicleSelector) { InorderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = randomSelector) { RandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = reverseFactionSelector) { ReverseOrderFactionVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = reverseVehicleSelector) { ReverseOrderVehicleNameVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
        verify(exactly = rollingRandomSelector) { RollingRandomVehicleSelector.selectVehicle(enabledVehicles, defaultEnabled) }
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

    //endregion

    //region Test data

    private val missingVehicle = StarWarsVehicle("missing", "green", 0, 0, 0f)

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun selectorValues(): Stream<Arguments> = Stream.of(
            Arguments.of(SelectionType.INORDER_FACTION, 1, 0, 0, 0, 0, 0),
            Arguments.of(SelectionType.INORDER_VEHICLE_NAME, 0, 1, 0, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_ALL, 0, 0, 1, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, 0, 0, 0, 1, 0, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_FACTION, 0, 0, 0, 0, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_VEHICLE_NAME, 0, 0, 0, 0, 0, 1),
        )
    }

    //endregion
}
