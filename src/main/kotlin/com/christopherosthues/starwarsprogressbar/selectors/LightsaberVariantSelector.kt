package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.CombinedId
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import java.util.concurrent.ThreadLocalRandom

/**
 * Selects a random variant index from an entity's declared variants list.
 *
 * When the global lightsaber variants toggle is enabled, this selector randomly chooses
 * from each entity's declared `variants: List<List<CombinedId>>` list. The selected index
 * persists until the next entity selection. Falls back to first/default variant (index 0)
 * when the toggle is disabled or only a single variant is declared.
 */
internal object LightsaberVariantSelector {

    /**
     * Selects a random variant index from the entity's declared variants list.
     *
     * @param lightsabers The lightsaber entity with declared variants
     * @param enableVariants Whether the global variant toggle is enabled
     * @return The selected variant index (0-based), or 0 if disabled/single variant
     */
    fun selectVariantIndex(lightsabers: Lightsabers, enableVariants: Boolean): Int {
        val variants = lightsabers.variants
        return when {
            !enableVariants -> 0
            variants.size == 1 -> 0
            else -> ThreadLocalRandom.current().nextInt(variants.size)
        }
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
