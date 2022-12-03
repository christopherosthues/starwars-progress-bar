package com.christopherosthues.starwarsprogressbar

object StarWarsPluginConstants {
    const val EXTENSION_NAME = "starwars"

    const val INCLUDE_VEHICLES_NAME = "includeVehicles"
    const val VERSION_BUMP_NAME = "bumpVersion"

    const val README_FILE_NAME = "README.md"
    const val FACTIONS_FILE_NAME = "factions.json"
    const val MESSAGES_FILE_NAME = "StarWarsBundle.properties"
    const val GRADLE_PROPERTIES_FILE_NAME = "gradle.properties"

    const val NEW_LINE = "\n"
    const val BEGIN_INCLUDED_VEHICLES_SECTION = "<!-- Included vehicles -->"
    const val END_INCLUDED_VEHICLES_SECTION = "<!-- Included vehicles end -->"
    const val INCLUDED_VEHICLES_SECTION = "## Included vehicles$NEW_LINE"

    private const val RESOURCE_PATH = "src/main/resources/"
    const val IMAGE_RESOURCE_PATH = "icons/"
    const val JSON_RESOURCE_PATH = RESOURCE_PATH + "json/"
    const val MESSAGES_RESOURCE_PATH = RESOURCE_PATH + "messages/"
    const val FACTION = "faction."
    const val VEHICLES = "vehicles."

    const val PLUGIN_VERSION = "pluginVersion"

    const val MAJOR_VERSION = "MAJOR"
    const val MINOR_VERSION = "MINOR"
    const val PATCH_VERSION = "PATCH"
}
