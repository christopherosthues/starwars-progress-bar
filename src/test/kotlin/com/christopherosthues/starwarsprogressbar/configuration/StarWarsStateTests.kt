package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW_VEHICLES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VEHICLE_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_VEHICLE_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_VEHICLE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@TestFor(classes = [StarWarsState::class])
class StarWarsStateTests {

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(FactionHolder)

        every { FactionHolder.defaultVehicles } returns listOf()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `default values should be set correctly`() {
        // Arrange
        val sut = StarWarsState()

        // Act and Assert
        assertDefaultValues(sut)
    }

    @Test
    fun `vehiclesEnabled should return map with all vehicle ids mapped to true`() {
        // Arrange
        setupDefaultVehicles()
        val sut = StarWarsState()

        // Act
        val result = sut.vehiclesEnabled

        // Assert
        assertVehiclesEnabled(result)
    }

    @Test
    fun `values should be updated on set`() {
        // Arrange
        val sut = StarWarsState()
        val showVehicle = false
        val showVehicleNames = true
        val showToolTips = false
        val showFactionCrests = true
        val sameVehicleVelocity = true
        val enableNewVehicles = false
        val solidProgressBarColor = true
        val drawSilhouettes = true
        val changeVehicleAfterPass = true
        val numberOfPassesUntilVehicleChange = 1
        val version = "1.0.0"

        assertDefaultValues(sut)

        // Act
        sut.vehiclesEnabled = mapOf("1" to false, "2" to true)
        sut.showVehicle = showVehicle
        sut.showVehicleNames = showVehicleNames
        sut.showToolTips = showToolTips
        sut.showFactionCrests = showFactionCrests
        sut.sameVehicleVelocity = sameVehicleVelocity
        sut.enableNewVehicles = enableNewVehicles
        sut.solidProgressBarColor = solidProgressBarColor
        sut.drawSilhouettes = drawSilhouettes
        sut.changeVehicleAfterPass = changeVehicleAfterPass
        sut.numberOfPassesUntilVehicleChange = numberOfPassesUntilVehicleChange
        sut.version = version

        // Assert
        assertAll(
            { assertEquals(showVehicle, sut.showVehicle) },
            { assertEquals(showVehicleNames, sut.showVehicleNames) },
            { assertEquals(showToolTips, sut.showToolTips) },
            { assertEquals(showFactionCrests, sut.showFactionCrests) },
            { assertEquals(sameVehicleVelocity, sut.sameVehicleVelocity) },
            { assertEquals(enableNewVehicles, sut.enableNewVehicles) },
            { assertEquals(solidProgressBarColor, sut.solidProgressBarColor) },
            { assertEquals(drawSilhouettes, sut.drawSilhouettes) },
            { assertEquals(changeVehicleAfterPass, sut.changeVehicleAfterPass) },
            { assertEquals(numberOfPassesUntilVehicleChange, sut.numberOfPassesUntilVehicleChange) },
            { assertEquals(version, sut.version) },
            { assertTrue(sut.vehiclesEnabled.isNotEmpty()) },
            { assertEquals(2, sut.vehiclesEnabled.size) },
            { assertFalse(sut.vehiclesEnabled["1"]!!) },
            { assertTrue(sut.vehiclesEnabled["2"]!!) }
        )
    }

    @Test
    fun `vehiclesEnabled should be updated on set`() {
        // Arrange
        setupDefaultVehicles()
        val sut = StarWarsState()

        assertVehiclesEnabled(sut.vehiclesEnabled)

        // Act
        sut.vehiclesEnabled = mapOf("1" to false, "2" to true)

        // Assert
        assertAll(
            { assertTrue(sut.vehiclesEnabled.isNotEmpty()) },
            { assertEquals(2, sut.vehiclesEnabled.size) },
            { assertFalse(sut.vehiclesEnabled["1"]!!) },
            { assertTrue(sut.vehiclesEnabled["2"]!!) }
        )
    }

    //endregion

    //region Helper methods

    private fun assertDefaultValues(sut: StarWarsState) {
        assertAll(
            { assertEquals(DEFAULT_SHOW_VEHICLE_NAMES, sut.showVehicleNames) },
            { assertEquals(DEFAULT_SHOW_VEHICLE, sut.showVehicle) },
            { assertEquals(DEFAULT_SHOW_TOOLTIPS, sut.showToolTips) },
            { assertEquals(DEFAULT_SHOW_FACTION_CRESTS, sut.showFactionCrests) },
            { assertEquals(DEFAULT_SAME_VEHICLE_VELOCITY, sut.sameVehicleVelocity) },
            { assertEquals(DEFAULT_ENABLE_NEW_VEHICLES, sut.enableNewVehicles) },
            { assertEquals(DEFAULT_SOLID_PROGRESS_BAR_COLOR, sut.solidProgressBarColor) },
            { assertEquals(DEFAULT_DRAW_SILHOUETTES, sut.drawSilhouettes) },
            { assertEquals(DEFAULT_CHANGE_VEHICLE_AFTER_PASS, sut.changeVehicleAfterPass) },
            { assertEquals(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, sut.numberOfPassesUntilVehicleChange) },
            { assertEquals("", sut.version) },
            { assertTrue(sut.vehiclesEnabled.isEmpty()) }
        )
    }

    private fun assertVehiclesEnabled(vehiclesEnabled: Map<String, Boolean>) {
        assertAll(
            { assertEquals(6, vehiclesEnabled.size) },
            { assertTrue(vehiclesEnabled.keys.containsAll(listOf("1", "2", "3", "4", "5", "6"))) },
            { assertTrue(vehiclesEnabled.values.all { v -> v }) }
        )
    }

    private fun setupDefaultVehicles() {
        every { FactionHolder.defaultVehicles } returns listOf(
            StarWarsVehicle("1", "a", 2, 3, 4f),
            StarWarsVehicle("2", "b", 3, 4, 5f),
            StarWarsVehicle("3", "c", 4, 5, 6f),
            StarWarsVehicle("4", "d", 5, 6, 7f),
            StarWarsVehicle("5", "e", 6, 7, 8f),
            StarWarsVehicle("6", "f", 7, 8, 9f)
        )
    }

    //endregion
}
