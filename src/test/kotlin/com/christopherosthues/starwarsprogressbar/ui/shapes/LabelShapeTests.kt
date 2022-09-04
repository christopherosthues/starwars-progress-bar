package com.christopherosthues.starwarsprogressbar.ui.shapes

import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Dimension
import java.awt.Insets
import java.util.stream.Stream
import kotlin.random.Random

@TestFor(classes = [LabelShape::class])
class LabelShapeTests {
    //region Test lifecycle

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("widthValues")
    fun `constructor should set width to maximum of width and width minus left padding and right padding`(
        width: Int,
        leftInset: Int,
        rightInset: Int,
        expectedWidth: Int
    ) {
        // Arrange
        val iconShapeMock = mockk<IconShape>()
        every { iconShapeMock.x } returns Random.nextInt()
        every { iconShapeMock.y } returns Random.nextInt()
        every { iconShapeMock.width } returns Random.nextInt()
        every { iconShapeMock.height } returns Random.nextInt()
        val height = Random.nextInt()
        val topInset = Random.nextInt()
        val bottomInset = Random.nextInt()

        // Act
        val labelShape =
            LabelShape(Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset), iconShapeMock)

        // Assert
        assertEquals(expectedWidth, labelShape.width)
    }

    @RepeatedTest(10)
    fun `constructor should set height to height of dimension`() {
        // Arrange
        val iconShapeMock = mockk<IconShape>()
        every { iconShapeMock.x } returns Random.nextInt()
        every { iconShapeMock.y } returns Random.nextInt()
        every { iconShapeMock.width } returns Random.nextInt()
        every { iconShapeMock.height } returns Random.nextInt()
        val height = Random.nextInt()
        val width = Random.nextInt()
        val topInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val leftInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val labelShape =
            LabelShape(Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset), iconShapeMock)

        // Assert
        assertEquals(height, labelShape.height)
    }

    @ParameterizedTest()
    @MethodSource("xValues")
    fun `constructor should set x to x of icon shape plus width of icon shape plus icon test spacing`(
        iconShapeX: Int,
        iconShapeWidth: Int,
        expectedX: Int
    ) {
        // Arrange
        val iconShapeMock = mockk<IconShape>()
        every { iconShapeMock.x } returns iconShapeX
        every { iconShapeMock.y } returns Random.nextInt()
        every { iconShapeMock.width } returns iconShapeWidth
        every { iconShapeMock.height } returns Random.nextInt()
        val height = Random.nextInt()
        val width = Random.nextInt()
        val topInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val leftInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val labelShape =
            LabelShape(Dimension(width, height), Insets(topInset, leftInset, bottomInset, rightInset), iconShapeMock)

        // Assert
        assertEquals(expectedX, labelShape.x)
    }

    @ParameterizedTest()
    @MethodSource("yValues")
    fun `constructor should set y to y of icon shape plus half icon shape height minus half dimension height`(
        iconShapeY: Int,
        iconShapeHeight: Int,
        dimensionHeight: Int,
        expectedY: Int
    ) {
        // Arrange
        val iconShapeMock = mockk<IconShape>()
        every { iconShapeMock.x } returns Random.nextInt()
        every { iconShapeMock.y } returns iconShapeY
        every { iconShapeMock.width } returns Random.nextInt()
        every { iconShapeMock.height } returns iconShapeHeight
        val width = Random.nextInt()
        val topInset = Random.nextInt()
        val bottomInset = Random.nextInt()
        val leftInset = Random.nextInt()
        val rightInset = Random.nextInt()

        // Act
        val labelShape = LabelShape(
            Dimension(width, dimensionHeight),
            Insets(topInset, leftInset, bottomInset, rightInset),
            iconShapeMock
        )

        // Assert
        assertEquals(expectedY, labelShape.y)
    }

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun widthValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 0, 0),
                Arguments.of(1, 0, 0, 1),
                Arguments.of(1, -1, 0, 2),
                Arguments.of(1, -5, 0, 6),
                Arguments.of(1, 1, 0, 1),
                Arguments.of(1, 0, -1, 2),
                Arguments.of(1, 0, -5, 6),
                Arguments.of(-3, 1, 2, -3),
                Arguments.of(3, -2, 2, 3)
            )
        }

        @JvmStatic
        fun xValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 5),
                Arguments.of(1, 0, 6),
                Arguments.of(1, -1, 5),
                Arguments.of(1, -5, 1),
                Arguments.of(1, 1, 7),
                Arguments.of(-3, 1, 3),
                Arguments.of(3, -2, 6),
                Arguments.of(-3, -2, 0),
                Arguments.of(-30, -2, -27)
            )
        }

        @JvmStatic
        fun yValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, 0, 0, 0),
                Arguments.of(1, 0, 6, -2),
                Arguments.of(1, -1, 5, -1),
                Arguments.of(1, -5, 1, -1),
                Arguments.of(1, 1, 7, -2),
                Arguments.of(-3, 1, 3, -4),
                Arguments.of(3, -2, 6, -1),
                Arguments.of(-3, -2, 0, -4),
                Arguments.of(-30, -2, -27, -18)
            )
        }
    }

    //endregion
}
