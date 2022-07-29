package com.christopherosthues.starwarsprogressbar.listeners

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.ui.StarWarsProgressBarFactory
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.ui.LafManager
import com.intellij.idea.TestFor
import com.intellij.openapi.extensions.PluginId
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import javax.swing.UIDefaults
import javax.swing.UIManager

private const val progressBarUiKey = "ProgressBarUI"

@TestFor(classes = [StarWarsProgressListener::class])
class StarWarsProgressListenerTests {

    //region Fields

    private lateinit var pluginIdMock: PluginId
    private val pluginId = "starwarsprogressbar"

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(UIManager::class)
        mockkStatic(PluginId::class)

        val equalsSlot = slot<Any>()
        pluginIdMock = mockk()
        every { pluginIdMock.idString } returns pluginId
        every { pluginIdMock.equals(other = capture(equalsSlot)) } answers {
            pluginIdEquals(pluginIdMock, equalsSlot.captured)
        }

        every { UIManager.get(any()) } returns ""
        every { UIManager.put(any(), any()) } returns ""
        every { PluginId.getId(PluginConstants.PluginId) } returns pluginIdMock
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Constructor tests

    @Test
    fun `constructor should retrieve current progress bar ui key`() {
        // Arrange

        // Act
        StarWarsProgressListener()

        // Assert
        verifyProgressBarKeyRetrieved(1)
    }

    @Test
    fun `constructor should store progress bar ui key of star wars progress bar`() {
        // Arrange

        // Act
        StarWarsProgressListener()

        // Assert
        verifyCorrectProgressBarKeyIsStored(1)
    }

    @Test
    fun `constructor should store progress bar factory`() {
        // Arrange
        val uiDefaultsMock = createAndSetupUIDefaultsMock()

        // Act
        StarWarsProgressListener()

        // Assert
        verifyProgressBarFactoryStored(uiDefaultsMock, 1)
    }

    @Test
    fun `constructor should retrieve plugin id`() {
        // Arrange

        // Act
        StarWarsProgressListener()

        // Assert
        verify(exactly = 1) { PluginId.getId(PluginConstants.PluginId) }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `constructor should not store previous progress bar if previous progress bar is equal to star wars progress bar key`(
        isUpdate: Boolean
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        every { UIManager.get(progressBarUiKey) } returns StarWarsProgressBarFactory::class.java.name

        // Act
        val sut = StarWarsProgressListener()
        verify(exactly = 1) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }

        sut.beforePluginUnload(ideaPluginDescriptorMock, isUpdate)

        // Assert
        verify(exactly = 1) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }
    }

    //endregion

    //region lookAndFeelChanged tests

    @Test
    fun `lookAndFeelChanged should retrieve current progress bar ui key`() {
        // Arrange
        val lookAndFellMock = mockk<LafManager>()
        val sut = StarWarsProgressListener()

        verifyProgressBarKeyRetrieved(1)

        // Act
        sut.lookAndFeelChanged(lookAndFellMock)

        // Assert
        verifyProgressBarKeyRetrieved(2)
    }

    @Test
    fun `lookAndFeelChanged should store progress bar ui key of star wars progress bar`() {
        // Arrange
        val lookAndFellMock = mockk<LafManager>()
        val sut = StarWarsProgressListener()

        verifyCorrectProgressBarKeyIsStored(1)

        // Act
        sut.lookAndFeelChanged(lookAndFellMock)

        // Assert
        verifyCorrectProgressBarKeyIsStored(2)
    }

    @Test
    fun `lookAndFeelChanged should store progress bar factory`() {
        // Arrange
        val lookAndFellMock = mockk<LafManager>()
        val uiDefaultsMock = createAndSetupUIDefaultsMock()
        val sut = StarWarsProgressListener()

        verifyProgressBarFactoryStored(uiDefaultsMock, 1)

        // Act
        sut.lookAndFeelChanged(lookAndFellMock)

        // Assert
        verifyProgressBarFactoryStored(uiDefaultsMock, 2)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `lookAndFeelChanged should not store previous progress bar if previous progress bar is equal to star wars progress bar key`(
        isUpdate: Boolean
    ) {
        // Arrange
        val lookAndFellMock = mockk<LafManager>()
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val progressBarKey = "progressbar"
        every { UIManager.get(progressBarUiKey) } returns progressBarKey
        val sut = StarWarsProgressListener()
        every { UIManager.get(progressBarUiKey) } returns StarWarsProgressBarFactory::class.java.name

        verify(exactly = 1) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }

        // Act
        sut.lookAndFeelChanged(lookAndFellMock)
        verify(exactly = 2) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }

        sut.beforePluginUnload(ideaPluginDescriptorMock, isUpdate)

        // Assert
        verify(exactly = 2) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }
        verify(exactly = 1) { UIManager.put(progressBarUiKey, progressBarKey) }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `lookAndFeelChanged should store previous progress bar if previous progress bar is equal to star wars progress bar key`(
        isUpdate: Boolean
    ) {
        // Arrange
        val lookAndFellMock = mockk<LafManager>()
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val progressBarKey = "progressbar"
        val newProgressBarKey = "progressbar 2"
        every { UIManager.get(progressBarUiKey) } returns progressBarKey
        val sut = StarWarsProgressListener()
        every { UIManager.get(progressBarUiKey) } returns newProgressBarKey

        // Act
        sut.lookAndFeelChanged(lookAndFellMock)
        sut.beforePluginUnload(ideaPluginDescriptorMock, isUpdate)

        // Assert
        verify(exactly = 0) { UIManager.put(progressBarUiKey, progressBarKey) }
        verify(exactly = 1) { UIManager.put(progressBarUiKey, newProgressBarKey) }
    }

    //endregion

    //region pluginLoaded tests

    @Test
    fun `pluginLoaded should retrieve current progress bar ui key if provided plugin descriptor is star wars progress bar plugin`() {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val sut = StarWarsProgressListener()

        verifyProgressBarKeyRetrieved(1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyProgressBarKeyRetrieved(2)
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("otherPluginValues")
    fun `pluginLoaded should retrieve current progress bar ui key if provided plugin descriptor is not star wars progress bar plugin`(
        pluginId: String
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val sut = StarWarsProgressListener()

        verifyProgressBarKeyRetrieved(1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyProgressBarKeyRetrieved(1)
    }

    @Test
    fun `pluginLoaded should store progress bar ui key of star wars progress bar if provided plugin descriptor is star wars progress bar plugin`() {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val sut = StarWarsProgressListener()

        verifyCorrectProgressBarKeyIsStored(1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyCorrectProgressBarKeyIsStored(2)
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("otherPluginValues")
    fun `pluginLoaded should store progress bar ui key of star wars progress bar if provided plugin descriptor is not star wars progress bar plugin`(
        pluginId: String
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val sut = StarWarsProgressListener()

        verifyCorrectProgressBarKeyIsStored(1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyCorrectProgressBarKeyIsStored(1)
    }

    @Test
    fun `pluginLoaded should store progress bar factory if provided plugin descriptor is star wars progress bar plugin`() {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val uiDefaultsMock = createAndSetupUIDefaultsMock()
        val sut = StarWarsProgressListener()

        verifyProgressBarFactoryStored(uiDefaultsMock, 1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyProgressBarFactoryStored(uiDefaultsMock, 2)
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("otherPluginValues")
    fun `pluginLoaded should store progress bar factory if provided plugin descriptor is not star wars progress bar plugin`(
        pluginId: String
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val uiDefaultsMock = createAndSetupUIDefaultsMock()
        val sut = StarWarsProgressListener()

        verifyProgressBarFactoryStored(uiDefaultsMock, 1)

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)

        // Assert
        verifyProgressBarFactoryStored(uiDefaultsMock, 1)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `pluginLoaded should not store previous progress bar if previous progress bar is equal to star wars progress bar key`(
        isUpdate: Boolean
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val progressBarKey = "progressbar"
        every { UIManager.get(progressBarUiKey) } returns progressBarKey
        val sut = StarWarsProgressListener()
        every { UIManager.get(progressBarUiKey) } returns StarWarsProgressBarFactory::class.java.name

        verify(exactly = 1) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)
        verify(exactly = 2) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }

        sut.beforePluginUnload(ideaPluginDescriptorMock, isUpdate)

        // Assert
        verify(exactly = 2) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }
        verify(exactly = 1) { UIManager.put(progressBarUiKey, progressBarKey) }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `pluginLoaded should store previous progress bar if previous progress bar is equal to star wars progress bar key`(
        isUpdate: Boolean
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        val progressBarKey = "progressbar"
        val newProgressBarKey = "progressbar 2"
        every { UIManager.get(progressBarUiKey) } returns progressBarKey
        val sut = StarWarsProgressListener()
        every { UIManager.get(progressBarUiKey) } returns newProgressBarKey

        // Act
        sut.pluginLoaded(ideaPluginDescriptorMock)
        sut.beforePluginUnload(ideaPluginDescriptorMock, isUpdate)

        // Assert
        verify(exactly = 0) { UIManager.put(progressBarUiKey, progressBarKey) }
        verify(exactly = 1) { UIManager.put(progressBarUiKey, newProgressBarKey) }
    }

    //endregion

    //region beforePluginUnload tests

    @ParameterizedTest
    @MethodSource("previousProgressBarKeyValues")
    fun `beforePluginUnload should reset progress bar ui key to previous key if is plugin update`(previousProgressBarKey: String) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        every { UIManager.get(progressBarUiKey) } returns previousProgressBarKey
        val sut = StarWarsProgressListener()

        // Act
        sut.beforePluginUnload(ideaPluginDescriptorMock, true)

        // Assert
        verify(exactly = 1) { UIManager.put(progressBarUiKey, previousProgressBarKey) }
    }

    @ParameterizedTest
    @MethodSource("otherPluginAndPreviousProgressBarKeyValues")
    fun `beforePluginUnload should not reset progress bar ui key to previous key if provided plugin descriptor is not star wars progress bar plugin and is plugin update`(
        pluginId: String,
        previousProgressBarKey: String
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        every { UIManager.get(progressBarUiKey) } returns previousProgressBarKey
        val sut = StarWarsProgressListener()

        // Act
        sut.beforePluginUnload(ideaPluginDescriptorMock, true)

        // Assert
        verify(exactly = 0) { UIManager.put(progressBarUiKey, previousProgressBarKey) }
    }

    @ParameterizedTest
    @MethodSource("previousProgressBarKeyValues")
    fun `beforePluginUnload should reset progress bar ui key to previous key`(previousProgressBarKey: String) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        every { UIManager.get(progressBarUiKey) } returns previousProgressBarKey
        val sut = StarWarsProgressListener()

        // Act
        sut.beforePluginUnload(ideaPluginDescriptorMock, false)

        // Assert
        verify(exactly = 1) { UIManager.put(progressBarUiKey, previousProgressBarKey) }
    }

    @ParameterizedTest
    @MethodSource("otherPluginAndPreviousProgressBarKeyValues")
    fun `beforePluginUnload should not reset progress bar ui key to previous key if provided plugin descriptor is not star wars progress bar plugin`(
        pluginId: String,
        previousProgressBarKey: String
    ) {
        // Arrange
        val ideaPluginDescriptorMock = createAndSetupPluginDescriptorMock(pluginId)
        every { UIManager.get(progressBarUiKey) } returns previousProgressBarKey
        val sut = StarWarsProgressListener()

        // Act
        sut.beforePluginUnload(ideaPluginDescriptorMock, false)

        // Assert
        verify(exactly = 0) { UIManager.put(progressBarUiKey, previousProgressBarKey) }
    }

    //endregion

    //region Helper methods

    private fun pluginIdEquals(pluginIdMock: PluginId, o: Any): Boolean {
        if (o == pluginIdMock) {
            return true
        }

        if (o !is PluginId) {
            return false
        }

        return pluginIdMock.idString == o.idString
    }

    private fun createAndSetupUIDefaultsMock(): UIDefaults {
        val uiDefaultsMock = mockk<UIDefaults>()
        every { UIManager.getDefaults() } returns uiDefaultsMock
        every { uiDefaultsMock.put(any(), any()) } returns ""

        return uiDefaultsMock
    }

    private fun createAndSetupPluginDescriptorMock(pluginId: String): IdeaPluginDescriptor {
        val ideaPluginDescriptorMock = mockk<IdeaPluginDescriptor>()
        val pluginIdMock = mockk<PluginId>()
        every { pluginIdMock.idString } returns pluginId
        every { ideaPluginDescriptorMock.pluginId } returns pluginIdMock

        return ideaPluginDescriptorMock
    }

    private fun verifyProgressBarKeyRetrieved(exactlyCalled: Int) {
        verify(exactly = exactlyCalled) { UIManager.get(progressBarUiKey) }
    }

    private fun verifyCorrectProgressBarKeyIsStored(exactlyCalled: Int) {
        verify(exactly = exactlyCalled) { UIManager.put(progressBarUiKey, StarWarsProgressBarFactory::class.java.name) }
    }

    private fun verifyProgressBarFactoryStored(uiDefaultsMock: UIDefaults, exactlyCalled: Int) {
        verify(exactly = exactlyCalled) {
            uiDefaultsMock.put(
                StarWarsProgressBarFactory::class.java.name,
                StarWarsProgressBarFactory::class.java
            )
        }
    }

    companion object {
        @JvmStatic
        private fun otherPluginValues(): Stream<String> {
            return Stream.of("progressbar", "aSFAf", "starwarsprogressba", "starwarsprogressbarr", "tarwarsprogressbar")
        }

        @JvmStatic
        private fun previousProgressBarKeyValues(): Stream<String> {
            return Stream.of(
                "asdas",
                "starwarsprogressbar",
                "StarWarsBarProgressFactor",
                "StarWarsBarProgressFactoryy",
                "starwarsprogressbarfactory"
            )
        }

        @JvmStatic
        private fun otherPluginAndPreviousProgressBarKeyValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("progressbar", "asdas"),
                Arguments.of("aSFAf", "starwarsprogressbar"),
                Arguments.of("starwarsprogressba", "StarWarsBarProgressFactor"),
                Arguments.of("starwarsprogressbarr", "StarWarsBarProgressFactoryy"),
                Arguments.of("tarwarsprogressbar", "starwarsprogressbarfactory"),
                Arguments.of("", "")
            )
        }
    }

    //endregion
}
