package com.christopherosthues.starwarsprogressbar.models

import kotlinx.serialization.Serializable

/**
 * Structured reference to a specific lightsaber and blade combination.
 *
 * @property lightsaberId 1-based index into Lightsabers.lightsabers list (matches existing Lightsaber.id field)
 * @property bladeId 0-based index into the referenced lightsaber's blades list
 */
@Serializable
internal data class CombinedId(val lightsaberId: Int, val bladeId: Int)
