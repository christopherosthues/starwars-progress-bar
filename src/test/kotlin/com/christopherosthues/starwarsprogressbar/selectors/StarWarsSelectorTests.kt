package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
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
import java.util.stream.Stream

@TestFor(classes = [StarWarsSelector::class])
class StarWarsSelectorTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(StarWarsBundle::message)
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        // Instead of mocking all underlying selectors, mock the Determinate/Indeterminate singletons
        mockkObject(DeterminateSelector)
        mockkObject(IndeterminateSelector)

        setupStarWarsState(null)
        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
        every { StarWarsBundle.message("vehicles.faction.1") } returns("1")
        every { StarWarsBundle.message("lightsabers.faction.2") } returns("2")
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
        every { StarWarsFactionHolder.lightsabersFactions } returns listOf(StarWarsFaction("2", listOf()))

        // Act
        StarWarsSelector.selectEntity(
            null,
            null,
            false,
            SelectionType.RANDOM_ALL,
            SelectionType.RANDOM_ALL,
            false,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        verify(exactly = 0) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `selectEntity should return missing vehicle if persistent state component is null`() {
        // Arrange
        every { StarWarsFactionHolder.vehicleFactions } returns listOf(StarWarsFaction("1", listOf()))
        every { StarWarsFactionHolder.lightsabersFactions } returns listOf(StarWarsFaction("2", listOf()))
        setupStarWarsState(null)

        // Act
        val result = StarWarsSelector.selectEntity(
            null,
            null,
            false,
            SelectionType.RANDOM_ALL,
            SelectionType.RANDOM_ALL,
            false,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `selectEntity should return missing vehicle if state is null`() {
        // Arrange
        every { StarWarsFactionHolder.vehicleFactions } returns listOf(StarWarsFaction("1", listOf()))
        every { StarWarsFactionHolder.lightsabersFactions } returns listOf(StarWarsFaction("2", listOf()))
        setupStarWarsState(null)

        // Act
        val result = StarWarsSelector.selectEntity(
            null,
            null,
            false,
            SelectionType.RANDOM_ALL,
            SelectionType.RANDOM_ALL,
            false,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertEquals(missingVehicle, result)
        verify(exactly = 0) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles are not all enabled and provided enabled vehicles are null`(
        determinateSelectionType: SelectionType,
        indeterminateSelectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateExpectedCalls: Int,
        indeterminateExpectedCalls: Int,
    ) {
        // Arrange
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        val enabledVehicles = mutableMapOf("1.1" to false, "1.2" to true, "1.3" to false)
        val enabledLightsabers = mutableMapOf<String, Boolean>()
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { DeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(
            null,
            null,
            defaultEnabled,
            determinateSelectionType,
            indeterminateSelectionType,
            isIndeterminate,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = determinateExpectedCalls) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = indeterminateExpectedCalls) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default lightsabers are not all enabled and provided enabled lightsabers are null`(
        determinateSelectionType: SelectionType,
        indeterminateSelectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateExpectedCalls: Int,
        indeterminateExpectedCalls: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mutableMapOf<String, Boolean>()
        val enabledLightsabers = mutableMapOf("2.4" to false, "2.5" to true, "2.6" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { DeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(
            null,
            null,
            defaultEnabled,
            determinateSelectionType,
            indeterminateSelectionType,
            isIndeterminate,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = determinateExpectedCalls) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = indeterminateExpectedCalls) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct vehicle if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are null`(
        determinateSelectionType: SelectionType,
        indeterminateSelectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateExpectedCalls: Int,
        indeterminateExpectedCalls: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mutableMapOf("1.1" to false, "1.2" to true, "1.3" to false)
        val enabledLightsabers = mutableMapOf("2.4" to false, "2.5" to true, "2.6" to false)
        val defaultEnabled = false
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = enabledVehicles
            lightsabersEnabled = enabledLightsabers
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { DeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns vehicles[1]
        every { IndeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns vehicles[1]

        // Act
        val result = StarWarsSelector.selectEntity(
            null,
            null,
            defaultEnabled,
            determinateSelectionType,
            indeterminateSelectionType,
            isIndeterminate,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(vehicles[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = determinateExpectedCalls) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = indeterminateExpectedCalls) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should return correct lightsaber if default vehicles and lightsabers are not all enabled and provided enabled vehicles and lightsabers are null`(
        determinateSelectionType: SelectionType,
        indeterminateSelectionType: SelectionType,
        isIndeterminate: Boolean,
        determinateExpectedCalls: Int,
        indeterminateExpectedCalls: Int,
    ) {
        // Arrange
        val lightsabers = createLightsabers()
        val vehicles = createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        val enabledVehicles = mapOf("1.1" to false, "1.2" to true, "1.3" to false)
        val vehiclesEnabledState = mutableMapOf("1.1" to true, "1.2" to true, "1.3" to false)
        val enabledLightsabers = mapOf("2.4" to false, "2.5" to true, "2.6" to false)
        val lightsabersEnabledState = mutableMapOf("2.4" to true, "2.5" to true, "2.6" to false)
        val defaultEnabled = true
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = vehiclesEnabledState
            lightsabersEnabled = lightsabersEnabledState
            enableNew = true
        }
        setupStarWarsState(starWarsState)
        every { DeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns lightsabers[1]
        every { IndeterminateSelector.selectEntity(any(), any(), defaultEnabled, any(), any()) } returns lightsabers[1]

        // Act
        val result = StarWarsSelector.selectEntity(
            enabledVehicles,
            enabledLightsabers,
            defaultEnabled,
            determinateSelectionType,
            indeterminateSelectionType,
            isIndeterminate,
            EntitySelectionType.ALL,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(lightsabers[1], result) },
            { assertNotEquals(missingVehicle, result) },
        )

        verify(exactly = determinateExpectedCalls) { DeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
        verify(exactly = indeterminateExpectedCalls) { IndeterminateSelector.selectEntity(any(), any(), any(), any(), any()) }
    }

    //endregion

    //region Helper methods

    private fun setupStarWarsState(starWarsState: StarWarsState?) {
        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns starWarsState
    }

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
            ).apply { factionId = "2" },
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
            ).apply { factionId = "2" },
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
            ).apply { factionId = "2" },
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
        fun selectorValues(): Stream<Arguments> = Stream.of(
            // matching determinate/indeterminate (not indeterminate)
            Arguments.of(SelectionType.INORDER_FACTION, SelectionType.INORDER_FACTION, false, 1, 0),
            Arguments.of(SelectionType.INORDER_NAME, SelectionType.INORDER_NAME, false, 1, 0),
            Arguments.of(SelectionType.RANDOM_ALL, SelectionType.RANDOM_ALL, false, 1, 0),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, SelectionType.RANDOM_NOT_DISPLAYED, false, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_FACTION, SelectionType.REVERSE_ORDER_FACTION, false, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, SelectionType.REVERSE_ORDER_NAME, false, 1, 0),
            // matching determinate/indeterminate (indeterminate path)
            Arguments.of(SelectionType.INORDER_FACTION, SelectionType.INORDER_FACTION, true, 0, 1),
            Arguments.of(SelectionType.INORDER_NAME, SelectionType.INORDER_NAME, true, 0, 1),
            Arguments.of(SelectionType.RANDOM_ALL, SelectionType.RANDOM_ALL, true, 0, 1),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, SelectionType.RANDOM_NOT_DISPLAYED, true, 0, 1),
            Arguments.of(SelectionType.REVERSE_ORDER_FACTION, SelectionType.REVERSE_ORDER_FACTION, true, 0, 1),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, SelectionType.REVERSE_ORDER_NAME, true, 0, 1),
            // mixed combinations to exercise both paths - determinate
            Arguments.of(SelectionType.INORDER_FACTION, SelectionType.RANDOM_ALL, false, 1, 0),
            Arguments.of(SelectionType.RANDOM_ALL, SelectionType.INORDER_NAME, false, 1, 0),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, SelectionType.REVERSE_ORDER_FACTION, false, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, SelectionType.RANDOM_NOT_DISPLAYED, false, 1, 0),
            // mixed combinations to exercise both paths - indeterminate
            Arguments.of(SelectionType.INORDER_FACTION, SelectionType.RANDOM_ALL, true, 0, 1),
            Arguments.of(SelectionType.RANDOM_ALL, SelectionType.INORDER_NAME, true, 0, 1),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, SelectionType.REVERSE_ORDER_FACTION, true, 0, 1),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, SelectionType.RANDOM_NOT_DISPLAYED, true, 0, 1),
        )
    }

    //endregion
}
