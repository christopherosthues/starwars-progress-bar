package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.State
import com.intellij.util.xmlb.XmlSerializerUtil
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@TestFor(classes = [StarWarsPersistentStateComponent::class])
class StarWarsPersistentStateComponentTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(FactionHolder)

        every { FactionHolder.updateFactions(any() as List<StarWarsFaction>) } just runs
        every { FactionHolder.factions } returns mockk(relaxed = true)
        every { FactionHolder.missingVehicle } returns mockk(relaxed = true)
        every { FactionHolder.defaultFactions } returns mockk(relaxed = true)
        every { FactionHolder.defaultVehicles } returns listOf()
    }

    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `constructor should initialize state with default values`() {
        // Arrange
        val vehicles = listOf(
            StarWarsVehicle("1", "blue", 0, 0, 0f),
            StarWarsVehicle("2", "green", 0, 0, 0f),
            StarWarsVehicle("3", "red", 0, 0, 0f)
        )
        every { FactionHolder.defaultVehicles } returns vehicles
        val sut = StarWarsPersistentStateComponent()

        // Act
        val result = sut.state

        // Assert
        assertAll(
            { assertNotNull(result) },
            {
                val vehiclesEnabled = result!!.vehiclesEnabled
                assertAll(
                    { assertEquals(3, vehiclesEnabled.count()) },
                    { assertEquals(true, vehiclesEnabled["1"]) },
                    { assertEquals(true, vehiclesEnabled["2"]) },
                    { assertEquals(true, vehiclesEnabled["3"]) }
                )
            },
            { assertTrue(result!!.showVehicle) },
            { assertFalse(result!!.showVehicleNames) },
            { assertTrue(result!!.showToolTips) },
            { assertFalse(result!!.showFactionCrests) },
            { assertFalse(result!!.sameVehicleVelocity) },
            { assertTrue(result!!.enableNewVehicles) },
            { assertFalse(result!!.solidProgressBarColor) },
            { assertFalse(result!!.drawSilhouettes) },
            { assertFalse(result!!.changeVehicleAfterPass) },
            { assertEquals(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, result!!.numberOfPassesUntilVehicleChange) },
            { assertEquals("", result!!.version) }
        )
    }

    @Test
    fun `loadState should return copy state`() {
        // Arrange
        val sut = StarWarsPersistentStateComponent()
        val expectedVersion = "1.0.0"
        val expectedVehiclesEnabled = mapOf("1" to false, "2" to true, "3" to false, "4" to false, "5" to true)
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = expectedVehiclesEnabled
            showVehicle = false
            showVehicleNames = true
            showToolTips = false
            showFactionCrests = true
            sameVehicleVelocity = true
            enableNewVehicles = false
            solidProgressBarColor = true
            drawSilhouettes = true
            changeVehicleAfterPass = true
            numberOfPassesUntilVehicleChange = 3
            version = expectedVersion
        }

        mockkStatic(XmlSerializerUtil::class)

        every { XmlSerializerUtil.copyBean(any<StarWarsState>(), any()) } answers { callOriginal() }

        val initialState = sut.state
        assertInitialState(initialState)

        // Act
        sut.loadState(starWarsState)

        // Assert
        val result = sut.state
        assertAll(
            { assertNotNull(result) },
            { assertNotSame(starWarsState, result) },
            { assertEquals(starWarsState.vehiclesEnabled, result!!.vehiclesEnabled) },
            { assertEquals(starWarsState.showVehicle, result!!.showVehicle) },
            { assertEquals(starWarsState.showVehicleNames, result!!.showVehicleNames) },
            { assertEquals(starWarsState.showToolTips, result!!.showToolTips) },
            { assertEquals(starWarsState.showFactionCrests, result!!.showFactionCrests) },
            { assertEquals(starWarsState.sameVehicleVelocity, result!!.sameVehicleVelocity) },
            { assertEquals(starWarsState.enableNewVehicles, result!!.enableNewVehicles) },
            { assertEquals(starWarsState.solidProgressBarColor, result!!.solidProgressBarColor) },
            { assertEquals(starWarsState.drawSilhouettes, result!!.drawSilhouettes) },
            { assertEquals(starWarsState.changeVehicleAfterPass, result!!.changeVehicleAfterPass) },
            { assertEquals(starWarsState.numberOfPassesUntilVehicleChange, result!!.numberOfPassesUntilVehicleChange) },
            { assertEquals(starWarsState.version, result!!.version) },

            { assertEquals(expectedVehiclesEnabled, result!!.vehiclesEnabled) },
            { assertFalse(result!!.showVehicle) },
            { assertTrue(result!!.showVehicleNames) },
            { assertFalse(result!!.showToolTips) },
            { assertTrue(result!!.showFactionCrests) },
            { assertTrue(result!!.sameVehicleVelocity) },
            { assertFalse(result!!.enableNewVehicles) },
            { assertTrue(result!!.solidProgressBarColor) },
            { assertTrue(result!!.drawSilhouettes) },
            { assertTrue(result!!.changeVehicleAfterPass) },
            { assertEquals(3, result!!.numberOfPassesUntilVehicleChange) },
            { assertEquals(expectedVersion, result!!.version) }
        )

        verify(exactly = 1) { XmlSerializerUtil.copyBean(starWarsState, initialState!!) }
    }

    @Test
    fun `instance should return instance created by application manager`() {
        // Arrange
        val expectedResult = StarWarsPersistentStateComponent()
        mockkStatic(ApplicationManager::class)

        val applicationMock = mockk<Application>()

        every { ApplicationManager.getApplication() } returns applicationMock
        every { applicationMock.getService(StarWarsPersistentStateComponent::class.java) } returns expectedResult

        // Act
        val result = StarWarsPersistentStateComponent.instance

        // Assert
        assertAll(
            { assertEquals(expectedResult, result) },
            { assertSame(expectedResult, result) }
        )

        verify(exactly = 1) { ApplicationManager.getApplication() }
        verify(exactly = 1) { applicationMock.getService(StarWarsPersistentStateComponent::class.java) }
    }

    @Test
    fun `instance should return null if application manager returns null`() {
        // Arrange
        mockkStatic(ApplicationManager::class)

        val applicationMock = mockk<Application>()

        every { ApplicationManager.getApplication() } returns applicationMock
        every { applicationMock.getService(StarWarsPersistentStateComponent::class.java) } returns null

        // Act
        val result = StarWarsPersistentStateComponent.instance

        // Assert
        assertNull(result)

        verify(exactly = 1) { ApplicationManager.getApplication() }
        verify(exactly = 1) { applicationMock.getService(StarWarsPersistentStateComponent::class.java) }
    }

    @Test
    fun `class annotation parameters should be set correctly`() {
        // Arrange

        // Act
        val annotations = StarWarsPersistentStateComponent::class.annotations

        // Assert
        assertAll(
            { assertEquals(1, annotations.size) },
            { assertEquals(State::class, annotations.first().annotationClass) },
            { assertEquals("StarWarsProgress.xml", (annotations.first() as State).storages.first().value) },
            {
                assertEquals(
                    "com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent",
                    (annotations.first() as State).name
                )
            }
        )
    }

    //endregion

    //region Helper methods

    private fun assertInitialState(starWarsState: StarWarsState?) {
        assertAll(
            { assertNotNull(starWarsState) },
            { assertTrue(starWarsState!!.vehiclesEnabled.isEmpty()) },
            { assertTrue(starWarsState!!.showVehicle) },
            { assertFalse(starWarsState!!.showVehicleNames) },
            { assertTrue(starWarsState!!.showToolTips) },
            { assertFalse(starWarsState!!.showFactionCrests) },
            { assertFalse(starWarsState!!.sameVehicleVelocity) },
            { assertTrue(starWarsState!!.enableNewVehicles) },
            { assertFalse(starWarsState!!.solidProgressBarColor) },
            { assertFalse(starWarsState!!.drawSilhouettes) },
            { assertFalse(starWarsState!!.changeVehicleAfterPass) },
            { assertEquals(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, starWarsState!!.numberOfPassesUntilVehicleChange) },
            { assertEquals("", starWarsState!!.version) }
        )
    }

    //endregion
}
