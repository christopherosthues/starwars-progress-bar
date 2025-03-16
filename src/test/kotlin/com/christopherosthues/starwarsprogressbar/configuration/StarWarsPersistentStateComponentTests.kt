package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_VEHICLE_SELECTOR
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import com.intellij.idea.TestFor
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
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
        mockkObject(StarWarsFactionHolder)

        every { StarWarsFactionHolder.updateFactions(any()) } just runs
        every { StarWarsFactionHolder.vehicleFactions } returns mockk(relaxed = true)
        every { StarWarsFactionHolder.lightsaberFactions } returns mockk(relaxed = true)
        every { StarWarsFactionHolder.missingVehicle } returns mockk(relaxed = true)
        every { StarWarsFactionHolder.defaultVehicleFactions } returns mockk(relaxed = true)
        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
        every { StarWarsFactionHolder.defaultLightsaberFactions } returns mockk(relaxed = true)
        every { StarWarsFactionHolder.defaultLightsabers } returns listOf()
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
            StarWarsVehicle("3", "red", 0, 0, 0f),
        )
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        val lightsabers = listOf(
            Lightsaber("4", "blue", 0f, isShoto = false, isDoubleBladed = false),
            Lightsaber("5", "green", 0f, isShoto = true, isDoubleBladed = false),
            Lightsaber("6", "red", 0f, isShoto = false, isDoubleBladed = true),
        )
        every { StarWarsFactionHolder.defaultVehicles } returns vehicles
        every { StarWarsFactionHolder.defaultLightsabers } returns lightsabers
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
                    { assertEquals(true, vehiclesEnabled["3"]) },
                )
            },
            {
                val lightsabersEnabled = result!!.lightsabersEnabled
                assertAll(
                    { assertEquals(3, lightsabersEnabled.count()) },
                    { assertEquals(true, lightsabersEnabled["4"]) },
                    { assertEquals(true, lightsabersEnabled["5"]) },
                    { assertEquals(true, lightsabersEnabled["6"]) },
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
            { assertEquals(DEFAULT_VEHICLE_SELECTOR, result!!.vehicleSelector) },
            { assertEquals(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, result!!.numberOfPassesUntilVehicleChange) },
            { assertEquals("", result!!.version) },
        )
    }

    @Test
    fun `loadState should return copy state`() {
        // Arrange
        val sut = StarWarsPersistentStateComponent()
        val expectedVersion = "2.0.0"
        val expectedVehiclesEnabled = mutableMapOf("1" to false, "2" to true, "3" to false, "4" to false, "5" to true)
        val expectedLightsabersEnabled = mutableMapOf("6" to false, "7" to true, "8" to false, "9" to false, "10" to true)
        val starWarsState = StarWarsState().apply {
            vehiclesEnabled = expectedVehiclesEnabled
            lightsabersEnabled = expectedLightsabersEnabled
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
            vehicleSelector = SelectionType.INORDER_FACTION
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
            { assertEquals(starWarsState.lightsabersEnabled, result!!.lightsabersEnabled) },
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
            { assertEquals(starWarsState.vehicleSelector, result!!.vehicleSelector) },

            { assertEquals(expectedVehiclesEnabled, result!!.vehiclesEnabled) },
            { assertEquals(expectedLightsabersEnabled, result!!.lightsabersEnabled) },
            { assertFalse(result!!.showVehicle) },
            { assertTrue(result!!.showVehicleNames) },
            { assertFalse(result!!.showToolTips) },
            { assertTrue(result!!.showFactionCrests) },
            { assertTrue(result!!.sameVehicleVelocity) },
            { assertFalse(result!!.enableNewVehicles) },
            { assertTrue(result!!.solidProgressBarColor) },
            { assertTrue(result!!.drawSilhouettes) },
            { assertTrue(result!!.changeVehicleAfterPass) },
            { assertEquals(SelectionType.INORDER_FACTION, result!!.vehicleSelector) },
            { assertEquals(3, result!!.numberOfPassesUntilVehicleChange) },
            { assertEquals(expectedVersion, result!!.version) },
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
            { assertSame(expectedResult, result) },
        )

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
            { assertEquals(2, annotations.size) },
            { assertEquals(Service::class, annotations.first().annotationClass) },
            { assertEquals(State::class, annotations[1].annotationClass) },
            { assertEquals("StarWarsProgress.xml", (annotations[1] as State).storages.first().value) },
            {
                assertEquals(
                    "com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent",
                    (annotations[1] as State).name,
                )
            },
        )
    }

    //endregion

    //region Helper methods

    private fun assertInitialState(starWarsState: StarWarsState?) {
        assertAll(
            { assertNotNull(starWarsState) },
            { assertTrue(starWarsState!!.vehiclesEnabled.isEmpty()) },
            { assertTrue(starWarsState!!.lightsabersEnabled.isEmpty()) },
            { assertTrue(starWarsState!!.showVehicle) },
            { assertFalse(starWarsState!!.showVehicleNames) },
            { assertTrue(starWarsState!!.showToolTips) },
            { assertFalse(starWarsState!!.showFactionCrests) },
            { assertFalse(starWarsState!!.sameVehicleVelocity) },
            { assertTrue(starWarsState!!.enableNewVehicles) },
            { assertFalse(starWarsState!!.solidProgressBarColor) },
            { assertFalse(starWarsState!!.drawSilhouettes) },
            { assertFalse(starWarsState!!.changeVehicleAfterPass) },
            { assertEquals(DEFAULT_VEHICLE_SELECTOR, starWarsState!!.vehicleSelector) },
            { assertEquals(DEFAULT_NUMBER_OF_PASSES_UNTIL_VEHICLE_CHANGE, starWarsState!!.numberOfPassesUntilVehicleChange) },
            { assertEquals("", starWarsState!!.version) },
        )
    }

    //endregion
}
