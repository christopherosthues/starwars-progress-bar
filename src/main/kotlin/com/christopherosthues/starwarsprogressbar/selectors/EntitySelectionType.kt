package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants

enum class EntitySelectionType(val localizationKey: String) {
    VEHICLES("vehicles"),
    LIGHTSABERS("lightsabers"),
    ALL("all"),
    ;

    override fun toString(): String = StarWarsBundle.message("${BundleConstants.ENTITY_SELECTOR}.$localizationKey")
}
