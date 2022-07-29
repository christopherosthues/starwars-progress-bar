package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@TestFor(classes = [StarWarsFaction::class])
class StarWarsFactionTests {
    @Test
    fun `constructor should set vehicles to provided list`() {
        // Arrange
        val vehicles = listOf(
            StarWarsVehicle("1", "a", 1, 2, 3f),
            StarWarsVehicle("2", "b", 2, 3, 4f)
        )

        // Act
        val sut = StarWarsFaction("1", vehicles)

        // Assert
        assertAll(
            { assertEquals(vehicles, sut.vehicles) },
            { assertSame(vehicles, sut.vehicles) }
        )
    }

    @Test
    fun `localizationKey should return correct key`() {
        // Arrange
        val id = "123456789"
        val sut = StarWarsFaction(id, listOf())

        // Act
        val result = sut.localizationKey

        // Assert
        val expectedLocalizationKey = "${BundleConstants.FACTION}$id"
        assertEquals(expectedLocalizationKey, result)
    }
}
