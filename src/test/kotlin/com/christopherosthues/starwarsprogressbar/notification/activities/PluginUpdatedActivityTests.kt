package com.christopherosthues.starwarsprogressbar.notification.activities

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsProgressConfigurable
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.mocks.TestPropertiesComponent
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.idea.TestFor
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications.Bus
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableGroup
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.NonNls
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.awt.Component
import java.util.function.Consumer
import java.util.function.Predicate
import javax.swing.Icon

@TestFor(classes = [PluginUpdatedActivity::class])
class PluginUpdatedActivityTests {
    //region Fields

    private lateinit var starWarsPersistentStateComponent: StarWarsPersistentStateComponent
    private lateinit var applicationMock: Application

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(StarWarsResourceLoader)
        mockkObject(DoNotAskService)
        mockkObject(StarWarsBundle)
        mockkStatic(StarWarsBundle::message)
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
                installedVersion,
            )
        } returns pluginUpdatedMessage
        every { StarWarsBundle.message(BundleConstants.NOTIFICATION_CONFIGURE) } returns configureNotificationText
        every {
            StarWarsBundle.message(BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN)
        } returns doNotShowAgainNotificationText

        applicationMock = mockk<Application>(relaxed = true)
        ApplicationManager.setApplication(applicationMock)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        ApplicationManager.setApplication(null)
    }

    //endregion

    //region Tests

    @Test
    fun `execute should retrieve correct plugin id`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        every { PluginId.getId(PluginConstants.PLUGIN_ID) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns null
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 1) { PluginId.getId(PluginConstants.PLUGIN_ID) }
        verify(exactly = 1) { PluginManagerCore.getPlugin(pluginIdMock) }
    }

    @Test
    fun `execute should not display notification if plugin descriptor is null`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val starWarsStateMock = mockk<StarWarsState>(relaxed = true)
        every { PluginId.getId(PluginConstants.PLUGIN_ID) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns null
        every { starWarsPersistentStateComponent.state } returns starWarsStateMock
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 0) { starWarsStateMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `execute should not display notification if star wars state is null`() {
        // Arrange
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val pluginDescriptorMock = mockk<IdeaPluginDescriptor>(relaxed = true)
        every { PluginId.getId(PluginConstants.PLUGIN_ID) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns pluginDescriptorMock
        every { starWarsPersistentStateComponent.state } returns null
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 0) { pluginDescriptorMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `execute should not display notification if stored version and installed version are equal`() {
        // Arrange
        val (pluginDescriptorMock, starWarsStateMock) = setupPluginDescriptorAndStarWarsState(storedEqualVersion)
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 1) { pluginDescriptorMock.version }
        verify(exactly = 1) { starWarsStateMock.version }
        verify(exactly = 0) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `execute should store installed version if stored version and installed version are different`() {
        // Arrange
        val (pluginDescriptorMock, starWarsStateMock) = setupPluginDescriptorAndStarWarsState()
        every { DoNotAskService.canShowNotification() } returns false
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 1) { pluginDescriptorMock.version }
        verify(exactly = 1) { starWarsStateMock.version }
        verify(exactly = 1) { starWarsStateMock.version = installedVersion }
    }

    @Test
    fun `execute should not display notification if notification should not be displayed`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        every { DoNotAskService.canShowNotification() } returns false
        val sut = PluginUpdatedActivity()

        // Act
        runBlocking { sut.execute(mockk()) }

        // Assert
        verify(exactly = 1) { DoNotAskService.canShowNotification() }
        verify(exactly = 0) { NotificationGroupManager.getInstance() }
    }

    @Test
    fun `execute should retrieve correct notification group`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (notificationManagerMock, _, _) = setupNotification()
        every { StarWarsResourceLoader.getPluginIcon() } returns mockk(relaxed = true)
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        verify(exactly = 1) { NotificationGroupManager.getInstance() }
        verify(exactly = 1) { notificationManagerMock.getNotificationGroup(PluginConstants.NOTIFICATION_GROUP_ID) }
    }

    @Test
    fun `execute should create correct notification`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, notificationGroupMock, _) = setupNotification()
        every { StarWarsResourceLoader.getPluginIcon() } returns mockk(relaxed = true)
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        verify(exactly = 1) {
            notificationGroupMock.createNotification(
                pluginName,
                pluginUpdatedMessage,
                NotificationType.INFORMATION,
            )
        }
    }

    @Test
    fun `execute should create correct notification with correct icon`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        assertEquals(pluginIconMock, notificationMock.icon)
        verify(exactly = 1) { StarWarsResourceLoader.getPluginIcon() }
    }

    @Test
    fun `execute should create correct notification with two actions`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        assertEquals(2, notificationMock.actions.size)
        verify(exactly = 1) { StarWarsBundle.message(BundleConstants.NOTIFICATION_CONFIGURE) }
        verify(exactly = 1) { StarWarsBundle.message(BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN) }
    }

    @Test
    fun `execute should create correct notification actions and first action should open settings`() {
        // Arrange
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()
        val actionEventMock = mockk<AnActionEvent>(relaxed = true)
        val showSettingsUtilMock = TestShowSettingsUtil()
        every { applicationMock.getService(any<Class<ShowSettingsUtil>>()) } returns showSettingsUtilMock
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        assertAll(
            { assertEquals(2, notificationMock.actions.size) },
            { assertEquals(configureNotificationText, notificationMock.actions.first().templateText) },
        )

        // Act
        notificationMock.actions.first().actionPerformed(actionEventMock)

        // Assert
        assertAll(
            { assertEquals(1, showSettingsUtilMock.projectList.size) },
            { assertEquals(1, showSettingsUtilMock.classList.size) },
            { assertEquals(projectMock, showSettingsUtilMock.projectList.first()) },
            { assertEquals(StarWarsProgressConfigurable::class.java, showSettingsUtilMock.classList.first()) },
        )
    }

    @Test
    fun `execute should create notification actions and second action should store do not ask and hide balloon`() {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID) } returns null
        setupPluginDescriptorAndStarWarsState()
        val (_, _, notificationMock) = setupNotification()
        val pluginIconMock = mockk<Icon>(relaxed = true)
        every { StarWarsResourceLoader.getPluginIcon() } returns pluginIconMock
        val sut = PluginUpdatedActivity()
        val actionEventMock = mockk<AnActionEvent>(relaxed = true)
        val propertiesComponentMock = TestPropertiesComponent()
        every { applicationMock.getService(any<Class<PropertiesComponent>>()) } returns propertiesComponentMock
        val projectMock = mockk<Project>()
        every { projectMock.disposed } returns { false }

        // Act
        runBlocking { sut.execute(projectMock) }

        // Assert
        assertAll(
            { assertEquals(2, notificationMock.actions.size) },
            { assertEquals(doNotShowAgainNotificationText, notificationMock.actions[1].templateText) },
        )

        // Act
        notificationMock.actions[1].actionPerformed(actionEventMock)

        // Assert
        val doNotAsk = propertiesComponentMock.getBoolean("Notification.DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}")
        assertTrue(doNotAsk)
        assertNull(notificationMock.balloon)
    }

    //endregion

    //region Helper methods

    private fun setupPluginDescriptorAndStarWarsState(
        storedVersion: String = storedDifferentVersion,
    ): Pair<IdeaPluginDescriptor, StarWarsState> {
        val pluginIdMock = mockk<PluginId>(relaxed = true)
        val pluginDescriptorMock = mockk<IdeaPluginDescriptor>(relaxed = true)
        val starWarsStateMock = mockk<StarWarsState>(relaxed = true)
        every { PluginId.getId(PluginConstants.PLUGIN_ID) } returns pluginIdMock
        every { PluginManagerCore.getPlugin(pluginIdMock) } returns pluginDescriptorMock
        every { starWarsPersistentStateComponent.state } returns starWarsStateMock
        every { starWarsStateMock.version } returns storedVersion
        every { pluginDescriptorMock.version } returns installedVersion

        return Pair(pluginDescriptorMock, starWarsStateMock)
    }

    private fun setupNotification(): Triple<NotificationGroupManager, NotificationGroup, Notification> {
        val notificationManagerMock = mockk<NotificationGroupManager>(relaxed = true)
        val notificationGroupMock = mockk<NotificationGroup>(relaxed = true)
        val notificationMock = Notification(pluginName, pluginUpdatedMessage, NotificationType.INFORMATION)
        every { DoNotAskService.canShowNotification() } returns true
        every { NotificationGroupManager.getInstance() } returns notificationManagerMock
        every {
            notificationManagerMock.getNotificationGroup(PluginConstants.NOTIFICATION_GROUP_ID)
        } returns notificationGroupMock
        every {
            notificationGroupMock.createNotification(
                pluginName,
                pluginUpdatedMessage,
                NotificationType.INFORMATION,
            )
        } returns notificationMock
//        every { notificationMock.addAction(any()) } returns notificationMock

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

    private class TestShowSettingsUtil : ShowSettingsUtil() {
        var projectList = mutableListOf<Project?>()
        var classList = mutableListOf<Class<Configurable?>>()

        override fun showSettingsDialog(
            p0: Project,
            vararg p1: ConfigurableGroup?
        ) {
        }

        override fun <T : Configurable?> showSettingsDialog(
            p0: Project?,
            p1: Class<T?>
        ) {
            projectList.add(p0)
            classList.add(p1 as Class<Configurable?>)
        }

        override fun showSettingsDialog(
            p0: Project?,
            p1: @NlsContexts.ConfigurableName String
        ) {
        }

        override fun showSettingsDialog(
            p0: Project,
            p1: Configurable?
        ) {
        }

        override fun <T : Configurable?> showSettingsDialog(
            p0: Project?,
            p1: Class<T?>,
            p2: Consumer<in T>?
        ) {
        }

        override fun showSettingsDialog(
            p0: Project?,
            p1: Predicate<in Configurable>,
            p2: Consumer<in Configurable>?
        ) {
        }

        override fun editConfigurable(
            p0: Project?,
            p1: Configurable
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Project?,
            p1: Configurable,
            p2: Runnable?
        ): Boolean {
            return false
        }

        override fun <T : Configurable?> editConfigurable(
            p0: Project?,
            p1: T & Any,
            p2: Consumer<in T>
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Component?,
            p1: Configurable
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Component?,
            p1: @NlsContexts.ConfigurableName String
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Component?,
            p1: @NlsContexts.ConfigurableName String,
            p2: Runnable?
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Component?,
            p1: Configurable,
            p2: Runnable?
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Project?,
            p1: @NonNls String,
            p2: Configurable
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Project?,
            p1: @NonNls String,
            p2: Configurable,
            p3: Boolean
        ): Boolean {
            return false
        }

        override fun editConfigurable(
            p0: Component?,
            p1: @NonNls String,
            p2: Configurable
        ): Boolean {
            return false
        }

    }
}
