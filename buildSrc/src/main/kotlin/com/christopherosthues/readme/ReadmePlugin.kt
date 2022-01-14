package com.christopherosthues.readme

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReadmePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(ReadmePluginConstants.EXTENSION_NAME, ReadmePluginExtension::class.java).apply {
            readmeFilePath.convention(project.provider {
                "${project.projectDir}/${ReadmePluginConstants.README_FILE_NAME}"
            })
            factionsFilePath.convention(project.provider {
                "${project.projectDir}/${ReadmePluginConstants.JSON_RESOURCE_PATH}${ReadmePluginConstants.FACTIONS_FILE_NAME}"
            })
            messagesFilePath.convention(project.provider {
                "${project.projectDir}/${ReadmePluginConstants.MESSAGES_RESOURCE_PATH}${ReadmePluginConstants.MESSAGES_FILE_NAME}"
            })
        }

        project.tasks.register(ReadmePluginConstants.INCLUDE_VEHICLES_NAME, AddIncludedVehiclesTask::class.java) {
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
    }
}