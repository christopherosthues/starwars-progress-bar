package com.christopherosthues.starwarsprogressbar.constants

internal object BundleConstants {
    const val PLUGIN_NAME = "name"

    private const val NOTIFICATION = "notification."
    const val NOTIFICATION_PLUGIN_UPDATED = NOTIFICATION + "pluginUpdated"
    const val NOTIFICATION_CONFIGURE = NOTIFICATION + "configure"
    const val NOTIFICATION_DONT_SHOW_AGAIN = NOTIFICATION + "dontShowAgain"

    private const val CONFIGURATION = "configuration."
    const val PREVIEW_TITLE = CONFIGURATION + "preview"
    const val DETERMINATE = CONFIGURATION + "determinate"
    const val INDETERMINATE = CONFIGURATION + "indeterminate"
    const val UI_OPTIONS = CONFIGURATION + "uiOptions"
    const val SHOW_ICON = CONFIGURATION + "showIcon"
    const val SHOW_NAME = CONFIGURATION + "showName"
    const val SHOW_TOOL_TIPS = CONFIGURATION + "showToolTips"
    const val SHOW_FACTION_CRESTS = CONFIGURATION + "showFactionCrests"
    const val SAME_VELOCITY = CONFIGURATION + "sameVelocity"
    const val ENABLE_NEW = CONFIGURATION + "enableNew"
    const val SOLID_PROGRESS_BAR_COLOR = CONFIGURATION + "solidProgressBarColor"
    const val DRAW_SILHOUETTES = CONFIGURATION + "drawSilhouettes"
    const val CHANGE_AFTER_PASS = CONFIGURATION + "changeAfterPass"
    const val VEHICLES_TITLE = CONFIGURATION + "vehicles"
    const val LIGHTSABERS_TITLE = CONFIGURATION + "lightsabers"
    const val SELECT_ALL = CONFIGURATION + "selectAll"
    const val DESELECT_ALL = CONFIGURATION + "deselectAll"
    const val SELECTED = CONFIGURATION + "selected"
    const val SELECTOR = CONFIGURATION + "selector"
    const val LANGUAGE = CONFIGURATION + "language"

    const val VEHICLES = "vehicles."
    const val VEHICLES_FACTION = VEHICLES + "faction."

    const val LIGHTSABERS = "lightsabers."
    const val LIGHTSABERS_FACTION = LIGHTSABERS + "faction."
}
