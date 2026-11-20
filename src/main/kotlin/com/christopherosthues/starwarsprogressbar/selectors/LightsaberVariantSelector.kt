package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import java.util.concurrent.ThreadLocalRandom

/**
 * Selects the variant index for a lightsaber entity.
 *
 * When the global lightsaber variants toggle is enabled, this selector randomly chooses
 * from each entity's declared `variants: List<List<CombinedId>>` list. The selected index
 * persists until the next entity selection. When the toggle is disabled, the entity's
 * declared default variant ([Lightsabers.defaultVariant]) is used instead.
 */
internal object LightsaberVariantSelector {

    /**
     * Selects the variant index for rendering.
     *
     * @param lightsabers The lightsaber entity with declared variants
     * @param enableVariants Whether the global variant toggle is enabled
     * @return The selected variant index (0-based); the default variant when disabled
     */
    fun selectVariantIndex(lightsabers: Lightsabers, enableVariants: Boolean): Int {
        val variants = lightsabers.variants
        return if (variants.isEmpty()) {
            0
        } else if (enableVariants) {
            ThreadLocalRandom.current().nextInt(variants.size)
        } else {
            resolveDefaultVariantIndex(lightsabers)
        }
    }

    /**
     * Resolves the entity's declared default variant index.
     *
     * @param lightsabers The lightsaber entity with a declared default variant
     * @return The default variant index, falling back to 0 when the declared value is invalid
     */
    fun resolveDefaultVariantIndex(lightsabers: Lightsabers): Int =
        if (lightsabers.defaultVariant in lightsabers.variants.indices) {
            lightsabers.defaultVariant
        } else {
            0
        }

    /**
     * Resolves the selected variant index to the actual list of CombinedIds for rendering.
     *
     * @param lightsabers The lightsaber entity with declared variants
     * @param variantIndex The selected variant index (from selectVariantIndex)
     * @return List of CombinedId references that define what should be rendered together
     */
    fun resolveVariant(lightsabers: Lightsabers, variantIndex: Int): List<CombinedId> {
        val variants = lightsabers.variants
        return if (variantIndex in variants.indices) {
            variants[variantIndex]
        } else {
            // Fallback to first variant if index is out of bounds
            variants.firstOrNull() ?: listOf(CombinedId(1, 0))
        }
    }
}
