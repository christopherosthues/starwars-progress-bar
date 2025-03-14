package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicleFaction
import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@TestFor(classes = [StarWarsVehicleFaction::class])
class StarWarsVehicleFactionTests {
    @Test
    fun `constructor should set vehicles to provided list`() {
        // Arrange
        val vehicles = listOf(
            StarWarsVehicle("1", "a", 1, 2, 3f),
            StarWarsVehicle("2", "b", 2, 3, 4f),
        )

        // Act
        val sut = StarWarsVehicleFaction("1", vehicles)

        // Assert
        assertAll(
            { assertEquals(vehicles, sut.vehicles) },
            { assertSame(vehicles, sut.vehicles) },
        )
    }

    @Test
    fun `localizationKey should return correct key`() {
        // Arrange
        val id = "123456789"
        val sut = StarWarsVehicleFaction(id, listOf())

        // Act
        val result = sut.localizationKey

        // Assert
        val expectedLocalizationKey = "${BundleConstants.VEHICLES_FACTION}$id"
        assertEquals(expectedLocalizationKey, result)
    }
}
