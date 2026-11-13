package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LightsaberLayoutCalculatorTests {
    private fun blade(xBlade: Int, yBlade: Int, bladeSize: Int, isShoto: Boolean = false) =
        Blade(bladeColor = "blue", isShoto = isShoto, bladeSize = bladeSize, xBlade = xBlade, yBlade = yBlade)

    private fun lightsaber(id: Int, isDoubleBladed: Boolean, yShift: Int, vararg blades: Blade) =
        Lightsaber(id, isDoubleBladed, yShift, blades.toList())

    private fun lightsabers(vararg sabers: Lightsaber) = Lightsabers("1", 1f, false, sabers.toList())

    //region Tests

    @Test
    fun `compute should return left blade geometry for single bladed odd id lightsaber`() {
        val saber = lightsaber(1, isDoubleBladed = false, yShift = 3, blade(xBlade = 200, yBlade = 2, bladeSize = 8))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(200, 2, 8, false),
            x = 300,
            y = 5,
            width = 100,
            height = 8,
            borderX = 300,
            borderWidth = 500,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), HiltGeometry(x = 0, y = 3)), result.single())
    }

    @Test
    fun `compute should return right blade geometry for single bladed even id lightsaber`() {
        val saber = lightsaber(2, isDoubleBladed = false, yShift = 1, blade(xBlade = -200, yBlade = 2, bladeSize = 8))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(-200, 2, 8, false),
            x = 0,
            y = 3,
            width = 100,
            height = 8,
            borderX = -400,
            borderWidth = 500,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), HiltGeometry(x = 300, y = 1)), result.single())
    }

    @Test
    fun `compute should halve blade and border width for shoto lightsaber`() {
        val shotoBlade = blade(xBlade = 200, yBlade = 2, bladeSize = 8, isShoto = true)
        val saber = lightsaber(1, isDoubleBladed = false, yShift = 3, shotoBlade)

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(200, 2, 8, true),
            x = 300,
            y = 5,
            width = 50,
            height = 8,
            borderX = 300,
            borderWidth = 250,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), HiltGeometry(x = 0, y = 3)), result.single())
    }

    @Test
    fun `compute should return left and right blade geometry for double bladed lightsaber`() {
        val saber = lightsaber(1, isDoubleBladed = true, yShift = 3, blade(-100, 2, 8, false), blade(100, 4, 6, false))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val leftBlade = BladeGeometry(
            blade(-100, 2, 8, false),
            x = 10,
            y = 5,
            width = 40,
            height = 8,
            borderX = -150,
            borderWidth = 200,
        )
        val rightBlade = BladeGeometry(
            blade(100, 4, 6, false),
            x = 350,
            y = 7,
            width = 40,
            height = 6,
            borderX = 350,
            borderWidth = 200,
        )

        assertEquals(LightsaberLayout(listOf(leftBlade, rightBlade), HiltGeometry(x = 150, y = 3)), result.single())
    }

    @Test
    fun `compute should quarter shoto blade border width for double bladed lightsaber`() {
        val saber = lightsaber(1, isDoubleBladed = true, yShift = 3, blade(-100, 2, 8, true), blade(100, 4, 6, false))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val leftBlade = BladeGeometry(
            blade(-100, 2, 8, true),
            x = 30,
            y = 5,
            width = 20,
            height = 8,
            borderX = -50,
            borderWidth = 100,
        )
        val rightBlade = BladeGeometry(
            blade(100, 4, 6, false),
            x = 350,
            y = 7,
            width = 40,
            height = 6,
            borderX = 350,
            borderWidth = 200,
        )

        assertEquals(LightsaberLayout(listOf(leftBlade, rightBlade), HiltGeometry(x = 150, y = 3)), result.single())
    }

    @Test
    fun `compute should return no hilt geometry when icon is not shown`() {
        val saber = lightsaber(1, isDoubleBladed = false, yShift = 3, blade(xBlade = 200, yBlade = 2, bladeSize = 8))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = false,
        )

        val expectedBlade = BladeGeometry(
            blade(200, 2, 8, false),
            x = 0,
            y = 5,
            width = 80,
            height = 8,
            borderX = 0,
            borderWidth = 400,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), null), result.single())
    }

    @Test
    fun `compute should use first blade only for double bladed lightsaber without hilt`() {
        val saber = lightsaber(2, isDoubleBladed = true, yShift = 3, blade(-100, 2, 8, false), blade(100, 4, 6, false))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = false,
        )

        val expectedBlade = BladeGeometry(
            blade(-100, 2, 8, false),
            x = 320,
            y = 5,
            width = 80,
            height = 8,
            borderX = 0,
            borderWidth = 400,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), null), result.single())
    }

    @Test
    fun `compute should return no hilt geometry when icon does not fit`() {
        val saber = lightsaber(1, isDoubleBladed = false, yShift = 3, blade(xBlade = 200, yBlade = 2, bladeSize = 8))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(350),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(200, 2, 8, false),
            x = 0,
            y = 5,
            width = 80,
            height = 8,
            borderX = 0,
            borderWidth = 400,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), null), result.single())
    }

    @Test
    fun `compute should return no hilt geometry for double bladed lightsaber when icon does not fit`() {
        val saber = lightsaber(1, isDoubleBladed = true, yShift = 3, blade(-100, 2, 8, false))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(saber),
            iconWidths = listOf(100),
            width = 150,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(-100, 2, 8, false),
            x = 0,
            y = 5,
            width = 80,
            height = 8,
            borderX = 0,
            borderWidth = 150,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), null), result.single())
    }

    @Test
    fun `compute should use max icon widths of single bladed lightsabers for all layouts`() {
        val leftSaber = lightsaber(1, isDoubleBladed = false, yShift = 3, blade(-40, 2, 8, false))
        val rightSaber = lightsaber(2, isDoubleBladed = false, yShift = 1, blade(-40, 2, 8, false))

        val result = LightsaberLayoutCalculator.compute(
            lightsabers(leftSaber, rightSaber),
            iconWidths = listOf(60, 80),
            width = 400,
            amountFull = 100,
            showIcon = true,
        )

        val leftBlade = BladeGeometry(
            blade(-40, 2, 8, false),
            x = 20,
            y = 5,
            width = 75,
            height = 8,
            borderX = 20,
            borderWidth = 300,
        )
        val rightBlade = BladeGeometry(
            blade(-40, 2, 8, false),
            x = 205,
            y = 3,
            width = 75,
            height = 8,
            borderX = -20,
            borderWidth = 300,
        )

        val expectedLayouts = listOf(
            LightsaberLayout(listOf(leftBlade), HiltGeometry(x = 0, y = 3)),
            LightsaberLayout(listOf(rightBlade), HiltGeometry(x = 320, y = 1)),
        )

        assertEquals(expectedLayouts, result)
    }

    //endregion
}
