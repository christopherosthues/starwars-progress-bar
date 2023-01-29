package com.christopherosthues.starwarsprogressbar.readme

import com.christopherosthues.starwarsprogressbar.StarWarsPluginConstants
import com.christopherosthues.starwarsprogressbar.readme.models.StarWarsFactions
import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.FileInputStream
import java.util.*
import javax.inject.Inject

open class AddIncludedVehiclesTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {

    @InputFile
    @Optional
    val inputFile: RegularFileProperty = objectFactory.fileProperty()

    @OutputFile
    @Optional
    val outputFile: RegularFileProperty = objectFactory.fileProperty()

    @InputFile
    @Optional
    val factionsFile: RegularFileProperty = objectFactory.fileProperty()

    @InputFile
    @Optional
    val messagesFile: RegularFileProperty = objectFactory.fileProperty()

    @TaskAction
    fun run() {
        val readme = Readme(inputFile.get().asFile)

        val messages = Properties()
        FileInputStream(messagesFile.get().asFile).use {
            messages.load(it)
        }

        val gson = Gson()
        val starWarsFactions = gson.fromJson(factionsFile.get().asFile.readText(), StarWarsFactions::class.java)
        starWarsFactions.factions.filter { it.id.isNotEmpty() }
            .forEach {
                readme.addNewFaction(messages.getProperty(it.localizationKey))

                it.vehicles.forEach { vehicle ->
                    vehicle.faction = it
                }
                it.vehicles.sortedWith(
                    compareBy(
                        String.CASE_INSENSITIVE_ORDER
                    ) { vehicle -> messages.getProperty(vehicle.localizationKey) }
                )
                    .forEach { vehicle ->
                        val imagePath = "${StarWarsPluginConstants.IMAGE_RESOURCE_PATH}${vehicle.fileName}"
                        readme.addVehicle(
                            messages.getProperty(vehicle.localizationKey),
                            "$imagePath@2x.png",
                            "${imagePath}_r@2x.png"
                        )
                    }
            }

        outputFile.get().asFile.writeText(readme.getAll())
    }
}
