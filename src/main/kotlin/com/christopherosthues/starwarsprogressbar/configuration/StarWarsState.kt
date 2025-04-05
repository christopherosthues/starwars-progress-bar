package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_CHANGE_AFTER_PASS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_ENABLE_NEW
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_LANGUAGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SAME_VELOCITY
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SELECTOR
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_ICON
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_NAMES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_TOOLTIPS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import java.util.stream.Collectors

internal class StarWarsState {
    var languageOrdinal: Int = DEFAULT_LANGUAGE.ordinal

    internal var language: Language?
        get() = languageOrdinal.let {
            Language.entries.getOrNull(it) ?: DEFAULT_LANGUAGE
        }
        set(value) {
            languageOrdinal = value?.ordinal ?: DEFAULT_LANGUAGE.ordinal
        }

    @JvmField
    var vehiclesEnabled: MutableMap<String, Boolean> =
        StarWarsFactionHolder.defaultVehicles.stream().collect(Collectors.toMap(StarWarsVehicle::entityId) { true })

    @JvmField
    var lightsabersEnabled: MutableMap<String, Boolean> =
        StarWarsFactionHolder.defaultLightsabers.stream().collect(Collectors.toMap(Lightsabers::entityId) { true })

    var showIcon: Boolean = DEFAULT_SHOW_ICON

    var showNames: Boolean = DEFAULT_SHOW_NAMES

    var showToolTips: Boolean = DEFAULT_SHOW_TOOLTIPS

    var showFactionCrests: Boolean = DEFAULT_SHOW_FACTION_CRESTS

    var sameVelocity: Boolean = DEFAULT_SAME_VELOCITY

    var enableNew: Boolean = DEFAULT_ENABLE_NEW

    var solidProgressBarColor: Boolean = DEFAULT_SOLID_PROGRESS_BAR_COLOR

    var drawSilhouettes: Boolean = DEFAULT_DRAW_SILHOUETTES

    var changeAfterPass: Boolean = DEFAULT_CHANGE_AFTER_PASS

    var numberOfPassesUntilChange: Int = DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE

    var selectorOrdinal: Int = DEFAULT_SELECTOR.ordinal

    internal var selector: SelectionType?
        get() = selectorOrdinal.let {
            SelectionType.entries.getOrNull(it) ?: DEFAULT_SELECTOR
        }
        set(value) {
            selectorOrdinal = value?.ordinal ?: DEFAULT_SELECTOR.ordinal
        }

    var version: String = ""

    fun copy(starWarsState: StarWarsState) {
        language = starWarsState.language
        vehiclesEnabled.clear()
        vehiclesEnabled.putAll(starWarsState.vehiclesEnabled)
        lightsabersEnabled.clear()
        lightsabersEnabled.putAll(starWarsState.lightsabersEnabled)
        showIcon = starWarsState.showIcon
        showNames = starWarsState.showNames
        showToolTips = starWarsState.showToolTips
        showFactionCrests = starWarsState.showFactionCrests
        sameVelocity = starWarsState.sameVelocity
        enableNew = starWarsState.enableNew
        solidProgressBarColor = starWarsState.solidProgressBarColor
        drawSilhouettes = starWarsState.drawSilhouettes
        changeAfterPass = starWarsState.changeAfterPass
        numberOfPassesUntilChange = starWarsState.numberOfPassesUntilChange
        selector = starWarsState.selector
        version = starWarsState.version
    }
}
