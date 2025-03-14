package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.ui.IonEngineColor
import com.intellij.idea.TestFor
import com.intellij.ui.JBColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@TestFor(classes = [StarWarsVehicle::class])
class StarWarsVehicleTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(IonEngineColor)

        every { IonEngineColor.colors } returns mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `constructor should set properties correctly`() {
        // Arrange
        val id = "1"
        val ionEngineColor = "a"
        val xShift = 1
        val yShift = 2
        val velocity = 3f

        // Act
        val sut = StarWarsVehicle(id, ionEngineColor, xShift, yShift, velocity)

        // Assert
        assertAll(
            { assertEquals(id, sut.id) },
            { assertEquals(ionEngineColor, sut.ionEngine) },
            { assertEquals(xShift, sut.xShift) },
            { assertEquals(yShift, sut.yShift) },
            { assertEquals(velocity, sut.velocity) },
            { assertEquals("", sut.factionId) },
        )
    }

    @Test
    fun `factionId should return correct faction id if set`() {
        // Arrange
        val factionId = "2"
        val sut = StarWarsVehicle("1", "a", 1, 2, 3f)

        // Act
        sut.factionId = factionId

        // Assert
        assertEquals(factionId, sut.factionId)
    }

    @Test
    fun `fileName should return correct file name for vehicle if faction id is set`() {
        // Arrange
        val factionId = "faction"
        val id = "123456789"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)
        sut.factionId = factionId

        // Act
        val result = sut.fileName

        // Assert
        val expectedFileName = "$factionId/$id"
        assertEquals(expectedFileName, result)
    }

    @Test
    fun `fileName should return correct file name for vehicle if faction id is not set`() {
        // Arrange
        val id = "123456789"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)

        // Act
        val result = sut.fileName

        // Assert
        assertEquals(id, result)
    }

    @Test
    fun `vehicleId should return correct id for vehicle if faction id is set`() {
        // Arrange
        val factionId = "faction"
        val id = "123456789"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)
        sut.factionId = factionId

        // Act
        val result = sut.vehicleId

        // Assert
        val expectedVehicleId = "$factionId.$id"
        assertEquals(expectedVehicleId, result)
    }

    @Test
    fun `vehicleId should return correct id for vehicle if faction id is not set`() {
        // Arrange
        val id = "123456789"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)

        // Act
        val result = sut.vehicleId

        // Assert
        assertEquals(id, result)
    }

    @Test
    fun `localizationKey should return correct localization key for vehicle if faction is empty`() {
        // Arrange
        val id = "123456789"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)

        // Act
        val result = sut.localizationKey

        // Assert
        val expectedLocalizationKey = "${BundleConstants.VEHICLES}$id"
        assertEquals(expectedLocalizationKey, result)
    }

    @Test
    fun `localizationKey should return correct localization key for vehicle if faction is not empty`() {
        // Arrange
        val id = "123456789"
        val faction = "faction"
        val sut = StarWarsVehicle(id, "a", 1, 2, 3f)
        sut.factionId = faction

        // Act
        val result = sut.localizationKey

        // Assert
        val expectedLocalizationKey = "${BundleConstants.VEHICLES}$faction.$id"
        assertEquals(expectedLocalizationKey, result)
    }

    @Test
    fun `color should return correct color if color is defined`() {
        // Arrange
        val ionEngine = "a"
        val colorMock = mockk<JBColor>()
        every { IonEngineColor.colors } returns mapOf(ionEngine to colorMock)
        val id = "123456789"
        val sut = StarWarsVehicle(id, ionEngine, 1, 2, 3f)

        // Act
        val result = sut.color

        // Assert
        assertEquals(colorMock, result)
    }

    @Test
    fun `color should return blue color if color is not defined`() {
        // Arrange
        val ionEngine = "a"
        val colorMock = mockk<JBColor>()
        every { IonEngineColor.colors } returns mapOf(ionEngine to colorMock)
        val id = "123456789"
        val sut = StarWarsVehicle(id, "b", 1, 2, 3f)

        // Act
        val result = sut.color

        // Assert
        assertEquals(IonEngineColor.BlueEngine, result)
    }

    @Test
    fun `missingVehicle should return missing vehicle`() {
        // Arrange

        // Act
        val result = StarWarsVehicle.missingVehicle

        // Assert
        assertAll(
            { assertEquals("missing", result.id) },
            { assertEquals("missing", result.vehicleId) },
            { assertEquals("green", result.ionEngine) },
            { assertEquals(-4, result.xShift) },
            { assertEquals(-6, result.yShift) },
            { assertEquals(2.5f, result.velocity) },
        )
    }

    //endregion
}
