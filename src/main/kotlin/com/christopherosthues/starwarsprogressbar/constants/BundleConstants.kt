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
    const val SHOW_VEHICLE_NAME = CONFIGURATION + "showVehicleName"
    const val SHOW_TOOL_TIPS = CONFIGURATION + "showToolTips"
    const val SHOW_FACTION_CRESTS = CONFIGURATION + "showFactionCrests"
    const val SAME_VEHICLE_VELOCITY = CONFIGURATION + "sameVehicleVelocity"
    const val ENABLE_NEW_VEHICLES = CONFIGURATION + "enableNewVehicles"
    const val VEHICLES_TITLE = CONFIGURATION + "vehicles"
    const val SELECT_ALL = CONFIGURATION + "selectAll"
    const val DESELECT_ALL = CONFIGURATION + "deselectAll"
    const val SELECTED = CONFIGURATION + "selected"

    const val GOT_IT_ICON_SELECTION = CONFIGURATION + "gotit.iconSelection"

    const val FACTION = "faction."

    const val VEHICLES = "vehicles."
}
