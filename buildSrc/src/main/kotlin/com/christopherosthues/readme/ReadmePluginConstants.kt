package com.christopherosthues.readme

object ReadmePluginConstants {
    const val EXTENSION_NAME = "readme"

    const val INCLUDE_VEHICLES_NAME = "includeVehicles"

    const val README_FILE_NAME = "README.md"
    const val FACTIONS_FILE_NAME = "factions.json"
    const val MESSAGES_FILE_NAME = "StarWarsBundle.properties"

    const val NEW_LINE = "\n"
    const val BEGIN_INCLUDED_VEHICLES_SECTION = "<!-- Included vehicles -->"
    const val END_INCLUDED_VEHICLES_SECTION = "<!-- Included vehicles end -->"
    const val INCLUDED_VEHICLES_SECTION = "## Included vehicles$NEW_LINE"

    private const val RESOURCE_PATH = "src/main/resources/"
    const val IMAGE_RESOURCE_PATH = RESOURCE_PATH + "icons/"
    const val JSON_RESOURCE_PATH = RESOURCE_PATH + "json/"
    const val MESSAGES_RESOURCE_PATH = RESOURCE_PATH + "messages/"
    const val FACTION = "faction."
    const val VEHICLES = "vehicles."
}