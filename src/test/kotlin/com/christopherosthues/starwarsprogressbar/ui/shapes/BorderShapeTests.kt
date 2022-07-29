package com.christopherosthues.starwarsprogressbar.ui.shapes

import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

@TestFor(classes = [BorderShape::class])
class BorderShapeTests {
    @ParameterizedTest
    @MethodSource("xAndYValues")
    fun `constructor should set x to x plus margin`(x: Int, margin: Int, expectedX: Int) {
        // Arrange
        val y = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, margin)

        // Assert
        assertEquals(expectedX, borderShape.x)
    }

    @ParameterizedTest
    @MethodSource("xAndYValues")
    fun `constructor should set y to y plus margin`(y: Int, margin: Int, expectedY: Int) {
        // Arrange
        val x = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, margin)

        // Assert
        assertEquals(expectedY, borderShape.y)
    }

    @ParameterizedTest
    @MethodSource("topInsetLessThanMarginForYValues")
    fun `constructor should set y to y plus margin minus top padding if top padding is less than margin`(
        y: Int,
        margin: Int,
        topInset: Int,
        expectedY: Int
    ) {
        // Arrange
        val x = Random.nextInt()
        val width = Random.nextInt()
        val height = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, topInset)

        // Assert
        assertEquals(expectedY, borderShape.y)
    }

    @ParameterizedTest
    @MethodSource("widthAndHeightValues")
    fun `constructor should set width to width minus two times of the margin`(
        width: Int,
        margin: Int,
        expectedWidth: Int
    ) {
        // Arrange
        val x = Random.nextInt()
        val y = Random.nextInt()
        val height = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, margin)

        // Assert
        assertEquals(expectedWidth, borderShape.width)
    }

    @ParameterizedTest
    @MethodSource("widthAndHeightValues")
    fun `constructor should set height to height minus two times of the margin`(
        height: Int,
        margin: Int,
        expectedHeight: Int
    ) {
        // Arrange
        val x = Random.nextInt()
        val y = Random.nextInt()
        val width = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, margin)

        // Assert
        assertEquals(expectedHeight, borderShape.height)
    }

    @ParameterizedTest
    @MethodSource("topInsetLessThanMarginForHeightValues")
    fun `constructor should set height to height minus two times of the margin plus top padding if top padding is less than margin`(
        height: Int,
        margin: Int,
        topInset: Int,
        expectedHeight: Int
    ) {
        // Arrange
        val x = Random.nextInt()
        val y = Random.nextInt()
        val width = Random.nextInt()

        // Act
        val borderShape = BorderShape(x, y, width, height, margin, topInset)

        // Assert
        assertEquals(expectedHeight, borderShape.height)
    }

    //region Test case data

    companion object {
        @JvmStatic
        fun xAndYValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(1, 0, 1),
                Arguments.of(-1, 0, -1),
                Arguments.of(-1, 1, 0),
                Arguments.of(1, -1, 0),
                Arguments.of(10, -1, 9),
                Arguments.of(10, 5, 15)
            )
        }

        @JvmStatic
        fun widthAndHeightValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(1, 0, 1),
                Arguments.of(-1, 0, -1),
                Arguments.of(-1, 1, -3),
                Arguments.of(1, -1, 3),
                Arguments.of(10, -1, 12),
                Arguments.of(10, 5, 0)
            )
        }

        @JvmStatic
        fun topInsetLessThanMarginForYValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, -1, 1),
                Arguments.of(1, 0, -5, 6),
                Arguments.of(-1, 0, -3, 2),
                Arguments.of(-1, 1, 0, 0),
                Arguments.of(1, -1, -9, 9),
                Arguments.of(10, -1, -54, 63),
                Arguments.of(10, 5, 3, 12)
            )
        }

        @JvmStatic
        fun topInsetLessThanMarginForHeightValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, -1, -1),
                Arguments.of(1, 0, -5, -4),
                Arguments.of(-1, 0, -3, -4),
                Arguments.of(-1, 1, 0, -3),
                Arguments.of(1, -1, -9, -6),
                Arguments.of(10, -1, -54, -42),
                Arguments.of(10, 5, 3, 3)
            )
        }
    }

    //endregion
}
