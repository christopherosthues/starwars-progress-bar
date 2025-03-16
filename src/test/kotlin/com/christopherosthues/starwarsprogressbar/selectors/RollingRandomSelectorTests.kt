//package com.christopherosthues.starwarsprogressbar.selectors
//
//import com.christopherosthues.starwarsprogressbar.StarWarsBundle
//import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
//import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
//import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
//import com.christopherosthues.starwarsprogressbar.util.randomInt
//import com.intellij.idea.TestFor
//import io.mockk.every
//import io.mockk.mockkObject
//import io.mockk.mockkStatic
//import io.mockk.unmockkAll
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.Arguments
//import org.junit.jupiter.params.provider.MethodSource
//import org.junit.jupiter.params.provider.ValueSource
//import java.util.stream.Stream
//
//@TestFor(classes = [RollingRandomSelector::class])
//class RollingRandomSelectorTests {
//    //region Test lifecycle
//
//    @BeforeEach
//    fun setup() {
//        mockkStatic(StarWarsBundle::message)
//        mockkStatic(::randomInt)
//        mockkObject(StarWarsFactionHolder)
//        mockkObject(StarWarsPersistentStateComponent)
//
//        every { StarWarsFactionHolder.missingVehicle } returns missingVehicle
//    }
//
//    @AfterEach
//    fun tearDown() {
//        unmockkAll()
//    }
//
//    //endregion
//
//    //region Tests
//
//    @ParameterizedTest
//    @MethodSource("defaultEnabledValues")
//    fun `selectEntity should return missing vehicle if provided enabled vehicles and default vehicles are empty`(
//        defaultEnabled: Boolean,
//    ) {
//        // Arrange
//        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
//
//        // Act
//        val result = RollingRandomSelector.selectEntity(mapOf(), mapOf(), defaultEnabled)
//
//        // Assert
//        Assertions.assertEquals(missingVehicle, result)
//    }
//
//    @ParameterizedTest
//    @MethodSource("defaultEnabledValues")
//    fun `selectEntity should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty`(
//        defaultEnabled: Boolean,
//    ) {
//        // Arrange
//        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
//
//        // Act
//        val result = RollingRandomSelector.selectEntity(
//            mapOf("2.1" to true, "1.2" to false, "1.3" to true),
//            defaultEnabled,
//        )
//
//        // Assert
//        Assertions.assertEquals(missingVehicle, result)
//    }
//
//    @ParameterizedTest
//    @ValueSource(booleans = [true, false])
//    fun `selectEntity should return missing vehicle if provided enabled vehicles are not empty and default vehicles are empty and all values are true or false`(
//        enabled: Boolean,
//    ) {
//        // Arrange
//        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
//
//        // Act
//        val result =
//            RollingRandomSelector.selectEntity(
//                mapOf("2.1" to enabled, "1.2" to enabled, "1.3" to enabled),
//                true,
//            )
//
//        // Assert
//        Assertions.assertEquals(missingVehicle, result)
//    }
//
//    @Test
//    fun `selectEntity should return missing vehicle if provided enabled vehicles are empty and default vehicles are not empty and default enabled is false`() {
//        // Arrange
//        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
//
//        // Act
//        val result = RollingRandomSelector.selectEntity(mapOf(), false)
//
//        // Assert
//        Assertions.assertEquals(missingVehicle, result)
//    }
//
//    @ParameterizedTest
//    @MethodSource("defaultEnabledValues")
//    fun `selectEntity should return missing vehicle if provided enabled vehicles are all false and default vehicles are not empty`(
//        defaultEnabled: Boolean,
//    ) {
//        // Arrange
//        every { StarWarsFactionHolder.defaultVehicles } returns createStarWarsVehicles()
//
//        // Act
//        val result =
//            RollingRandomSelector.selectEntity(
//                mapOf("2.1" to false, "1.2" to false, "1.3" to false),
//                defaultEnabled,
//            )
//
//        // Assert
//        Assertions.assertEquals(missingVehicle, result)
//    }
//
//    @Test
//    fun `selectEntity should return random vehicles`() {
//        // Arrange
//        val vehicles = createStarWarsVehicles().toMutableList()
//        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
//        every { randomInt(any()) } returns 0
//
//        // Act
//        var result = mutableListOf<StarWarsVehicle>()
//        for (i in 0 until (2 * vehicles.size)) {
//            result.add(
//                RollingRandomSelector.selectEntity(
//                    mapOf("2.1" to true, "1.2" to true, "1.3" to true),
//                    true,
//                ),
//            )
//        }
//
//        // Assert
//        Assertions.assertAll(
//            { Assertions.assertEquals(6, result.size) },
//            { Assertions.assertEquals(vehicles[0], result[0]) },
//            { Assertions.assertEquals(vehicles[1], result[1]) },
//            { Assertions.assertEquals(vehicles[2], result[2]) },
//            { Assertions.assertEquals(vehicles[0], result[3]) },
//            { Assertions.assertEquals(vehicles[1], result[4]) },
//            { Assertions.assertEquals(vehicles[2], result[5]) },
//        )
//
//        // Arrange
//        vehicles[0] = vehicles[2].also {
//            vehicles[2] = vehicles[0]
//        }
//
//        // Act
//        result = mutableListOf()
//        for (i in 0 until (2 * vehicles.size)) {
//            result.add(
//                RollingRandomSelector.selectEntity(
//                    mapOf("2.1" to true, "1.2" to true, "1.3" to true),
//                    true,
//                ),
//            )
//        }
//
//        // Assert
//        Assertions.assertAll(
//            { Assertions.assertEquals(6, result.size) },
//            { Assertions.assertEquals(vehicles[0], result[0]) },
//            { Assertions.assertEquals(vehicles[1], result[1]) },
//            { Assertions.assertEquals(vehicles[2], result[2]) },
//            { Assertions.assertEquals(vehicles[0], result[3]) },
//            { Assertions.assertEquals(vehicles[1], result[4]) },
//            { Assertions.assertEquals(vehicles[2], result[5]) },
//        )
//
//        // Act
//        result = mutableListOf()
//        for (i in 0 until (2 * vehicles.size)) {
//            result.add(
//                RollingRandomSelector.selectEntity(
//                    mapOf("2.1" to true, "1.2" to false, "1.3" to true),
//                    true,
//                ),
//            )
//        }
//
//        // Assert
//        Assertions.assertAll(
//            { Assertions.assertEquals(6, result.size) },
//            { Assertions.assertEquals(vehicles[0], result[0]) },
//            { Assertions.assertEquals(vehicles[2], result[1]) },
//            { Assertions.assertEquals(vehicles[0], result[2]) },
//            { Assertions.assertEquals(vehicles[2], result[3]) },
//            { Assertions.assertEquals(vehicles[0], result[4]) },
//            { Assertions.assertEquals(vehicles[2], result[5]) },
//        )
//    }
//
//    //endregion
//
//    //region Helper methods
//
//    private fun createStarWarsVehicles(): List<StarWarsVehicle> {
//        val vehicles = listOf(
//            StarWarsVehicle("1", "a", 1, 1, 1f).apply { factionId = "2" },
//            StarWarsVehicle("2", "b", 2, 2, 2f).apply { factionId = "1" },
//            StarWarsVehicle("3", "c", 3, 3, 3f).apply { factionId = "1" },
//        )
//        for (vehicle in vehicles) {
//            every { StarWarsBundle.message(vehicle.localizationKey) } returns vehicle.id
//        }
//
//        return vehicles
//    }
//
//    //endregion
//
//    //region Test data
//
//    private val missingVehicle = StarWarsVehicle("missing", "green", 0, 0, 0f)
//
//    //endregion
//
//    //region Test case data
//
//    companion object {
//        @JvmStatic
//        fun defaultEnabledValues(): Stream<Arguments> = Stream.of(Arguments.of(true), Arguments.of(false))
//    }
//
//    //endregion
//}
