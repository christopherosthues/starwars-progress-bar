package com.christopherosthues.starwarsprogressbar.constants

import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

@TestFor(classes = [PluginConstants::class])
class PluginConstantsTests {
    //region Tests

    @ParameterizedTest
    @MethodSource("pluginConstants")
    fun `constants should return correct values`(expectedConstant: String, constant: String) {
        // Arrange

        // Act and Assert
        assertEquals(expectedConstant, constant)
    }

    @Test
    fun `all public constants should be tested`() {
        // Arrange
        val pluginConstantMembers =
            PluginConstants::class.memberProperties.filter { it.visibility != KVisibility.PRIVATE }

        // Act and Assert
        assertEquals(pluginConstantMembers.size, pluginConstants().count().toInt())
    }

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun pluginConstants(): Stream<Arguments> = Stream.of(
            Arguments.of("com.christopherosthues.starwarsprogressbar", PluginConstants.PLUGIN_ID),
            Arguments.of(
                "com.christopherosthues.starwarsprogressbar.ui.configuration",
                PluginConstants.PLUGIN_SEARCH_ID,
            ),
            Arguments.of("Star Wars Progress Bar updated", PluginConstants.NOTIFICATION_GROUP_ID),
        )
    }

    //endregion
}
