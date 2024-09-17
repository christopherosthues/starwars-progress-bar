package com.christopherosthues.starwarsprogressbar.ui

import com.intellij.idea.TestFor
import com.intellij.ui.JBColor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import java.awt.Color
import java.util.stream.Stream

@TestFor(classes = [IonEngineColor::class])
class IonEngineColorTests {
    @ParameterizedTest
    @MethodSource("colors")
    fun `ion engine colors should return correct colors`(colorName: String, expectedColor: JBColor) {
        // Arrange

        // Act and Assert
        assertEquals(expectedColor, IonEngineColor.colors[colorName])
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = ["cyan", "blueb", "bl ue"])
    fun `ion engine colors should return null if color is not defined`(colorName: String?) {
        // Arrange

        // Act and Assert
        assertNull(IonEngineColor.colors[colorName])
    }

    @Test
    fun `all ion engine colors should be tested`() {
        // Arrange

        // Act and Assert
        assertEquals(IonEngineColor.colors.size, colors().count().toInt())
    }

    companion object {
        @JvmStatic
        fun colors(): Stream<Arguments> = Stream.of(
            Arguments.of("blue", JBColor(Color(74, 228, 220), Color(74, 228, 220))),
            Arguments.of("brown", JBColor(Color(117, 80, 62), Color(117, 80, 62))),
            Arguments.of("green", JBColor(Color(85, 255, 36), Color(85, 255, 36))),
            Arguments.of("orange", JBColor(Color(240, 200, 91), Color(240, 200, 91))),
            Arguments.of("purple", JBColor(Color(255, 73, 245), Color(255, 73, 245))),
            Arguments.of("red", JBColor(Color(255, 107, 107), Color(255, 107, 107))),
            Arguments.of("yellow", JBColor(Color(245, 238, 60), Color(245, 238, 60))),
            Arguments.of("white", JBColor(Color(255, 255, 255), Color(255, 255, 255))),
        )
    }
}
