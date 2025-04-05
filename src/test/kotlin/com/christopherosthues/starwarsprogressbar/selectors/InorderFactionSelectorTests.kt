package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
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

@TestFor(classes = [InorderFactionSelector::class])
class InorderFactionSelectorTests {
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
        InorderFactionSelector.reset()
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled vehicles and default vehicles are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = InorderFactionSelector.selectEntity(mapOf(), mapOf(), defaultEnabled)

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
        val result = InorderFactionSelector.selectEntity(
            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
            mapOf(),
            defaultEnabled,
        )

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @ParameterizedTest
    @MethodSource("defaultEnabledValues")
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are not empty and default vehicles and lightsabers are empty`(
        defaultEnabled: Boolean,
    ) {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()

        // Act
        val result = InorderFactionSelector.selectEntity(
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
        val result = InorderFactionSelector.selectEntity(
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
            InorderFactionSelector.selectEntity(
                mapOf("2.1" to enabled, "1.2" to enabled, "1.3" to enabled),
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
            InorderFactionSelector.selectEntity(
                mapOf(),
                mapOf("4.1" to enabled, "3.2" to enabled, "3.3" to enabled),
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
            InorderFactionSelector.selectEntity(
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
        val result = InorderFactionSelector.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled lightsabers are empty and default lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = InorderFactionSelector.selectEntity(mapOf(), mapOf(), false)

        // Assert
        Assertions.assertEquals(missingVehicle, result)
    }

    @Test
    fun `selectEntity should return missing vehicle if provided enabled vehicles and lightsabers are empty and default vehicles and lightsabers are not empty and default enabled is false`() {
        // Arrange
        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
        every { StarWarsFactionHolder.defaultLightsabers } returns createLightsabers()

        // Act
        val result = InorderFactionSelector.selectEntity(mapOf(), mapOf(), false)

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
            InorderFactionSelector.selectEntity(
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
            InorderFactionSelector.selectEntity(
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
            InorderFactionSelector.selectEntity(
                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
                mapOf("4.1" to false, "3.2" to false, "3.3" to false),
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
        entities[0].factionId = "2"
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
        every { StarWarsBundle.message(any()) } returnsArgument 0

        // Act
        var result = selectMultipleEntities(entities, mapOf("2.1" to true, "1.2" to true, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[0], result[2]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }

        // Act
        result = selectMultipleEntities(entities, mapOf("2.1" to true, "1.2" to true, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[0], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("2.1" to true, "1.2" to false, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("2.1" to true, "1.2" to false), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("2.1" to true, "1.2" to false), mapOf(), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Arrange
        entities[0] = entities[2].also {
            entities[1] = entities[0].also {
                entities[2] = entities[1]
            }
        }
        entities[0].factionId = "1"
        InorderFactionSelector.selectEntity(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf(),
            true,
        )
        InorderFactionSelector.selectEntity(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf(),
            true,
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("1.1" to true, "1.2" to true, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[0], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }

        // Act
        result = selectMultipleEntities(entities, mapOf("1.1" to true, "1.2" to true, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[0], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("1.1" to true, "1.2" to false, "1.3" to true), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("1.1" to true, "1.2" to false), mapOf(), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf("1.1" to true, "1.2" to false), mapOf(), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )
    }

    @Test
    fun `selectEntity should return lightsabers in sorted order`() {
        // Arrange
        val lightsabers = createLightsabers().toMutableList()
        val entities: MutableList<StarWarsEntity> = lightsabers.toMutableList()
        entities[0].factionId = "4"
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { StarWarsBundle.message(any()) } returnsArgument 0

        // Act
        var result = selectMultipleEntities(entities, mapOf(), mapOf("4.1" to true, "3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[0], result[2]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("4.1" to true, "3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[0], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("4.1" to true, "3.2" to false, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("4.1" to true, "3.2" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("4.1" to true, "3.2" to false), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Arrange
        entities[0] = entities[2].also {
            entities[1] = entities[0].also {
                entities[2] = entities[1]
            }
        }
        entities[0].factionId = "3"
        InorderFactionSelector.selectEntity(
            mapOf(),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )
        InorderFactionSelector.selectEntity(
            mapOf(),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("3.1" to true, "3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[0], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("3.1" to true, "3.2" to true, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[0], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("3.1" to true, "3.2" to false, "3.3" to true), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("3.1" to true, "3.2" to false), true)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
        )

        // Act
        result = selectMultipleEntities(entities, mapOf(), mapOf("3.1" to true, "3.2" to false), false)

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(3, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
        )
    }

    @Test
    fun `selectEntity should return vehicles and lightsabers in sorted order`() {
        // Arrange
        val vehicles = createStarWarsVehicles().toMutableList()
        val lightsabers = createLightsabers().toMutableList()
        val entities: MutableList<StarWarsEntity> = (lightsabers + vehicles).toMutableList()
        entities[0].factionId = "4"
        entities[3].factionId = "2"
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
        every { StarWarsBundle.message(any()) } returnsArgument 0

        // Act
        var result = selectMultipleEntities(
            entities,
            mapOf("2.1" to true, "1.2" to true, "1.3" to true),
            mapOf("4.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[0], result[2]) },
            { Assertions.assertEquals(entities[4], result[3]) },
            { Assertions.assertEquals(entities[5], result[4]) },
            { Assertions.assertEquals(entities[3], result[5]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }
        entities[3] = entities[4].also {
            entities[4] = entities[5].also {
                entities[5] = entities[3]
            }
        }

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("2.1" to true, "1.2" to true, "1.3" to true),
            mapOf("4.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[0], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
            { Assertions.assertEquals(entities[3], result[3]) },
            { Assertions.assertEquals(entities[4], result[4]) },
            { Assertions.assertEquals(entities[5], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
            mapOf("4.1" to true, "3.2" to false, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[1], result[0]) },
            { Assertions.assertEquals(entities[2], result[1]) },
            { Assertions.assertEquals(entities[4], result[2]) },
            { Assertions.assertEquals(entities[5], result[3]) },
            { Assertions.assertEquals(entities[1], result[4]) },
            { Assertions.assertEquals(entities[2], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("2.1" to true, "1.2" to false),
            mapOf("4.1" to true, "3.2" to false),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[4], result[0]) },
            { Assertions.assertEquals(entities[5], result[1]) },
            { Assertions.assertEquals(entities[1], result[2]) },
            { Assertions.assertEquals(entities[2], result[3]) },
            { Assertions.assertEquals(entities[4], result[4]) },
            { Assertions.assertEquals(entities[5], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("2.1" to true, "1.2" to false),
            mapOf("4.1" to true, "3.2" to false),
            false,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[5], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
            { Assertions.assertEquals(entities[5], result[3]) },
            { Assertions.assertEquals(entities[2], result[4]) },
            { Assertions.assertEquals(entities[5], result[5]) },
        )

        // Arrange
        entities[0] = entities[2].also {
            entities[1] = entities[0].also {
                entities[2] = entities[1]
            }
        }
        entities[3] = entities[5].also {
            entities[4] = entities[3].also {
                entities[5] = entities[4]
            }
        }

        entities[0].factionId = "3"
        entities[3].factionId = "1"
        InorderFactionSelector.selectEntity(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )
        InorderFactionSelector.selectEntity(
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[4], result[0]) },
            { Assertions.assertEquals(entities[5], result[1]) },
            { Assertions.assertEquals(entities[0], result[2]) },
            { Assertions.assertEquals(entities[1], result[3]) },
            { Assertions.assertEquals(entities[2], result[4]) },
            { Assertions.assertEquals(entities[3], result[5]) },
        )

        // Arrange
        entities[0] = entities[1].also {
            entities[1] = entities[2].also {
                entities[2] = entities[0]
            }
        }
        entities[3] = entities[4].also {
            entities[4] = entities[5].also {
                entities[5] = entities[3]
            }
        }

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("1.1" to true, "1.2" to true, "1.3" to true),
            mapOf("3.1" to true, "3.2" to true, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[3], result[0]) },
            { Assertions.assertEquals(entities[4], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
            { Assertions.assertEquals(entities[0], result[3]) },
            { Assertions.assertEquals(entities[1], result[4]) },
            { Assertions.assertEquals(entities[5], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("1.1" to true, "1.2" to false, "1.3" to true),
            mapOf("3.1" to true, "3.2" to false, "3.3" to true),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[1], result[1]) },
            { Assertions.assertEquals(entities[5], result[2]) },
            { Assertions.assertEquals(entities[4], result[3]) },
            { Assertions.assertEquals(entities[2], result[4]) },
            { Assertions.assertEquals(entities[1], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("1.1" to true, "1.2" to false),
            mapOf("3.1" to true, "3.2" to false),
            true,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[5], result[0]) },
            { Assertions.assertEquals(entities[4], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
            { Assertions.assertEquals(entities[1], result[3]) },
            { Assertions.assertEquals(entities[5], result[4]) },
            { Assertions.assertEquals(entities[4], result[5]) },
        )

        // Act
        result = selectMultipleEntities(
            entities,
            mapOf("1.1" to true, "1.2" to false),
            mapOf("3.1" to true, "3.2" to false),
            false,
        )

        // Assert
        Assertions.assertAll(
            { Assertions.assertEquals(6, result.size) },
            { Assertions.assertEquals(entities[2], result[0]) },
            { Assertions.assertEquals(entities[5], result[1]) },
            { Assertions.assertEquals(entities[2], result[2]) },
            { Assertions.assertEquals(entities[5], result[3]) },
            { Assertions.assertEquals(entities[2], result[4]) },
            { Assertions.assertEquals(entities[5], result[5]) },
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

    private fun createLightsabers(): List<Lightsabers> {
        val lightsabers = listOf(
            Lightsabers(
                "1",
                4f,
                isJarKai = false,
                listOf(Lightsaber(1, "a", isShoto = false, isDoubleBladed = false, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0)),
            ).apply { factionId = "4" },
            Lightsabers(
                "2",
                5f,
                isJarKai = false,
                listOf(Lightsaber(1, "b", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0)),
            ).apply { factionId = "3" },
            Lightsabers(
                "3",
                6f,
                isJarKai = false,
                listOf(Lightsaber(1, "c", isShoto = false, isDoubleBladed = true, yShift = 3, bladeSize = 8, xBlade = 0, yBlade = 0)),
            ).apply { factionId = "3" },
        )
        for (lightsaber in lightsabers) {
            every { StarWarsBundle.message(lightsaber.localizationKey) } returns lightsaber.id
        }

        return lightsabers
    }

    private fun selectMultipleEntities(
        entities: MutableList<StarWarsEntity>,
        enabledVehicles: Map<String, Boolean>,
        enabledLightsabers: Map<String, Boolean>,
        defaultEnabled: Boolean,
    ): List<StarWarsEntity> {
        val result = mutableListOf<StarWarsEntity>()
        for (i in entities.indices) {
            result.add(
                InorderFactionSelector.selectEntity(
                    enabledVehicles,
                    enabledLightsabers,
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
