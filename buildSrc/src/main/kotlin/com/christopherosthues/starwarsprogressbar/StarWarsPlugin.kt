package com.christopherosthues.starwarsprogressbar

import com.christopherosthues.starwarsprogressbar.readme.AddIncludedVehiclesTask
import com.christopherosthues.starwarsprogressbar.versionbump.VersionBumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class StarWarsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(StarWarsPluginConstants.EXTENSION_NAME, StarWarsPluginExtension::class.java).apply {
            readmeFilePath.convention(
                project.provider {
                    "${project.projectDir}/${StarWarsPluginConstants.README_FILE_NAME}"
                }
            )
            factionsFilePath.convention(
                project.provider {
                    "${project.projectDir}/${StarWarsPluginConstants.JSON_RESOURCE_PATH}${StarWarsPluginConstants.FACTIONS_FILE_NAME}"
                }
            )
            messagesFilePath.convention(
                project.provider {
                    "${project.projectDir}/${StarWarsPluginConstants.MESSAGES_RESOURCE_PATH}${StarWarsPluginConstants.MESSAGES_FILE_NAME}"
                }
            )
            gradlePropertiesFile.convention(
                project.provider {
                    "${project.projectDir}/${StarWarsPluginConstants.GRADLE_PROPERTIES_FILE_NAME}"
                }
            )
            increaseVersion.convention(StarWarsPluginConstants.MINOR_VERSION)
        }

        project.tasks.register(StarWarsPluginConstants.INCLUDE_VEHICLES_NAME, AddIncludedVehiclesTask::class.java) {
            inputFile.convention {
                project.file(extension.readmeFilePath.get())
            }
            outputFile.convention {
                project.file(extension.readmeFilePath.get())
            }
            factionsFile.convention {
                project.file(extension.factionsFilePath.get())
            }
            messagesFile.convention {
                project.file(extension.messagesFilePath.get())
            }
        }

        project.tasks.register(StarWarsPluginConstants.VERSION_BUMP_NAME, VersionBumpTask::class.java) {
            inputFile.convention {
                project.file(extension.gradlePropertiesFile.get())
            }
            outputFile.convention {
                project.file(extension.gradlePropertiesFile.get())
            }
            increaseVersion.set(extension.increaseVersion)
        }
    }
}
