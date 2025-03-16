package com.christopherosthues.starwarsprogressbar.models

import com.intellij.ui.JBColor
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface StarWarsEntity {
    val id: String
    val entityId: String
        get() = if (factionId.isEmpty()) id else "$factionId.$id"
    val color: JBColor
    val velocity: Float
    var factionId: String
    val fileName: String
    val localizationKey: String
}
