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

    @Test
    fun `every variant references existing sabers and blades`() {
        val json = javaClass.getResource("/json/factions.json")?.readText()
            ?: throw AssertionError("Bundled factions.json not found on the test classpath")

        val factions = parseFactionsFromJson(json)

        assertTrue(
            factions.lightsabers.isNotEmpty(),
            "factions.json parsed to no lightsaber factions - check for JSON syntax errors",
        )

        factions.lightsabers.forEach { faction ->
            faction.data.forEach { entity ->
                entity.variants.forEach { variant ->
                    variant.forEach { combinedId ->
                        val saber = entity.lightsabers.getOrNull(combinedId.lightsaberId - 1)
                            ?: throw AssertionError(
                                "${entity.id}: variant references unknown lightsaber " +
                                    "${combinedId.lightsaberId} (entity has ${entity.lightsabers.size} lightsaber(s))",
                            )
                        assertTrue(
                            combinedId.bladeId in saber.blades.indices,
                            "${entity.id}: variant references blade ${combinedId.bladeId} of lightsaber " +
                                "${combinedId.lightsaberId} which has ${saber.blades.size} blade(s)",
                        )
                    }
                }
            }
        }
    }
}
