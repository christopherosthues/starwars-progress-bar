package com.christopherosthues.starwarsprogressbar.util

import com.intellij.idea.TestFor
import com.intellij.ui.IconManager
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
import org.junit.jupiter.params.provider.ValueSource
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import java.util.concurrent.ExecutionException
import javax.swing.Icon
import javax.swing.ImageIcon

@TestFor(classes = [StarWarsResourceLoader::class])
class StarWarsResourceLoaderTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(IconManager::class)
        mockkStatic(::createClassLoader)
        mockkStatic(::createScaledEmptyImageIcon)
        mockkStatic(::createEmptyTranslucentBufferedImage)
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
        val iconManagerMock = mockk<IconManager>(relaxed = true)
        every { IconManager.getInstance() } returns iconManagerMock
        every {
            iconManagerMock.getIcon(
                pluginIconPath,
                StarWarsResourceLoader.javaClass
            )
        } returns mockk(relaxed = true)

        // Act
        StarWarsResourceLoader.getPluginIcon()

        // Assert
        verifyOrder {
            IconManager.getInstance()
            iconManagerMock.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass)
        }
        verify(exactly = 1) { IconManager.getInstance() }
        verify(exactly = 1) { iconManagerMock.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass) }
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
            { assertSame(imageIconMock, result) }
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
            { assertSame(imageIconMock, result) }
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
            { assertNotSame(imageIconMock, result) }
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
                Image.SCALE_SMOOTH
            )
        } returns imageMock
        every { createImageIconFromImage(imageMock) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getPluginIcon()

        // Assert
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) }
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
        val resourceName = setupFactionResourceName(isLargeIcon, factionName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        // Assert
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createImageIconFromURL(url)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { createImageIconFromURL(url) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return cached icon if icon was queried before`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction_cached"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createImageIconFromURL(url)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { createImageIconFromURL(url) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)

        // Act
        result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        // Assert
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) }
        )
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

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return icon converted to buffered image if resource path starts with a slash`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction2"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        // Assert
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
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return empty image icon if resource does not exists`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction3"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        // Assert
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
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getFactionLogo should return empty image icon if an execution exception is thrown`(isLargeIcon: Boolean) {
        // Arrange
        val imageIconMock = setupIconImage()
        val factionName = "faction4"
        val resourceName = setupFactionResourceName(isLargeIcon, factionName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        val (bufferedImageMock, graphicsMock) = setupImageScaling()
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getFactionLogo(factionName, isLargeIcon)

        // Assert
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createEmptyImageIcon()
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createImageIconFromURL(url) }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 1) { createEmptyImageIcon() }
        verifyBufferedImagePainted(bufferedImageMock, result, imageIconMock, graphicsMock)
    }

    //endregion

    //region getIcon tests

    @Test
    fun `getIcon should return icon for provided vehicle name`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle"
        val resourceName = setupIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
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
        val resourceName = setupIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
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
        val resourceName = setupIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
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
        val resourceName = setupIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
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
        val resourceName = setupIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getIcon(vehicleName)

        // Assert
        verifyEmptyIconAfterException(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    //endregion

    //region getIcon tests

    @Test
    fun `getReversedIcon should return icon for provided vehicle name`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle"
        val resourceName = setupReversedIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        // Assert
        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getReversedIcon should return cached icon if icon was queried before`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle_cached"
        val resourceName = setupReversedIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, url, "/$resourceName", null)
        every { createImageIconFromURL(url) } returns imageIconMock

        var result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)

        // Act
        result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        // Assert
        verifyImageIcon(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getReversedIcon should return correct icon for provided vehicle name if resource path starts with a slash`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle2"
        val resourceName = setupReversedIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", url)
        every { createImageIconFromURL(url) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        // Assert
        verifyIconForSlashPath(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getReversedIcon should return empty image icon if resource does not exists`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle3"
        val resourceName = setupReversedIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null)
        every { createEmptyImageIconFromBufferedImage(32) } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        // Assert
        verifyEmptyIconForMissingResource(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    @Test
    fun `getReversedIcon should return empty image icon if an execution exception is thrown`() {
        // Arrange
        val imageIconMock = setupIconImage()
        val vehicleName = "vehicle4"
        val resourceName = setupReversedIconResourceName(vehicleName)
        val url = setupResourceUrl(resourceName)
        val classLoaderMock = setupClassLoader(resourceName, null, "/$resourceName", null, true)
        every { createEmptyImageIcon() } returns imageIconMock

        // Act
        val result = StarWarsResourceLoader.getReversedIcon(vehicleName)

        // Assert
        verifyEmptyIconAfterException(classLoaderMock, resourceName, url, imageIconMock, result)
    }

    //endregion

    //region loadFactions tests

    // TODO: unit test loadFactions

    //endregion

    //endregion

    //region Helper methods

    private fun setupPluginIcon(width: Int, height: Int): Icon {
        val iconMock = mockk<Icon>(relaxed = true)
        val iconManagerMock = mockk<IconManager>(relaxed = true)
        every { IconManager.getInstance() } returns iconManagerMock
        every { iconManagerMock.getIcon(pluginIconPath, StarWarsResourceLoader.javaClass) } returns iconMock
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

    private fun setupFactionResourceName(isLargeIcon: Boolean, factionName: String): String {
        val largeIcon = if (isLargeIcon) "@2x" else ""
        return "icons/$factionName/logo$largeIcon.png"
    }

    private fun setupIconResourceName(vehicleName: String): String {
        return "icons/$vehicleName.png"
    }

    private fun setupReversedIconResourceName(vehicleName: String): String {
        return "icons/${vehicleName}_r.png"
    }

    private fun setupResourceUrl(resourceName: String): URL {
        return URL("https://$resourceName")
    }

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
        shouldSetupException: Boolean = false
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

    private fun verifyBufferedImagePainted(
        bufferedImageMock: BufferedImage,
        result: BufferedImage,
        imageIconMock: ImageIcon,
        graphicsMock: Graphics2D
    ) {
        assertAll(
            { assertEquals(bufferedImageMock, result) },
            { assertSame(bufferedImageMock, result) }
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
        result: Icon
    ) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createImageIconFromURL(url)
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 1) { createImageIconFromURL(url) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 0) { createEmptyImageIcon() }
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) }
        )
    }

    private fun verifyIconForSlashPath(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon
    ) {
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
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) }
        )
    }

    private fun verifyEmptyIconForMissingResource(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon
    ) {
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
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) }
        )
    }

    private fun verifyEmptyIconAfterException(
        classLoaderMock: ClassLoader,
        resourceName: String,
        url: URL,
        imageIconMock: ImageIcon,
        result: Icon
    ) {
        verifyOrder {
            classLoaderMock.getResource(resourceName)
            createEmptyImageIcon()
        }
        verify(exactly = 1) { classLoaderMock.getResource(resourceName) }
        verify(exactly = 0) { classLoaderMock.getResource("/$resourceName") }
        verify(exactly = 0) { createImageIconFromURL(url) }
        verify(exactly = 0) { createEmptyImageIconFromBufferedImage(32) }
        verify(exactly = 1) { createEmptyImageIcon() }
        assertAll(
            { assertEquals(imageIconMock, result) },
            { assertSame(imageIconMock, result) }
        )
    }

    //endregion

    //region Test data

    private val pluginIconPath = "/META-INF/pluginIcon.svg"
    private val scaledIconSize = 16
    private val width = 10
    private val height = 10

    //endregion
}
