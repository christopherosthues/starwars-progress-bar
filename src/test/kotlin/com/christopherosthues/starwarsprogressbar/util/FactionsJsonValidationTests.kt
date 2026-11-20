package com.christopherosthues.starwarsprogressbar.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Validates the bundled factions.json content so invalid data can never reach production.
 * Runs locally and in CI (part of `check`).
 */
internal class FactionsJsonValidationTests {
    @Test
    fun `every lightsaber entity declares at least one variant and a valid default variant`() {
        val json = javaClass.getResource("/json/factions.json")?.readText()
            ?: throw AssertionError("Bundled factions.json not found on the test classpath")

        val factions = parseFactionsFromJson(json)

        assertTrue(
            factions.lightsabers.isNotEmpty(),
            "factions.json parsed to no lightsaber factions - check for JSON syntax errors",
        )

        factions.lightsabers.forEach { faction ->
            faction.data.forEach { entity ->
                assertTrue(entity.variants.isNotEmpty(), "${entity.id}: declares no variants")
                assertTrue(
                    entity.defaultVariant in entity.variants.indices,
                    "${entity.id}: defaultVariant ${entity.defaultVariant} is out of range for " +
                        "${entity.variants.size} variant(s)",
                )
            }
        }
    }
}
