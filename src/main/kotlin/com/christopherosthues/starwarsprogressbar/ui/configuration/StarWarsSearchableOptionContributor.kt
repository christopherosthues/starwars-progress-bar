package com.christopherosthues.starwarsprogressbar.ui.configuration

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.ui.StarWarsVehicle
import com.intellij.ide.ui.search.SearchableOptionContributor
import com.intellij.ide.ui.search.SearchableOptionProcessor

class StarWarsSearchableOptionContributor : SearchableOptionContributor() {
    // TODO not working yet
    override fun processOptions(processor: SearchableOptionProcessor) {
        StarWarsVehicle.DEFAULT_VEHICLES.forEach {
            val splitText = it.vehicleName.split(" ")
            splitText.forEach { text ->
                processor.addOptions(text, null, text, PluginConstants.PluginSearchId, null, false)
            }
        }
    }
}