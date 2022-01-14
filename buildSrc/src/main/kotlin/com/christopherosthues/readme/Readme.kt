package com.christopherosthues.readme

import com.christopherosthues.readme.exceptions.MissingFileException
import com.christopherosthues.readme.ReadmePluginConstants.BEGIN_INCLUDED_VEHICLES_SECTION
import com.christopherosthues.readme.ReadmePluginConstants.END_INCLUDED_VEHICLES_SECTION
import com.christopherosthues.readme.ReadmePluginConstants.INCLUDED_VEHICLES_SECTION
import com.christopherosthues.readme.ReadmePluginConstants.NEW_LINE
import java.io.File

class Readme(file: File) {
    private val content = file.run {
        if (!exists()) {
            throw MissingFileException(canonicalPath)
        }
        readText()
    }

    private val untilIncludedVehiclesSection = content.substringBefore(BEGIN_INCLUDED_VEHICLES_SECTION)
    private var includedVehiclesSection = StringBuilder(INCLUDED_VEHICLES_SECTION)
    private val afterIncludedVehiclesSection = content.substringAfter(END_INCLUDED_VEHICLES_SECTION)

    fun addNewFaction(factionName: String) {
        includedVehiclesSection.append(NEW_LINE)
        includedVehiclesSection.append("### ")
        includedVehiclesSection.append(factionName)
        includedVehiclesSection.append(NEW_LINE)
        includedVehiclesSection.append(NEW_LINE)
    }

    fun addVehicle(vehicleName: String, imagePath: String, reverseImagePath: String) {
        includedVehiclesSection.append("* ![$vehicleName]($imagePath) $vehicleName ![$vehicleName]($reverseImagePath)")
        includedVehiclesSection.append(NEW_LINE)
    }

    fun getAll(): String {
        return listOfNotNull(
            untilIncludedVehiclesSection,
            BEGIN_INCLUDED_VEHICLES_SECTION,
            NEW_LINE,
            includedVehiclesSection,
            NEW_LINE,
            END_INCLUDED_VEHICLES_SECTION,
            afterIncludedVehiclesSection
        ).joinToString("")
    }
}
