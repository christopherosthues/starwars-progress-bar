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
) : StarWarsEntity {

    override var factionId: String = ""

    override val fileName: String
        get() = "lightsabers/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$entityId"
}
