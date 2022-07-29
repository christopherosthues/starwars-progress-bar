package com.christopherosthues.starwarsprogressbar.versionbump

import com.christopherosthues.starwarsprogressbar.StarWarsPluginConstants
import com.christopherosthues.starwarsprogressbar.exceptions.MissingFileException
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class VersionBumpTask @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {
    @InputFile
    @Optional
    val inputFile: RegularFileProperty = objectFactory.fileProperty()

    @OutputFile
    @Optional
    val outputFile: RegularFileProperty = objectFactory.fileProperty()

    @Optional
    @Input
    val increaseVersion = objectFactory.property<String>()

    @TaskAction
    fun run() {
        val propertiesContent = inputFile.get().asFile.run {
            if (!exists()) {
                throw MissingFileException(canonicalPath)
            }

            readText()
        }
        val pluginVersionLine = propertiesContent.lines().first { it.startsWith(StarWarsPluginConstants.PLUGIN_VERSION) }
        val pluginVersion = pluginVersionLine.substringAfterLast(" ")

        val version = Version(pluginVersion)

        version.increaseVersion(increaseVersion.get())

        val newPluginVersionLine = pluginVersionLine.replaceAfterLast(" ", version.get())

        outputFile.get().asFile.writeText(propertiesContent.replace(pluginVersionLine, newPluginVersionLine))
    }
}
