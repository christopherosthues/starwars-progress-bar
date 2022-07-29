package com.christopherosthues.starwarsprogressbar.ui.shapes

import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.awt.Dimension
import java.awt.Insets
import java.util.stream.Stream
import kotlin.random.Random

@TestFor(classes = [IconShape::class])
class IconShapeTests {
    @ParameterizedTest
    @MethodSource("xValues")
    fun `constructor should set x to x plus default left padding + left padding`(x: Int, leftInset: Int, expectedX: Int) {
        // Arrange
        val y = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()
        val topInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val iconShape = IconShape(x, y, Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset))

        // Assert
        assertEquals(expectedX, iconShape.x)
    }

    @ParameterizedTest
    @MethodSource("yForTopPaddingLessThanBaseMarginValues")
    fun `constructor should set y to y if top padding is less than base margin`(y: Int, topInset: Int) {
        // Arrange
        val x = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()
        val leftInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val iconShape = IconShape(x, y, Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset))

        // Assert
        assertEquals(y, iconShape.y)
    }

    @ParameterizedTest
    @MethodSource("yForTopPaddingGreaterThanOrEqualToBaseMarginValues")
    fun `constructor should set y to y plus top padding if top padding is greater than or equal to base margin`(y: Int, topInset: Int, expectedY: Int) {
        // Arrange
        val x = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()
        val leftInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val iconShape = IconShape(x, y, Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset))

        // Assert
        assertEquals(expectedY, iconShape.y)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1, 1, 5, 10])
    fun `constructor should set width to width of dimension`(width: Int) {
        // Arrange
        val x = Random.nextInt()
        val y = Random.nextInt()
        val height = Random.nextInt()
        val topInset = Random.nextInt()
        val leftInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val iconShape = IconShape(x, y, Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset))

        // Assert
        assertEquals(width, iconShape.width)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1, 1, 5, 10])
    fun `constructor should set height to height of dimension`(height: Int) {
        // Arrange
        val x = Random.nextInt()
        val y = Random.nextInt()
        val width = Random.nextInt()
        val topInset = Random.nextInt()
        val leftInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val iconShape = IconShape(x, y, Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset))

        // Assert
        assertEquals(height, iconShape.height)
    }

    //region Test case data

    companion object {
        @JvmStatic
        fun xValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 8),
                Arguments.of(0, 2, 10),
                Arguments.of(-10, 5, 3),
                Arguments.of(-5, 3, 6)
            )
        }

        @JvmStatic
        fun yForTopPaddingLessThanBaseMarginValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(0, -2),
                Arguments.of(-10, 0),
                Arguments.of(-5, -13)
            )
        }

        @JvmStatic
        fun yForTopPaddingGreaterThanOrEqualToBaseMarginValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 2, 2),
                Arguments.of(0, 3, 3),
                Arguments.of(-10, 8, -2),
                Arguments.of(-5, 2, -3)
            )
        }
    }

    //endregion
}
