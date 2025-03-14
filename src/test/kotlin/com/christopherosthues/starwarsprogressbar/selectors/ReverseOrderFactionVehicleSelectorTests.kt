package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
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

@TestFor(classes = [ReverseOrderFactionVehicleSelector::class])
class ReverseOrderFactionVehicleSelectorTests {
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
        val result = ReverseOrderFactionVehicleSelector.selectVehicle(mapOf(), defaultEnabled)

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
        val result = ReverseOrderFactionVehicleSelector.selectVehicle(
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
            ReverseOrderFactionVehicleSelector.selectVehicle(
                mapOf(
                    "2.1" to enabled,
                    "1.2" to enabled,
                    "1.3" to enabled,
                ),
                true,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectVehicle should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()

        // Act
        val result = ReverseOrderFactionVehicleSelector.selectVehicle(mapOf(), false)

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
            ReverseOrderFactionVehicleSelector.selectVehicle(
                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
                defaultEnabled,
            )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectVehicle should return vehicles in sorted order`() {
        // Arrange
        val vehicles = createStarWarsVehicles().toMutableList()
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsBundle.message(any()) } returnsArgument 0

        // Act
        var result = selectMultipleVehicles(vehicles, mapOf("2.1" to true, "1.2" to true, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[0], result[0]) },
            { Assertions.assertEquals(vehicles[2], result[1]) },
            { Assertions.assertEquals(vehicles[1], result[2]) },
        )

        // Arrange
        vehicles[0] = vehicles[1].also {
            vehicles[1] = vehicles[2].also {
                vehicles[2] = vehicles[0]
            }
        }

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("2.1" to true, "1.2" to true, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[1], result[1]) },
            { Assertions.assertEquals(vehicles[0], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("2.1" to true, "1.2" to false, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[1], result[1]) },
            { Assertions.assertEquals(vehicles[2], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("2.1" to true, "1.2" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[1], result[0]) },
            { Assertions.assertEquals(vehicles[2], result[1]) },
            { Assertions.assertEquals(vehicles[1], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("2.1" to true, "1.2" to false), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[2], result[1]) },
            { Assertions.assertEquals(vehicles[2], result[2]) },
        )

        // Arrange
        vehicles[0] = vehicles[2].also {
            vehicles[1] = vehicles[0].also {
                vehicles[2] = vehicles[1]
            }
        }
        vehicles[0].factionId = "1"
        ReverseOrderFactionVehicleSelector.selectVehicle(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            true,
        )
        ReverseOrderFactionVehicleSelector.selectVehicle(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            true,
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("1.1" to true, "1.2" to true, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[1], result[1]) },
            { Assertions.assertEquals(vehicles[0], result[2]) },
        )

        // Arrange
        vehicles[0] = vehicles[1].also {
            vehicles[1] = vehicles[2].also {
                vehicles[2] = vehicles[0]
            }
        }

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("1.1" to true, "1.2" to true, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[1], result[0]) },
            { Assertions.assertEquals(vehicles[0], result[1]) },
            { Assertions.assertEquals(vehicles[2], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("1.1" to true, "1.2" to false, "1.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[1], result[0]) },
            { Assertions.assertEquals(vehicles[2], result[1]) },
            { Assertions.assertEquals(vehicles[1], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("1.1" to true, "1.2" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[1], result[1]) },
            { Assertions.assertEquals(vehicles[2], result[2]) },
        )

        // Act
        result = selectMultipleVehicles(vehicles, mapOf("1.1" to true, "1.2" to false), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(vehicles[2], result[0]) },
            { Assertions.assertEquals(vehicles[2], result[1]) },
            { Assertions.assertEquals(vehicles[2], result[2]) },
        )
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

    private fun selectMultipleVehicles(
        vehicles: MutableList<StarWarsVehicle>,
        enabledVehicles: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): List<StarWarsVehicle> {
        val result = mutableListOf<StarWarsVehicle>()
        for (i in vehicles.indices) {
            result.add(
                ReverseOrderFactionVehicleSelector.selectVehicle(
                    enabledVehicles,
                    defaultEnabled,
                ),
            )
        }

        return result
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
