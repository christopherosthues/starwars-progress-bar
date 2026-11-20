package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LightsaberLayoutCalculatorTests {
    private fun blade(xBlade: Int, yBlade: Int, bladeSize: Int, isShoto: Boolean = false, isForward: Boolean = true) =
        Blade(
            bladeColor = "blue",
            isShoto = isShoto,
            isForward = isForward,
            bladeSize = bladeSize,
            xBlade = xBlade,
            yBlade = yBlade,
        )

    private fun lightsaber(id: Int, isDoubleBladed: Boolean, yShift: Int, vararg blades: Blade) =
        Lightsaber(id, isDoubleBladed, yShift, blades.toList())

    private fun doubleBladedVariantEntity() = Lightsabers(
        "1",
        1f,
        false,
        listOf(
            lightsaber(
                1,
                isDoubleBladed = true,
                yShift = 3,
                blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
                blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            ),
        ),
        variants = listOf(
            listOf(CombinedId(1, 0), CombinedId(1, 1)),
            listOf(CombinedId(1, 0)),
            listOf(CombinedId(1, 1)),
        ),
    )

    private fun jarKaiVariantEntity() = Lightsabers(
        "1",
        1f,
        true,
        listOf(
            lightsaber(1, isDoubleBladed = false, yShift = 3, blade(xBlade = 50, yBlade = 2, bladeSize = 8)),
            lightsaber(
                2,
                isDoubleBladed = false,
                yShift = 1,
                blade(xBlade = 50, yBlade = 2, bladeSize = 8, isForward = false),
            ),
        ),
        variants = listOf(
            listOf(CombinedId(1, 0), CombinedId(2, 0)),
            listOf(CombinedId(1, 0)),
            listOf(CombinedId(2, 0)),
        ),
    )

    private fun jarKaiDoubleBladedEntity() = Lightsabers(
        "1",
        1f,
        true,
        listOf(
            lightsaber(
                1,
                isDoubleBladed = true,
                yShift = 3,
                blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
                blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            ),
            lightsaber(
                2,
                isDoubleBladed = true,
                yShift = 1,
                blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
                blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            ),
        ),
        variants = listOf(
            listOf(CombinedId(1, 0), CombinedId(1, 1), CombinedId(2, 0), CombinedId(2, 1)),
        ),
    )

    //region Tests

    @Test
    fun `compute should render full double blade variant centered in the progress bar`() {
        val result = LightsaberLayoutCalculator.compute(
            doubleBladedVariantEntity(),
            selectedVariantIndex = 0,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val leftBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 121,
            y = 5,
            width = 30,
            height = 8,
            borderX = 0,
            borderWidth = 151,
        )
        val rightBlade = BladeGeometry(
            blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            x = 249,
            y = 5,
            width = 30,
            height = 8,
            borderX = 249,
            borderWidth = 151,
        )

        assertEquals(LightsaberLayout(listOf(leftBlade, rightBlade), HiltGeometry(x = 150, y = 3)), result.single())
    }

    @Test
    fun `compute should render left blade variant backward with hilt at right edge`() {
        val result = LightsaberLayoutCalculator.compute(
            doubleBladedVariantEntity(),
            selectedVariantIndex = 1,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 241,
            y = 5,
            width = 60,
            height = 8,
            borderX = 0,
            borderWidth = 301,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), HiltGeometry(x = 300, y = 3)), result.single())
    }

    @Test
    fun `compute should render right blade variant forward with hilt at left edge`() {
        val result = LightsaberLayoutCalculator.compute(
            doubleBladedVariantEntity(),
            selectedVariantIndex = 2,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(-1, 2, 8, false),
            x = 99,
            y = 5,
            width = 60,
            height = 8,
            borderX = 99,
            borderWidth = 301,
        )

        assertEquals(LightsaberLayout(listOf(expectedBlade), HiltGeometry(x = 0, y = 3)), result.single())
    }

    @Test
    fun `compute should halve shoto blade and border width for single blade variant`() {
        val saber = lightsaber(
            1,
            isDoubleBladed = false,
            yShift = 3,
            blade(xBlade = 200, yBlade = 2, bladeSize = 8, isShoto = true),
        )

        val result = LightsaberLayoutCalculator.compute(
            Lightsabers("1", 1f, false, listOf(saber)),
            selectedVariantIndex = 0,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(xBlade = 200, yBlade = 2, bladeSize = 8, isShoto = true),
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
    fun `compute should return no hilt geometry when icon does not fit`() {
        val saber = lightsaber(1, isDoubleBladed = false, yShift = 3, blade(xBlade = 200, yBlade = 2, bladeSize = 8))

        val result = LightsaberLayoutCalculator.compute(
            Lightsabers("1", 1f, false, listOf(saber)),
            selectedVariantIndex = 0,
            iconWidths = listOf(450),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val expectedBlade = BladeGeometry(
            blade(xBlade = 200, yBlade = 2, bladeSize = 8),
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
    fun `compute should render left blade variant backward without hilt when icon is not shown`() {
        val result = LightsaberLayoutCalculator.compute(
            doubleBladedVariantEntity(),
            selectedVariantIndex = 1,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = false,
        )

        val expectedBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
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
    fun `compute should use blade isForward for direction instead of blade index`() {
        val entity = Lightsabers(
            "1",
            1f,
            false,
            listOf(
                lightsaber(
                    1,
                    isDoubleBladed = true,
                    yShift = 3,
                    blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = true),
                    blade(xBlade = -1, yBlade = 2, bladeSize = 8, isForward = false),
                ),
            ),
            variants = listOf(
                listOf(CombinedId(1, 0), CombinedId(1, 1)),
                listOf(CombinedId(1, 0)),
                listOf(CombinedId(1, 1)),
            ),
        )

        val forwardResult = LightsaberLayoutCalculator.compute(
            entity,
            selectedVariantIndex = 1,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val forwardBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = true),
            x = 101,
            y = 5,
            width = 60,
            height = 8,
            borderX = 101,
            borderWidth = 301,
        )

        assertEquals(LightsaberLayout(listOf(forwardBlade), HiltGeometry(x = 0, y = 3)), forwardResult.single())

        val backwardResult = LightsaberLayoutCalculator.compute(
            entity,
            selectedVariantIndex = 2,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val backwardBlade = BladeGeometry(
            blade(xBlade = -1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 239,
            y = 5,
            width = 60,
            height = 8,
            borderX = -2,
            borderWidth = 301,
        )

        assertEquals(LightsaberLayout(listOf(backwardBlade), HiltGeometry(x = 300, y = 3)), backwardResult.single())
    }

    @Test
    fun `compute should render jar kai both sabers with shared space between hilts`() {
        val result = LightsaberLayoutCalculator.compute(
            jarKaiVariantEntity(),
            selectedVariantIndex = 0,
            iconWidths = listOf(100, 50),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val forwardBlade = BladeGeometry(
            blade(xBlade = 50, yBlade = 2, bladeSize = 8),
            x = 150,
            y = 5,
            width = 60,
            height = 8,
            borderX = 150,
            borderWidth = 300,
        )
        val backwardBlade = BladeGeometry(
            blade(xBlade = 50, yBlade = 2, bladeSize = 8, isForward = false),
            x = 340,
            y = 3,
            width = 60,
            height = 8,
            borderX = 100,
            borderWidth = 300,
        )

        val expectedLayouts = listOf(
            LightsaberLayout(listOf(forwardBlade), HiltGeometry(x = 0, y = 3)),
            LightsaberLayout(listOf(backwardBlade), HiltGeometry(x = 350, y = 1)),
        )

        assertEquals(expectedLayouts, result)
    }

    @Test
    fun `compute should render jar kai single blade variant like standard single bladed`() {
        val forwardResult = LightsaberLayoutCalculator.compute(
            jarKaiVariantEntity(),
            selectedVariantIndex = 1,
            iconWidths = listOf(100, 50),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val forwardBlade = BladeGeometry(
            blade(xBlade = 50, yBlade = 2, bladeSize = 8),
            x = 150,
            y = 5,
            width = 70,
            height = 8,
            borderX = 150,
            borderWidth = 350,
        )

        assertEquals(LightsaberLayout(listOf(forwardBlade), HiltGeometry(x = 0, y = 3)), forwardResult.single())

        val backwardResult = LightsaberLayoutCalculator.compute(
            jarKaiVariantEntity(),
            selectedVariantIndex = 2,
            iconWidths = listOf(100, 50),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val backwardBlade = BladeGeometry(
            blade(xBlade = 50, yBlade = 2, bladeSize = 8, isForward = false),
            x = 320,
            y = 3,
            width = 80,
            height = 8,
            borderX = 0,
            borderWidth = 400,
        )

        assertEquals(LightsaberLayout(listOf(backwardBlade), HiltGeometry(x = 350, y = 1)), backwardResult.single())
    }

    @Test
    fun `compute should render single saber jar kai full variant centered like a normal double bladed saber`() {
        val entity = Lightsabers(
            "1",
            1f,
            true,
            listOf(
                lightsaber(
                    1,
                    isDoubleBladed = true,
                    yShift = 3,
                    blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
                    blade(xBlade = -1, yBlade = 2, bladeSize = 8),
                ),
            ),
            variants = listOf(
                listOf(CombinedId(1, 0), CombinedId(1, 1)),
            ),
        )

        val result = LightsaberLayoutCalculator.compute(
            entity,
            selectedVariantIndex = 0,
            iconWidths = listOf(100),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val leftBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 121,
            y = 5,
            width = 30,
            height = 8,
            borderX = 0,
            borderWidth = 151,
        )
        val rightBlade = BladeGeometry(
            blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            x = 249,
            y = 5,
            width = 30,
            height = 8,
            borderX = 249,
            borderWidth = 151,
        )

        assertEquals(LightsaberLayout(listOf(leftBlade, rightBlade), HiltGeometry(x = 150, y = 3)), result.single())
    }

    @Test
    fun `compute should render jar kai with all full double bladed sabers centered with all hilts in the middle`() {
        val result = LightsaberLayoutCalculator.compute(
            jarKaiDoubleBladedEntity(),
            selectedVariantIndex = 0,
            iconWidths = listOf(100, 50),
            width = 400,
            amountFull = 80,
            showIcon = true,
        )

        val firstLeftBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 121,
            y = 5,
            width = 30,
            height = 8,
            borderX = 0,
            borderWidth = 151,
        )
        val firstRightBlade = BladeGeometry(
            blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            x = 249,
            y = 5,
            width = 30,
            height = 8,
            borderX = 249,
            borderWidth = 151,
        )
        val secondLeftBlade = BladeGeometry(
            blade(xBlade = 1, yBlade = 2, bladeSize = 8, isForward = false),
            x = 141,
            y = 3,
            width = 35,
            height = 8,
            borderX = 0,
            borderWidth = 176,
        )
        val secondRightBlade = BladeGeometry(
            blade(xBlade = -1, yBlade = 2, bladeSize = 8),
            x = 224,
            y = 3,
            width = 35,
            height = 8,
            borderX = 224,
            borderWidth = 176,
        )

        val expectedLayouts = listOf(
            LightsaberLayout(listOf(firstLeftBlade, firstRightBlade), HiltGeometry(x = 150, y = 3)),
            LightsaberLayout(listOf(secondLeftBlade, secondRightBlade), HiltGeometry(x = 175, y = 1)),
        )

        assertEquals(expectedLayouts, result)
    }

    //endregion
}
