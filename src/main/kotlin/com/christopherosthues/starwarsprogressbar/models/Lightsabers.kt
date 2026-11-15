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
    @Suppress("ktlint")
    val variants: List<List<CombinedId>> = listOf(listOf(CombinedId(1, 0))),
    /**
     * Alternative icon file names for the same lightsaber entity.
     * Different characters/contexts; V2 feature foundation.
     */
    @Suppress("ktlint")
    val iconVariations: List<String> = emptyList(),
) : StarWarsEntity {

    override var factionId: String = ""

    override val fileName: String
        get() = "lightsabers/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$entityId"
}
