package com.christopherosthues.starwarsprogressbar.notification.activities

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsProgressConfigurable
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.idea.TestFor
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import javax.swing.Icon

@TestFor(classes = [PluginUpdatedActivity::class])
class PluginUpdatedActivityTests {
    //region Fields

    private lateinit var starWarsPersistentStateComponent: StarWarsPersistentStateComponent

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(StarWarsResourceLoader)
        mockkObject(DoNotAskService)
        mockkObject(StarWarsBundle)
        mockkStatic(PluginManagerCore::class)
        mockkStatic(PluginId::class)
        mockkStatic(NotificationGroupManager::class)
        mockkStatic(ShowSettingsUtil::class)

        starWarsPersistentStateComponent = mockk(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponent

        every { StarWarsBundle.message(any()) } returns ""
        every { StarWarsBundle.message(BundleConstants.PLUGIN_NAME) } returns pluginName
        every {
            StarWarsBundle.message(
                BundleConstants.NOTIFICATION_PLUGIN_UPDATED,
                installedVersion
            )
        } returns pluginUpdatedMessage
        every { StarWarsBundle.message(BundleConstants.NOTIFICATION_CONFIGURE) } returns configureNotificationText
        every {
            StarWarsBundle.message(BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN)
        } returns doNotShowAgainNotificationText
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `runActivity should retrieve correct plugin id`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        every { PluginId.getId(PluginConstants.PluginId) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns null
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { PluginId.getId(PluginConstants.PluginId) }
        verify(exactly = 1) { PluginManagerCore.getPlugin(pluginIdMock) }
    }

    @Test
    fun `runActivity should not display notification if plugin descriptor is null`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val starWarsStateMock = mockk<StarWarsState>(relaxed = true)
        every { PluginId.getId(PluginConstants.PluginId) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns null
        every { starWarsPersistentStateComponent.state } returns starWarsStateMock
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 0) { starWarsStateMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `runActivity should not display notification if star wars state is null`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val pluginDescriptorMock = mockk<IdeaPluginDescriptor>(relaxed = true)
        every { PluginId.getId(PluginConstants.PluginId) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns pluginDescriptorMock
        every { starWarsPersistentStateComponent.state } returns null
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 0) { pluginDescriptorMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `runActivity should not display notification if stored version and installed version are equal`() {
        // Arrange
        val (pluginDescriptorMock, starWarsStateMock) = setupPluginDescriptorAndStarWarsState(storedEqualVersion)
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { pluginDescriptorMock.version }
        verify(exactly = 1) { starWarsStateMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `runActivity should store installed version if stored version and installed version are different`() {
        // Arrange
        val (pluginDescriptorMock, starWarsStateMock) = setupPluginDescriptorAndStarWarsState()
        every { DoNotAskService.canShowNotification() } returns false
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { pluginDescriptorMock.version }
        verify(exactly = 1) { starWarsStateMock.version }
        verify(exactly = 1) { starWarsStateMock.version = installedVersion }
    }

    @Test
    fun `runActivity should not display notification if notification should not be displayed`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        every { DoNotAskService.canShowNotification() } returns false
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `runActivity should retrieve correct notification group`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (notificationManagerMock, _, _) = setupNotification()
        every { StarWarsResourceLoader.getPluginIcon() } returns mockk(relaxed = true)
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { NotificationGroupManager.getInstance() }
        verify(exactly = 1) { notificationManagerMock.getNotificationGroup(PluginConstants.NotificationGroupId) }
    }

    @Test
    fun `runActivity should create correct notification`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, notificationGroupMock, _) = setupNotification()
        every { StarWarsResourceLoader.getPluginIcon() } returns mockk(relaxed = true)
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) {
            notificationGroupMock.createNotification(
                pluginName,
                pluginUpdatedMessage,
                NotificationType.INFORMATION
            )
        }
    }

    @Test
    fun `runActivity should create correct notification with correct icon`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 1) { StarWarsResourceLoader.getPluginIcon() }
        verify(exactly = 1) { notificationMock.icon = pluginIconMock }
    }

    @Test
    fun `runActivity should create correct notification with two actions`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()

        // Act
        sut.runActivity(mockk())

        // Assert
        verify(exactly = 2) { notificationMock.addAction(any()) }
        verify(exactly = 1) { StarWarsBundle.message(BundleConstants.NOTIFICATION_CONFIGURE) }
        verify(exactly = 1) { StarWarsBundle.message(BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN) }
    }

    @Test
    fun `runActivity should display correct notification`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()

        // Act
        sut.runActivity(projectMock)

        // Assert
        verify(exactly = 1) { notificationMock.notify(projectMock) }
    }

    @Test
    fun `runActivity should create correct notification actions and first action should open settings`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val actions = mutableListOf<DumbAwareAction>()
        every { notificationMock.addAction(capture(actions)) } returns notificationMock
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        val showSettingsUtilMock = mockk<ShowSettingsUtil>(relaxed = true)
        every { ShowSettingsUtil.getInstance() } returns showSettingsUtilMock
        justRun { showSettingsUtilMock.showSettingsDialog(any(), StarWarsProgressConfigurable::class.java) }
        val actionEventMock = mockk<AnActionEvent>(relaxed = true)

        // Act
        sut.runActivity(projectMock)

        // Assert
        assertAll(
            { assertEquals(2, actions.size) },
            { assertEquals(configureNotificationText, actions.first().templateText) }
        )

        // Act
        actions.first().actionPerformed(actionEventMock)

        // Assert
        verify(exactly = 1) {
            showSettingsUtilMock.showSettingsDialog(
                projectMock,
                StarWarsProgressConfigurable::class.java
            )
        }
    }

    @Test
    fun `runActivity should create notification actions and second action should store do not ask and hide balloon`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val actions = mutableListOf<DumbAwareAction>()
        every { notificationMock.addAction(capture(actions)) } returns notificationMock
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        justRun { DoNotAskService.setDoNotAskFor(true) }
        val actionEventMock = mockk<AnActionEvent>(relaxed = true)

        // Act
        sut.runActivity(projectMock)

        // Assert
        assertAll(
            { assertEquals(2, actions.size) },
            { assertEquals(doNotShowAgainNotificationText, actions[1].templateText) }
        )

        // Act
        actions[1].actionPerformed(actionEventMock)

        // Assert
        verify(exactly = 1) { DoNotAskService.setDoNotAskFor(true) }
        verify(exactly = 1) { notificationMock.hideBalloon() }
    }

    //endregion

    //region Helper methods

    private fun setupPluginDescriptorAndStarWarsState(
        storedVersion: String = storedDifferentVersion
    ): Pair<IdeaPluginDescriptor, StarWarsState> {
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val pluginDescriptorMock = mockk<IdeaPluginDescriptor>(relaxed = true)
        val starWarsStateMock = mockk<StarWarsState>(relaxed = true)
        every { PluginId.getId(PluginConstants.PluginId) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns pluginDescriptorMock
        every { starWarsPersistentStateComponent.state } returns starWarsStateMock
        every { starWarsStateMock.version } returns storedVersion
        every { pluginDescriptorMock.version } returns installedVersion

        return Pair(pluginDescriptorMock, starWarsStateMock)
    }

    private fun setupNotification(): Triple<NotificationGroupManager, NotificationGroup, Notification> {
        val notificationManagerMock = mockk<NotificationGroupManager>(relaxed = true)
        val notificationGroupMock = mockk<NotificationGroup>(relaxed = true)
        val notificationMock = mockk<Notification>(relaxed = true)
        every { DoNotAskService.canShowNotification() } returns true
        every { NotificationGroupManager.getInstance() } returns notificationManagerMock
        every {
            notificationManagerMock.getNotificationGroup(PluginConstants.NotificationGroupId)
        } returns notificationGroupMock
        every {
            notificationGroupMock.createNotification(
                pluginName,
                pluginUpdatedMessage,
                NotificationType.INFORMATION
            )
        } returns notificationMock
        every { notificationMock.addAction(any()) } returns notificationMock
        justRun { notificationMock.notify(any()) }

        return Triple(notificationManagerMock, notificationGroupMock, notificationMock)
    }

    //endregion

    //region Test data

    private val installedVersion = "1.0.1"
    private val storedDifferentVersion = "1.0.0"
    private val storedEqualVersion = installedVersion
    private val pluginName = "Star Wars Progress Bar"
    private val pluginUpdatedMessage = "Star Wars Progress Bar updated"
    private val configureNotificationText = "configure"
    private val doNotShowAgainNotificationText = "do not show again"

    //endregion
}
