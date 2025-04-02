package com.christopherosthues.starwarsprogressbar.models

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
            StarWarsVehicle("2", "b", 2, 3, 4f),
        )

        // Act
        val sut = StarWarsFaction("1", vehicles)

        // Assert
        assertAll(
            { assertEquals(vehicles, sut.data) },
            { assertSame(vehicles, sut.data) },
        )
    }

    @Test
    fun `constructor should set lightsabers to provided list`() {
        // Arrange
        val lightsabers = listOf(
            Lightsabers(
                "1",
                3f,
                isJarKai = false,
                listOf(Lightsaber(1, "a", isShoto = false, isDoubleBladed = true, yShift = 1, bladeSize = 8, xBlade = 0, yBlade = 0))
            ),
            Lightsabers(
                "2",
                4f,
                isJarKai = false,
                listOf(Lightsaber(1, "b", isShoto = true, isDoubleBladed = false, yShift = 2, bladeSize = 8, xBlade = 0, yBlade = 0))
            ),
        )

        // Act
        val sut = StarWarsFaction("1", lightsabers)

        // Assert
        assertAll(
            { assertEquals(lightsabers, sut.data) },
            { assertSame(lightsabers, sut.data) },
        )
    }
}
