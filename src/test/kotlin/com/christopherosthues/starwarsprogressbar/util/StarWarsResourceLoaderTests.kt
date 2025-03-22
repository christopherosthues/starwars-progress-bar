package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.idea.TestFor
import com.intellij.openapi.util.IconLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import java.util.concurrent.ExecutionException
import java.util.stream.Stream
import javax.swing.Icon
import javax.swing.ImageIcon

@TestFor(classes = [StarWarsResourceLoader::class])
internal class StarWarsResourceLoaderTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(IconLoader::class)
        mockkStatic(::createClassLoader)
        mockkStatic(::readTextFromUrl)
        mockkStatic(::parseFactionsFromJson)
        mockkStatic(::createScaledEmptyImageIcon)
        mockkStatic(::createEmptyImageIconFromBufferedImage)
        mockkStatic(::createImageIconFromImage)
        mockkStatic(::createImageIconFromURL)
        mockkStatic(::createEmptyImageIcon)
        mockkStatic(::createEmptyTranslucentBufferedImage)
        mockkStatic(::createEmptyBufferedImage)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    //region getPluginIcon tests

    @Test
    fun `getPluginIcon should load plugin icon`() {
        // Arrange
        every { IconLoader.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass) } returns mockk(relaxed = true)

        // Act
        StarWarsResourceLoader.getPluginIcon()

        // Assert
        verifyOrder {
            IconLoader.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass)
        }
        verify(exactly = 1) { IconLoader.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass) }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0])
    fun `getPluginIcon should return scaled empty icon if height is less or equal to zero`(height: Int) {
        // Arrange
        setupPluginIcon(width, height)
        val imageIconMock = mockk<ImageIcon>(relaxed = true)
        every { createScaledEmptyImageIcon(scaledIconSize) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getPluginIcon()

        // Assert
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
        verify(exactly = 1) { createScaledEmptyImageIcon(scaledIconSize) }
        verify(exactly = 0) { createEmptyTranslucentBufferedImage(10, height) }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0])
    fun `getPluginIcon should return scaled empty icon if width is less or equal to zero`(width: Int) {
        // Arrange
        setupPluginIcon(width, height)
        val imageIconMock = mockk<ImageIcon>(relaxed = true)
        every { createScaledEmptyImageIcon(scaledIconSize) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getPluginIcon()

        // Assert
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
        verify(exactly = 1) { createScaledEmptyImageIcon(scaledIconSize) }
        verify(exactly = 0) { createEmptyTranslucentBufferedImage(width, 10) }
    }

    @Test
    fun `getPluginIcon should not return empty scaled icon if width and height are greater than zero`() {
        // Arrange
        setupPluginIcon(1, 1)
        val imageIconMock = mockk<ImageIcon>(relaxed = true)
        every { createScaledEmptyImageIcon(scaledIconSize) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getPluginIcon()

        // Assert
        assertAll(
            { assertNotEquals(imageIconMock, result) },
            { assertNotSame(imageIconMock, result) },
        )
        verify(exactly = 0) { createScaledEmptyImageIcon(scaledIconSize) }
    }

    @Test
    fun `getPluginIcon should return scaled icon if width and height are greater than zero`() {
        // Arrange
        val iconMock = setupPluginIcon(width, height)
        val imageIconMock = mockk<ImageIcon>(relaxed = true)
        val bufferedImageMock = mockk<BufferedImage>(relaxed = true)
        val imageMock = mockk<Image>(relaxed = true)
        val graphicsMock = mockk<Graphics2D>(relaxed = true)
        every { createEmptyTranslucentBufferedImage(width, height) } returns bufferedImageMock
        every { bufferedImageMock.createGraphics() } returns graphicsMock
        every {
            bufferedImageMock.getScaledInstance(
                scaledIconSize,
                scaledIconSize,
                Image.SCALE_SMOOTH,
            )
        } returns imageMock
        every { createImageIconFromImage(imageMock) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getPluginIcon()

        // Assert
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
        verify(exactly = 0) { createScaledEmptyImageIcon(scaledIconSize) }
        verifyOrder {
            createEmptyTranslucentBufferedImage(width, height)
            bufferedImageMock.createGraphics()
            iconMock.paintIcon(null, graphicsMock, 0, 0)
            graphicsMock.dispose()
            bufferedImageMock.getScaledInstance(scaledIconSize, scaledIconSize, Image.SCALE_SMOOTH)
            createImageIconFromImage(imageMock)
        }

        verify(exactly = 1) { createEmptyTranslucentBufferedImage(width, height) }
        verify(exactly = 1) { bufferedImageMock.createGraphics() }
        verify(exactly = 1) { iconMock.paintIcon(null, graphicsMock, 0, 0) }
        verify(exactly = 1) { graphicsMock.dispose() }
        verify(exactly = 1) { bufferedImageMock.getScaledInstance(scaledIconSize, scaledIconSize, Image.SCALE_SMOOTH) }
        verify(exactly = 1) { createImageIconFromImage(imageMock) }
    }

    //endregion

    //region getFactionLogo tests

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return icon converted to buffered image`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName, "vehicles")
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        // Assert
        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return cached icon if icon was queried before`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction_cached"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName, "vehicles")
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)

        // Act
        result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        // Assert
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) },
        )
        verifyImage(classLoaderMock, resourceName, url)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return icon converted to buffered image if resource path starts with a slash`(
        isLargeIcon: Boolean,
    ) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction2"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName, "vehicles")
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        // Assert
        verifyImageForSlashPath(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return empty image icon if resource does not exists`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction3"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName, "vehicles")
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        // Assert
        verifyEmptyImageForMissingResource(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return empty image icon if an execution exception is thrown`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction4"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName, "vehicles")
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo("vehicles", factionName, isLargeIcon)

        // Assert
        verifyEmptyImageAfterException(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    //endregion

    //region getIcon tests

    @Test
    fun `getIcon should return icon for provided vehicle name`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getIcon should return cached icon if icon was queried before`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle_cached"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getIcon(vehicleName)

        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)

        // Act
        result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getIcon should return correct icon for provided vehicle name if resource path starts with a slash`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle2"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyIconForSlashPath(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getIcon should return empty image icon if resource does not exists`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle3"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyEmptyIconForMissingResource(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getIcon should return empty image icon if an execution exception is thrown`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle4"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyEmptyIconAfterException(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    //endregion

    //region getImage tests

    @Test
    fun `getImage should return icon for provided vehicle name`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImage"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getImage(vehicleName)

        // Assert
        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getImage should return cached icon if icon was queried before`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImage_cached"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getImage(vehicleName)

        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)

        // Act
        result = StarWarsResourceLoader.getImage(vehicleName)

        // Assert
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) },
        )
        verifyImage(classLoaderMock, resourceName, url)
    }

    @Test
    fun `getImage should return correct icon for provided vehicle name if resource path starts with a slash`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImage2"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getImage(vehicleName)

        // Assert
        verifyImageForSlashPath(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getImage should return empty image icon if resource does not exists`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImage3"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getImage(vehicleName)

        // Assert
        verifyEmptyImageForMissingResource(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getImage should return empty image icon if an execution exception is thrown`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImage4"
        val resourceName = setupVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getImage(vehicleName)

        // Assert
        verifyEmptyImageAfterException(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    //endregion

    //region getReversedImage tests

    @Test
    fun `getReversedImage should return icon for provided vehicle name`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImageReversed"
        val resourceName = setupReversedVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedImage(vehicleName)

        // Assert
        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getReversedImage should return cached icon if icon was queried before`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImageReversed_cached"
        val resourceName = setupReversedVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getReversedImage(vehicleName)

        verifyImage(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)

        // Act
        result = StarWarsResourceLoader.getReversedImage(vehicleName)

        // Assert
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) },
        )
        verifyImage(classLoaderMock, resourceName, url)
    }

    @Test
    fun `getReversedImage should return correct icon for provided vehicle name if resource path starts with a slash`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImageReversed2"
        val resourceName = setupReversedVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedImage(vehicleName)

        // Assert
        verifyImageForSlashPath(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getReversedImage should return empty image icon if resource does not exists`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImageReversed3"
        val resourceName = setupReversedVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedImage(vehicleName)

        // Assert
        verifyEmptyImageForMissingResource(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @Test
    fun `getReversedImage should return empty image icon if an execution exception is thrown`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicleImageReversed4"
        val resourceName = setupReversedVehicleImageResourceName(vehicleName)
        val url = setupResourceUrl()
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedImage(vehicleName)

        // Assert
        verifyEmptyImageAfterException(classLoaderMock, resourceName, url)
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    //endregion

    //region loadFactions tests

    @ParameterizedTest
    @MethodSource("factionValues")
    fun `loadFactions should return loaded factions`(starWarsFactions: StarWarsFactions) {
        // Arrange
        val factionJson = ""
        val url = setupUrlWithText(factionJson)
        val classLoaderMock = setupClassLoader(factionFileName, url, "/$factionFileName", null)
        every { parseFactionsFromJson(factionJson) } returns starWarsFactions

        // Act
        val result = StarWarsResourceLoader.loadFactions()

        // Assert
        verifyLoadedFactions(classLoaderMock, url, factionJson, starWarsFactions, result)
    }

    @ParameterizedTest
    @MethodSource("factionValues")
    fun `loadFactions should return loaded factions if resource path starts with a slash`(
        starWarsFactions: StarWarsFactions,
    ) {
        // Arrange
        val factionJson = ""
        val url = setupUrlWithText(factionJson)
        val classLoaderMock = setupClassLoader(factionFileName, null, "/$factionFileName", url)
        every { parseFactionsFromJson(factionJson) } returns starWarsFactions

        // Act
        val result = StarWarsResourceLoader.loadFactions()

        // Assert
        verifyLoadedFactionsPathWithSlash(classLoaderMock, url, factionJson, starWarsFactions, result)
    }

    @ParameterizedTest
    @MethodSource("factionValues")
    fun `loadFactions should return loaded factions if resource does not exists`(starWarsFactions: StarWarsFactions) {
        // Arrange
        val factionJson = ""
        val url = setupUrlWithText(factionJson)
        val classLoaderMock = setupClassLoader(factionFileName, null, "/$factionFileName", null)
        every { parseFactionsFromJson(emptyFactionsJson) } returns starWarsFactions

        // Act
        val result = StarWarsResourceLoader.loadFactions()

        // Assert
        verifyLoadedFactionsForMissingResource(classLoaderMock, url, starWarsFactions, result)
    }

    //endregion

    //endregion

    //region Helper methods

    private fun setupPluginIcon(width: Int, height: Int): Icon {
        val iconMock = mockk<Icon>(relaxed = true)
        every { IconLoader.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass) } returns iconMock
        every { iconMock.iconWidth } returns width
        every { iconMock.iconHeight } returns height

        return iconMock
    }

    private fun setupIconImage(): ImageIcon {
        val imageIconMock = mockk<ImageIcon>(relaxed = true)
        every { imageIconMock.iconWidth } returns width
        every { imageIconMock.iconHeight } returns height

        return imageIconMock
    }

    private fun setupFactionResourceName(isLargeIcon: Boolean, factionName: String, type: String): String {
        val largeIcon = if (isLargeIcon) "@2x" else ""
        return "icons/$type/$factionName/logo$largeIcon.png"
    }

    private fun setupVehicleImageResourceName(vehicleName: String): String = "icons/$vehicleName.png"

    private fun setupReversedVehicleImageResourceName(vehicleName: String): String = "icons/${vehicleName}_r.png"

    private fun setupResourceUrl(): URL = mockk(relaxed = true)

    private fun setupImageScaling(): Pair<BufferedImage, Graphics2D> {
        val bufferedImageMock = mockk<BufferedImage>(relaxed = true)
        val graphicsMock = mockk<Graphics2D>(relaxed = true)
        every { createEmptyBufferedImage(width, height) } returns bufferedImageMock
        every { bufferedImageMock.createGraphics() } returns graphicsMock
        return Pair(bufferedImageMock, graphicsMock)
    }

    private fun setupClassLoader(
        firstResourceName: String,
        firstUrl: URL?,
        secondResourceName: String,
        secondURL: URL?,
        shouldSetupException: Boolean = false,
    ): ClassLoader {
        val classLoaderMock = mockk<ClassLoader>(relaxed = true)
        if (shouldSetupException) {
            every { classLoaderMock.getResource(firstResourceName) } throws ExecutionException(NullPointerException())
        } else {
            every { classLoaderMock.getResource(firstResourceName) } returns firstUrl
            every { classLoaderMock.getResource(secondResourceName) } returns secondURL
        }
        every { createClassLoader() } returns classLoaderMock

        return classLoaderMock
    }

    private fun setupUrlWithText(factionJson: String): URL {
        val url = setupResourceUrl()
        every { readTextFromUrl(url) } returns factionJson

        return url
    }

    private fun verifyBufferedImagePainted(
        bufferedImageMock: BufferedImage,
        result: BufferedImage,
        imageIconMock: ImageIcon,
        graphicsMock: Graphics2D,
    ) {
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) },
        )
        verifyOrder {
            createEmptyBufferedImage(width, height)
            bufferedImageMock.createGraphics()
            imageIconMock.paintIcon(null, graphicsMock, 0, 0)
            graphicsMock.dispose()
        }
        verify(exactly = 1) { createEmptyBufferedImage(width, height) }
        verify(exactly = 1) { bufferedImageMock.createGraphics() }
        verify(exactly = 1) { imageIconMock.paintIcon(null, graphicsMock, 0, 0) }
        verify(exactly = 1) { graphicsMock.dispose() }
    }

    private fun verifyImageIcon(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon,
    ) {
        verifyImage(classLoaderMock, resourceName, url)
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
    }

    private fun verifyImage(classLoaderMock: ClassLoader, resourceName: String, url: URL) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createImageIconFromURL(url)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { createImageIconFromURL(url) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
    }

    private fun verifyIconForSlashPath(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon,
    ) {
        verifyImageForSlashPath(classLoaderMock, resourceName, url)
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
    }

    private fun verifyImageForSlashPath(classLoaderMock: ClassLoader, resourceName: String, url: URL) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            classLoaderMock.getResource("/$resourceName")
            createImageIconFromURL(url)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 1) { createImageIconFromURL(url) }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
    }

    private fun verifyEmptyIconForMissingResource(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon,
    ) {
        verifyEmptyImageForMissingResource(classLoaderMock, resourceName, url)
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
    }

    private fun verifyEmptyImageForMissingResource(classLoaderMock: ClassLoader, resourceName: String, url: URL) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            classLoaderMock.getResource("/$resourceName")
            createEmptyImageIconFromBufferedImage(32)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createImageIconFromURL(url) }
        verify(exactly = 1) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
    }

    private fun verifyEmptyIconAfterException(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon,
    ) {
        verifyEmptyImageAfterException(classLoaderMock, resourceName, url)
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) },
        )
    }

    private fun verifyEmptyImageAfterException(classLoaderMock: ClassLoader, resourceName: String, url: URL) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createEmptyImageIcon()
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createImageIconFromURL(url) }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 1) { createEmptyImageIcon() }
    }

    private fun verifyLoadedFactions(
        classLoaderMock: ClassLoader,
        url: URL,
        factionJson: String,
        loadedFactions: StarWarsFactions,
        result: StarWarsFactions,
    ) {
        verifyOrder {
            classLoaderMock.getResource(factionFileName)
            readTextFromUrl(url)
            parseFactionsFromJson(factionJson)
        }
        verify(exactly = 1) { classLoaderMock.getResource(factionFileName) }
        verify(exactly = 0) { classLoaderMock.getResource("/$factionFileName") }
        verify(exactly = 1) { readTextFromUrl(url) }
        verify(exactly = 1) { parseFactionsFromJson(factionJson) }
        assertAll(
            { assertEquals(loadedFactions, result) },
            {
                result.vehicles.forEach { faction ->
                    faction.data.forEach {
                        assertEquals(faction.id, it.factionId)
                    }
                }
            },
        )
    }

    private fun verifyLoadedFactionsPathWithSlash(
        classLoaderMock: ClassLoader,
        url: URL,
        factionJson: String,
        loadedFactions: StarWarsFactions,
        result: StarWarsFactions,
    ) {
        verifyOrder {
            classLoaderMock.getResource("/$factionFileName")
            readTextFromUrl(url)
            parseFactionsFromJson(factionJson)
        }
        verify(exactly = 1) { classLoaderMock.getResource(factionFileName) }
        verify(exactly = 1) { classLoaderMock.getResource("/$factionFileName") }
        verify(exactly = 1) { readTextFromUrl(url) }
        verify(exactly = 1) { parseFactionsFromJson(factionJson) }
        assertAll(
            { assertEquals(loadedFactions, result) },
            {
                result.vehicles.forEach { faction ->
                    faction.data.forEach {
                        assertEquals(faction.id, it.factionId)
                    }
                }
            },
        )
    }

    private fun verifyLoadedFactionsForMissingResource(
        classLoaderMock: ClassLoader,
        url: URL,
        loadedFactions: StarWarsFactions,
        result: StarWarsFactions,
    ) {
        verifyOrder {
            classLoaderMock.getResource("/$factionFileName")
            parseFactionsFromJson(emptyFactionsJson)
        }
        verify(exactly = 1) { classLoaderMock.getResource(factionFileName) }
        verify(exactly = 1) { classLoaderMock.getResource("/$factionFileName") }
        verify(exactly = 0) { readTextFromUrl(url) }
        verify(exactly = 1) { parseFactionsFromJson(emptyFactionsJson) }
        assertAll(
            { assertEquals(loadedFactions, result) },
            {
                result.vehicles.forEach { faction ->
                    faction.data.forEach {
                        assertEquals(faction.id, it.factionId)
                    }
                }
            },
        )
    }

    //endregion

    //region Test data

    private val pluginIconPath = "/META-INF/pluginIcon.svg"
    private val factionFileName = "json/factions.json"
    private val emptyFactionsJson = "{\"lightsabers\": [],\"vehicles\": []}"
    private val scaledIconSize = 16
    private val width = 10
    private val height = 10

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun factionValues(): Stream<Arguments> = Stream.of(
            Arguments.of(setupEmptyStarWarsFactions()),
            Arguments.of(setupStarWarsFactionsWithOneFactionWithoutVehicles()),
            Arguments.of(setupStarWarsFactionsWithOneFactionWithVehicles()),
            Arguments.of(setupStarWarsFactions()),
        )

        private fun setupEmptyStarWarsFactions(): StarWarsFactions {
            val loadedFactions = mockk<StarWarsFactions>(relaxed = true)
            every { loadedFactions.vehicles } returns listOf()

            return loadedFactions
        }

        private fun setupStarWarsFactionsWithOneFactionWithoutVehicles(): StarWarsFactions {
            val loadedFactions = mockk<StarWarsFactions>(relaxed = true)
            every { loadedFactions.vehicles } returns listOf(StarWarsFaction("1", listOf()))

            return loadedFactions
        }

        private fun setupStarWarsFactionsWithOneFactionWithVehicles(): StarWarsFactions {
            val loadedFactions = mockk<StarWarsFactions>(relaxed = true)
            every { loadedFactions.vehicles } returns listOf(
                StarWarsFaction(
                    "1",
                    listOf(
                        StarWarsVehicle("1", "a", 1, 1, 1f),
                        StarWarsVehicle("2", "b", 2, 2, 2f),
                        StarWarsVehicle("3", "c", 3, 3, 3f),
                    ),
                ),
            )

            return loadedFactions
        }

        private fun setupStarWarsFactions(): StarWarsFactions {
            val loadedFactions = mockk<StarWarsFactions>(relaxed = true)
            every { loadedFactions.vehicles } returns listOf(
                StarWarsFaction(
                    "1",
                    listOf(
                        StarWarsVehicle("1", "a", 1, 1, 1f),
                        StarWarsVehicle("2", "b", 2, 2, 2f),
                        StarWarsVehicle("3", "c", 3, 3, 3f),
                    ),
                ),
                StarWarsFaction(
                    "2",
                    listOf(
                        StarWarsVehicle("1", "a", 1, 1, 1f),
                        StarWarsVehicle("2", "b", 2, 2, 2f),
                        StarWarsVehicle("3", "c", 3, 3, 3f),
                    ),
                ),
                StarWarsFaction(
                    "3",
                    listOf(
                        StarWarsVehicle("1", "a", 1, 1, 1f),
                        StarWarsVehicle("2", "b", 2, 2, 2f),
                        StarWarsVehicle("3", "c", 3, 3, 3f),
                    ),
                ),
            )
            // TODO: setup lightsabers

            return loadedFactions
        }
    }

    //endregion
}
