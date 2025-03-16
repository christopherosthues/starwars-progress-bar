package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.*
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.ui.scale.JBUIScale
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.awt.GraphicsConfiguration
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.ImageIcon

class ResourceFactoryTests {
    //region createClassLoader tests
    // TODO tests for lightsabers

    @Test
    fun `createClassLoader should create class loader of StarWarsResourceLoader class`() {
        // Arrange
        val classLoader = StarWarsResourceLoader.javaClass.classLoader

        // Act
        val result = createClassLoader()

        // Assert
        assertAll(
            { assertEquals(classLoader, result) },
            { assertSame(classLoader, result) },
        )
    }

    //endregion

    //region parseFactionsFromJson tests

    @Test
    fun `parseFactionsFromJson should return empty factions if json provides empty factions`() {
        // Arrange

        // Act
        val result = parseFactionsFromJson("{\"factions\": []}")

        // Assert
        assertEquals(listOf<StarWarsFaction<StarWarsVehicle>>(), result.vehicles)
    }

    @Test
    fun `parseFactionsFromJson should return empty factions if json provides empty object`() {
        // Arrange

        // Act
        val result = parseFactionsFromJson("{}")

        // Assert
        assertEquals(listOf<StarWarsFaction<StarWarsVehicle>>(), result.vehicles)
    }

    @Test
    fun `parseFactionsFromJson should return empty factions if throw null pointer exception on empty json`() {
        // Arrange

        // Act and Assert
        val result = parseFactionsFromJson("")

        // Assert
        assertEquals(listOf<StarWarsFaction<StarWarsVehicle>>(), result.vehicles)
    }

    @Test
    fun `parseFactionsFromJson should return empty factions if throw json syntax exception on invalid json`() {
        // Arrange

        // Act and Assert
        val result = parseFactionsFromJson("[]")

        // Assert
        assertEquals(listOf<StarWarsFaction<StarWarsVehicle>>(), result.vehicles)
    }

    @Test
    fun `parseFactionsFromJson should return correct factions`() {
        // Arrange
        val vehicleFactions = listOf(
            StarWarsFaction("faction1", listOf(StarWarsVehicle("vehicle1", "brown", -4, -6, 2.5f))),
            StarWarsFaction(
                "faction2",
                listOf(
                    StarWarsVehicle("vehicle2", "blue", -6, -6, 2.5f),
                    StarWarsVehicle("vehicle3", "blue", -6, -6, 2.5f),
                ),
            ),
        )
        val lightsaberFactions = listOf(
            StarWarsFaction(
                "faction3",
                listOf(Lightsaber("lightsaber1", "brown", 2.5f, isShoto = false, isDoubleBladed = false))
            ),
            StarWarsFaction(
                "faction2", listOf(
                    Lightsaber("lightsaber2", "blue", 2.5f, isShoto = false, isDoubleBladed = true),
                    Lightsaber("lightsaber3", "blue", 2.5f, isShoto = true, isDoubleBladed = false),
                )
            )
        )

        // Act and Assert
        val json = """
{
    "lightsabers": [
        {
            "data": [
                {
                    "type": "Lightsaber",
                    "id": "lightsaber1",
                    "bladeColor": "brown",
                    "velocity": 2.5,
                    "isShoto": false,
                    "isDoubleBladed": false
                }
            ],
            "id": "faction3"
        },
        {
            "data": [
                {
                    "type": "Lightsaber",
                    "id": "lightsaber2",
                    "bladeColor": "blue",
                    "velocity": 2.5,
                    "isShoto": false,
                    "isDoubleBladed": true
                },
                {
                    "type": "Lightsaber",
                    "id": "lightsaber3",
                    "bladeColor": "blue",
                    "velocity": 2.5,
                    "isShoto": true,
                    "isDoubleBladed": false
                }
            ],
            "id": "faction2"
        }
    ],
    "vehicles": [
        {
            "data": [
                {
                    "type": "StarWarsVehicle",
                    "id": "vehicle1",
                    "ionEngine": "brown",
                    "velocity": 2.5,
                    "xShift": -4,
                    "yShift": -6
                }
            ],
            "id": "faction1"
        },
        {
            "data": [
                {
                    "type": "StarWarsVehicle",
                    "id": "vehicle2",
                    "ionEngine": "blue",
                    "velocity": 2.5,
                    "xShift": -6,
                    "yShift": -6
                },
                {
                    "type": "StarWarsVehicle",
                    "id": "vehicle3",
                    "ionEngine": "blue",
                    "velocity": 2.5,
                    "xShift": -6,
                    "yShift": -6
                }
            ],
            "id": "faction2"
        }
    ]
}
    """
        val result = parseFactionsFromJson(json)

        // Assert
        val resultFactions = result.vehicles
        val resultLightsabers = result.lightsabers
        assertAll(
            { assertEquals(vehicleFactions, resultFactions) },
            { assertEquals(vehicleFactions.size, resultFactions.size) },
            { assertEquals(vehicleFactions[0], resultFactions[0]) },
            { assertEquals(vehicleFactions[1], resultFactions[1]) },
            { assertEquals(lightsaberFactions, resultLightsabers) },
            { assertEquals(lightsaberFactions.size, resultLightsabers.size) },
            { assertEquals(lightsaberFactions[0], resultLightsabers[0]) },
            { assertEquals(lightsaberFactions[1], resultLightsabers[1]) },
        )
    }

    //endregion

    //region createScaledEmptyImageIcon

    @Test
    fun `createScaledEmptyImageIcon should return scaled image icon`() {
        // Arrange
        val size = 10

        // Act
        val result = createScaledEmptyImageIcon(size)

        // Assert
        assertAll(
            { assertEquals(size, result.iconWidth) },
            { assertEquals(size, result.iconHeight) },
            { assertInstanceOf(ImageIcon::class.java, result) },
            { assertInstanceOf(BufferedImage::class.java, result.image) },
            { assertEquals(size, result.image.getHeight { _, _, _, _, _, _ -> false }) },
            { assertEquals(size, result.image.getWidth { _, _, _, _, _, _ -> false }) },
        )
    }

    @Test
    fun `createScaledEmptyImageIcon should return scaled image icon from buffered image`() {
        // Arrange
        mockkStatic(::createEmptyBufferedImage)
        val size = 10
        val bufferedImageMock = mockk<BufferedImage>(relaxed = true)
        every { createEmptyBufferedImage(any(), any()) } returns bufferedImageMock

        // Act
        val result = createScaledEmptyImageIcon(size)

        // Assert
        assertAll(
            { assertEquals(bufferedImageMock, result.image) },
            { assertSame(bufferedImageMock, result.image) },
        )
        verify(exactly = 1) { createEmptyBufferedImage(size, size) }

        unmockkAll()
    }

    //endregion

    //region

    @Test
    fun `createEmptyImageIconFromBufferedImage should return empty icon image of specified size`() {
        // Arrange
        val size = 10

        // Act
        val result = createEmptyImageIconFromBufferedImage(size)

        // Assert
        assertAll(
            { assertEquals(size, result.iconWidth) },
            { assertEquals(size, result.iconHeight) },
            { assertInstanceOf(ImageIcon::class.java, result) },
            { assertInstanceOf(BufferedImage::class.java, result.image) },
            { assertEquals(size, result.image.getHeight { _, _, _, _, _, _ -> false }) },
            { assertEquals(size, result.image.getWidth { _, _, _, _, _, _ -> false }) },
        )
    }

    //endregion

    //region createImageIconFromImage tests

    @Test
    fun `createImageIconFromImage should return image icon created from provided image`() {
        // Arrange
        val size = 10
        val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)

        // Act
        val result = createImageIconFromImage(image)

        // Assert
        assertAll(
            { assertEquals(size, result.iconWidth) },
            { assertEquals(size, result.iconHeight) },
            { assertInstanceOf(ImageIcon::class.java, result) },
            { assertInstanceOf(BufferedImage::class.java, result.image) },
            { assertEquals(image, result.image) },
            { assertEquals(size, result.image.getHeight { _, _, _, _, _, _ -> false }) },
            { assertEquals(size, result.image.getWidth { _, _, _, _, _, _ -> false }) },
        )
    }

    //endregion

    //region createImageIconFromURL tests

    @Test
    @Disabled("Should only be executed locally")
    fun `createImageIconFromURL should return image icon created from provided url`() {
        // Arrange
        val size = 32
        val imageResource = "icons/vehicles/missing.png"

        val url = Optional.ofNullable(createClassLoader().getResource(imageResource))
            .orElseGet { createClassLoader().getResource("/$imageResource") }

        assertNotNull(url)
        if (url == null) {
            return
        }

        // Act
        val result = createImageIconFromURL(url)

        // Assert
        assertAll(
            { assertInstanceOf(ImageIcon::class.java, result) },
            { assertEquals(size, result.iconWidth) },
            { assertEquals(size, result.iconHeight) },
            { assertEquals(size, result.image.getHeight { _, _, _, _, _, _ -> false }) },
            { assertEquals(size, result.image.getWidth { _, _, _, _, _, _ -> false }) },
        )
    }

    //endregion

    //region createEmptyImageIcon tests

    @Test
    fun `createEmptyImageIcon should return default image icon`() {
        // Arrange
        val size = -1

        // Act
        val result = createEmptyImageIcon()

        // Assert
        assertAll(
            { assertInstanceOf(ImageIcon::class.java, result) },
            { assertEquals(size, result.iconWidth) },
            { assertEquals(size, result.iconHeight) },
            { assertNull(result.image) },
        )
    }

    //endregion

    //region createEmptyTranslucentBufferedImage tests

    @Test
    fun `createEmptyTranslucentBufferedImage should return created buffered image`() {
        // Arrange
        val width = 5
        val height = 2
        val gc: GraphicsConfiguration? = null
        val scalingFactor = JBUIScale.sysScale(gc)

        // Act
        val result = createEmptyTranslucentBufferedImage(width, height)

        // Assert
        assertAll(
            { assertInstanceOf(BufferedImage::class.java, result) },
            { assertEquals((height * scalingFactor).toInt(), result.getHeight { _, _, _, _, _, _ -> false }, "width") },
            { assertEquals((width * scalingFactor).toInt(), result.getWidth { _, _, _, _, _, _ -> false }, "height") },
        )
    }

    //endregion

    //region createEmptyBufferedImage tests

    @Test
    fun `createEmptyBufferedImage should return created buffered image`() {
        // Arrange
        val width = 1
        val height = 2

        // Act
        val result = createEmptyBufferedImage(width, height)

        // Assert
        assertAll(
            { assertInstanceOf(BufferedImage::class.java, result) },
            { assertEquals(height, result.getHeight { _, _, _, _, _, _ -> false }) },
            { assertEquals(width, result.getWidth { _, _, _, _, _, _ -> false }) },
        )
    }

    //endregion
}
