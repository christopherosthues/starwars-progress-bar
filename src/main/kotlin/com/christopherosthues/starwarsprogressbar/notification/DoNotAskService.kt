package com.christopherosthues.starwarsprogressbar.notification

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationGroup

object DoNotAskService {
    fun canShowNotification(): Boolean {

        val id = "Notification.DoNotAsk-${PluginConstants.NotificationGroupId}"
        val doNotAsk = PropertiesComponent.getInstance().getBoolean(id, false)

        return !doNotAsk
    }

    fun setDoNotAskFor(doNotAsk: Boolean) {
        var title = NotificationGroup.getGroupTitle(PluginConstants.NotificationGroupId)
        if (title == null) {
            title = PluginConstants.NotificationGroupId
        }
        val manager = PropertiesComponent.getInstance()
        manager.setValue("Notification.DoNotAsk-${PluginConstants.NotificationGroupId}", doNotAsk)
        manager.setValue("Notification.DisplayName-DoNotAsk-${PluginConstants.NotificationGroupId}", title)
    }
}
