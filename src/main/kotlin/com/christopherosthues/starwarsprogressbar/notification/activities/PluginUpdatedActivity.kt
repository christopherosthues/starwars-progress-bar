package com.christopherosthues.starwarsprogressbar.notification.activities

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.configuration.StarWarsProgressConfigurable
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService.canShowNotification
import com.christopherosthues.starwarsprogressbar.notification.DoNotAskService.setDoNotAskFor
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.text.SemVer

class PluginUpdatedActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val pluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId(PluginConstants.PLUGIN_ID))
        val starWarsState = StarWarsPersistentStateComponent.instance?.state
        if (pluginDescriptor != null && starWarsState != null) {
            val installedVersion = pluginDescriptor.version
            val installedSemanticVersion = SemVer.parseFromText(installedVersion)
            val storedSemanticVersion = SemVer.parseFromText(starWarsState.version)
            if (storedSemanticVersion == null ||
                installedSemanticVersion != null &&
                installedSemanticVersion > storedSemanticVersion
            ) {
                starWarsState.version = installedVersion

                displayUpdateNotification(project, installedVersion)
            }
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
