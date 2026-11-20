package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Lightsabers")
internal data class Lightsabers(
    override val id: String,
    override val velocity: Float,
    val isJarKai: Boolean,
    val lightsabers: List<Lightsaber>,
    /**
     * Declares allowed display modes as explicit CombinedId references.
     * Default preserves existing single-blade behavior.
     */
    val variants: List<List<CombinedId>> = listOf(listOf(CombinedId(1, 0))),
    /**
     * Alternative icon file names for the same lightsaber entity.
     * Different characters/contexts; V2 feature foundation.
     */
    val iconVariations: List<String> = emptyList(),
    /**
     * Index into [variants] rendered when the global lightsaber variants toggle is disabled.
     * Invalid values (negative or out of range) fall back to variant 0.
     */
    val defaultVariant: Int = 0,
) : StarWarsEntity {

    override var factionId: String = ""

    override val fileName: String
        get() = "lightsabers/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$entityId"
}
