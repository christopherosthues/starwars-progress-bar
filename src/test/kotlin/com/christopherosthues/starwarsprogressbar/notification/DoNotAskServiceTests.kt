package com.christopherosthues.starwarsprogressbar.notification

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.ide.util.PropertiesComponent
import com.intellij.idea.TestFor
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.jetbrains.annotations.NonNls
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestFor(classes = [DoNotAskService::class])
class DoNotAskServiceTests {

    private lateinit var propertiesComponentMock: TestPropertiesComponent

    @BeforeEach
    fun setUp() {
        // Clear any previous test state
        val applicationMock = mockk<Application>(relaxed = true)
        propertiesComponentMock = TestPropertiesComponent()
        every { applicationMock.getService<PropertiesComponent>(any()) } returns(propertiesComponentMock)
        ApplicationManager.setApplication(applicationMock)
    }

    @AfterEach
    fun tearDown() {
        // Clean up after each test
        ApplicationManager.setApplication(null)
    }

    @ParameterizedTest
    @MethodSource("doNotAskValues")
    fun `canShowNotification returns correct do not ask value`(doNotAsk: Boolean) {
        // Arrange
        val id = "Notification.DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}"
        propertiesComponentMock.setValue(id, doNotAsk)

        // Act
        val result = DoNotAskService.canShowNotification()

        // Assert
        assertEquals(!doNotAsk, result)
    }

    @ParameterizedTest
    @MethodSource("doNotAskValues")
    fun `setDoNotAskFor should set correct value for do not ask and title`(doNotAsk: Boolean) {
        // Arrange
        mockkObject(NotificationGroup)
        val title = "title"
        every { NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID) } returns title

        // Act
        DoNotAskService.setDoNotAskFor(doNotAsk)

        // Assert
        val resultDoNotAsk = propertiesComponentMock.getBoolean("Notification.DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}")
        val resultTitle = propertiesComponentMock.getValue("Notification.DisplayName-DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}")
        assertAll(
            { assertEquals(doNotAsk, resultDoNotAsk) },
            { assertEquals(title, resultTitle) }
        )
    }

    @Test
    fun `setDoNotAskFor should retrieve correct notification group title`() {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID) } returns null

        // Act
        DoNotAskService.setDoNotAskFor(false)

        // Assert
        verify(exactly = 1) { NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID) }
    }

    @Test
    fun `setDoNotAskFor should store notification group id as display name if notification group title is null`() {
        // Arrange
        mockkObject(NotificationGroup)
        every { NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID) } returns null

        // Act
        DoNotAskService.setDoNotAskFor(false)

        // Assert
        val result = propertiesComponentMock.getValue("Notification.DisplayName-DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}")
        assertEquals(PluginConstants.NOTIFICATION_GROUP_ID, result)
    }

    //region Test case data

    companion object {
        @JvmStatic
        fun doNotAskValues(): Stream<Arguments> = Stream.of(
            Arguments.of(true),
            Arguments.of(false),
        )
    }

    //endregion

    private class TestPropertiesComponent : PropertiesComponent() {
        private val values = mutableMapOf<String, String?>()

        override fun unsetValue(name: String) {
            values.remove(name)
        }

        override fun getValue(name: String): String? {
            return values[name]
        }

        override fun setValue(name: String, value: String?) {
            values[name] = value
        }

        override fun setValue(name: String, value: String?, defaultValue: String?) {
        }

        override fun setValue(name: String, value: Float, defaultValue: Float) {
        }

        override fun setValue(name: String, value: Int, defaultValue: Int) {
        }

        override fun setValue(name: String, value: Boolean, defaultValue: Boolean) {
            values[name] = value.toString()
        }

        override fun getValues(p0: @NonNls String): Array<out String?>? {
            return null
        }

        override fun setValues(
            p0: @NonNls String,
            p1: Array<out String?>?
        ) {
        }

        override fun getList(p0: @NonNls String): List<String?>? {
            return listOf<String?>()
        }

        override fun setList(
            p0: @NonNls String,
            p1: Collection<String?>?
        ) {
        }

        override fun updateValue(
            p0: @NonNls String,
            p1: Boolean
        ): Boolean {
            return false
        }

        override fun isValueSet(name: String): Boolean {
            return values.containsKey(name)
        }
    }
}
