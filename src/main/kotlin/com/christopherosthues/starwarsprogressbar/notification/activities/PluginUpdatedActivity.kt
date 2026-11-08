package com.christopherosthues.starwarsprogressbar.notification.activities

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsProgressConfigurable
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService.canShowNotification
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService.setDoNotAskFor
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class PluginUpdatedActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val starWarsState = StarWarsPersistentStateComponent.instance.state
        // previousVersion is set by StarWarsPersistentStateComponent.loadState when a new plugin version is detected.
        if (starWarsState != null &&
            starWarsState.previousVersion.isNotEmpty() &&
            starWarsState.previousVersion != starWarsState.version
        ) {
            // Clear the marker so the notification is only shown once per update.
            starWarsState.previousVersion = ""

            displayUpdateNotification(project, starWarsState.version)
        }
    }

    private fun displayUpdateNotification(project: Project, version: String) {
        if (!canShowNotification()) {
            return
        }

        val notification = NotificationGroupManager.getInstance()
            .getNotificationGroup(PluginConstants.NOTIFICATION_GROUP_ID)
            .createNotification(
                StarWarsBundle.message(BundleConstants.PLUGIN_NAME),
                StarWarsBundle.message(BundleConstants.NOTIFICATION_PLUGIN_UPDATED, version),
                NotificationType.INFORMATION,
            )
        notification.icon = StarWarsResourceLoader.getPluginIcon()
        notification.addAction(object :
            DumbAwareAction(StarWarsBundle.message(BundleConstants.NOTIFICATION_CONFIGURE)) {
            override fun actionPerformed(e: AnActionEvent) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, StarWarsProgressConfigurable::class.java)
            }
        })
        notification.addAction(object :
            DumbAwareAction(StarWarsBundle.message(BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN)) {
            override fun actionPerformed(e: AnActionEvent) {
                setDoNotAskFor(true)
                notification.hideBalloon()
            }
        })
        notification.notify(project)
    }
}
