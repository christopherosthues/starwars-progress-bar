package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class LightsaberVariantSelectorTests {
    private val threeVariants = listOf(
        listOf(CombinedId(1, 0)),
        listOf(CombinedId(1, 1)),
        listOf(CombinedId(2, 0)),
    )

    private fun lightsabers(variants: List<List<CombinedId>>, defaultVariant: Int = 0) = Lightsabers(
        "1",
        1f,
        false,
        listOf(Lightsaber(1, isDoubleBladed = false, yShift = 3, blades = listOf(blade()))),
        variants = variants,
        defaultVariant = defaultVariant,
    )

    private fun blade() = Blade(
        bladeColor = "blue",
        isShoto = false,
        isForward = true,
        bladeSize = 8,
        xBlade = 0,
        yBlade = 2,
    )

    //region Tests

    @Test
    fun `selectVariantIndex returns declared default variant when variants are disabled`() {
        val entity = lightsabers(threeVariants, defaultVariant = 2)

        assertEquals(2, LightsaberVariantSelector.selectVariantIndex(entity, enableVariants = false))
    }

    @Test
    fun `selectVariantIndex falls back to variant zero when default variant is out of range`() {
        val entity = lightsabers(threeVariants, defaultVariant = threeVariants.size)

        assertEquals(0, LightsaberVariantSelector.selectVariantIndex(entity, enableVariants = false))
    }

    @Test
    fun `selectVariantIndex falls back to variant zero when default variant is negative`() {
        val entity = lightsabers(threeVariants, defaultVariant = -1)

        assertEquals(0, LightsaberVariantSelector.selectVariantIndex(entity, enableVariants = false))
    }

    @Test
    fun `selectVariantIndex returns index within declared variants when enabled`() {
        val entity = lightsabers(threeVariants)

        repeat(100) {
            val index = LightsaberVariantSelector.selectVariantIndex(entity, enableVariants = true)
            assertTrue(index in entity.variants.indices)
        }
    }

    @Test
    fun `selectVariantIndex covers all declared variants when enabled`() {
        val entity = lightsabers(listOf(listOf(CombinedId(1, 0)), listOf(CombinedId(1, 1))))

        val selectedIndices = (1..100).mapTo(mutableSetOf<Int>()) {
            LightsaberVariantSelector.selectVariantIndex(entity, enableVariants = true)
        }

        assertEquals(setOf(0, 1), selectedIndices)
    }

    @Test
    fun `resolveDefaultVariantIndex returns declared default variant when valid`() {
        val entity = lightsabers(threeVariants, defaultVariant = 1)

        assertEquals(1, LightsaberVariantSelector.resolveDefaultVariantIndex(entity))
    }

    @Test
    fun `resolveVariant falls back to first variant for out of range index`() {
        val entity = lightsabers(threeVariants)

        assertEquals(entity.variants.first(), LightsaberVariantSelector.resolveVariant(entity, threeVariants.size))
    }

    @Test
    fun `resolveVariant returns implicit single blade variant when no variants are declared`() {
        val entity = lightsabers(emptyList())

        assertEquals(listOf(CombinedId(1, 0)), LightsaberVariantSelector.resolveVariant(entity, 0))
    }

    //endregion
}
