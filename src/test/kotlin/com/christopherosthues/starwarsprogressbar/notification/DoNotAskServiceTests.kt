package com.christopherosthues.starwarsprogressbar.notification

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.ide.util.PropertiesComponent
import com.intellij.idea.TestFor
import com.intellij.notification.NotificationGroup
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestFor(classes = [DoNotAskService::class])
class DoNotAskServiceTests {
    //region Fields

    private lateinit var propertiesComponentMock: PropertiesComponent

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkStatic(PropertiesComponent::class)

        propertiesComponentMock = mockk(relaxed = true)
        every { PropertiesComponent.getInstance() } returns propertiesComponentMock
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    //region canShowNotification tests

    @ParameterizedTest
    @MethodSource("doNotAskValues")
    fun `canShowNotification should return negated stored do not ask value`(doNotAsk: Boolean) {
        // Arrange
        val propertyId = "Notification.DoNotAsk-${PluginConstants.NotificationGroupId}"
        every { propertiesComponentMock.getBoolean(propertyId, any()) } returns doNotAsk

        // Act
        val result = DoNotAskService.canShowNotification()

        // Assert
        assertNotEquals(doNotAsk, result)
    }

    @Test
    fun `canShowNotification should set default value of do not ask to false`() {
        // Arrange
        val propertyId = "Notification.DoNotAsk-${PluginConstants.NotificationGroupId}"
        every { propertiesComponentMock.getBoolean(propertyId, any()) } returns true

        // Act
        DoNotAskService.canShowNotification()

        // Assert
        verify(exactly = 1) { propertiesComponentMock.getBoolean(any(), false) }
    }

    @Test
    fun `canShowNotification should retrieve correct do not ask id`() {
        // Arrange
        val propertyId = "Notification.DoNotAsk-${PluginConstants.NotificationGroupId}"
        every { propertiesComponentMock.getBoolean(propertyId, any()) } returns true

        // Act
        DoNotAskService.canShowNotification()

        // Assert
        verify(exactly = 1) { propertiesComponentMock.getBoolean(propertyId, any()) }
    }

    //endregion

    //region setDoNotAskFor

    @ParameterizedTest
    @MethodSource("doNotAskValues")
    fun `setDoNotAskFor should set correct value for do not ask`(doNotAsk: Boolean) {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId) } returns null
        justRun { propertiesComponentMock.setValue(any(), any<Boolean>()) }

        // Act
        DoNotAskService.setDoNotAskFor(doNotAsk)

        // Assert
        verify(exactly = 1) {
            propertiesComponentMock.setValue(
                "Notification.DoNotAsk-${PluginConstants.NotificationGroupId}",
                doNotAsk
            )
        }
    }

    @Test
    fun `setDoNotAskFor should retrieve correct notification group title`() {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId) } returns null

        // Act
        DoNotAskService.setDoNotAskFor(false)

        // Assert
        verify(exactly = 1) { NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["notificationGroup", "star wars", "progressbar"])
    @EmptySource
    fun `setDoNotAskFor should store correct display name for notification group`(displayName: String) {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId) } returns displayName
        justRun { propertiesComponentMock.setValue(any(), any<Boolean>()) }
        justRun { propertiesComponentMock.setValue(any(), any<String>()) }

        // Act
        DoNotAskService.setDoNotAskFor(false)

        "Notification.DisplayName-DoNotAsk-${PluginConstants.NotificationGroupId}"

        // Assert
        verify(exactly = 1) {
            propertiesComponentMock.setValue(
                "Notification.DisplayName-DoNotAsk-${PluginConstants.NotificationGroupId}",
                displayName
            )
        }
    }

    @Test
    fun `setDoNotAskFor should store notification group id as display name if notification group title is null`() {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId) } returns null
        justRun { propertiesComponentMock.setValue(any(), any<Boolean>()) }
        justRun { propertiesComponentMock.setValue(any(), any<String>()) }

        // Act
        DoNotAskService.setDoNotAskFor(false)

        "Notification.DisplayName-DoNotAsk-${PluginConstants.NotificationGroupId}"

        // Assert
        verify(exactly = 1) {
            propertiesComponentMock.setValue(
                "Notification.DisplayName-DoNotAsk-${PluginConstants.NotificationGroupId}",
                PluginConstants.NotificationGroupId
            )
        }
    }

    //endregion

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun doNotAskValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
            )
        }
    }

    //endregion
}
