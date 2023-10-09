package com.christopherosthues.starwarsprogressbar.notification

import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationGroup

object DoNotAskService {
    fun canShowNotification(): Boolean {
        val id = "Notification.DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}"
        val doNotAsk = PropertiesComponent.getInstance().getBoolean(id, false)

        return !doNotAsk
    }

    fun setDoNotAskFor(doNotAsk: Boolean) {
        var title = NotificationGroup.getGroupTitle(PluginConstants.NOTIFICATION_GROUP_ID)
        if (title == null) {
            title = PluginConstants.NOTIFICATION_GROUP_ID
        }
        val manager = PropertiesComponent.getInstance()
        manager.setValue("Notification.DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}", doNotAsk)
        manager.setValue("Notification.DisplayName-DoNotAsk-${PluginConstants.NOTIFICATION_GROUP_ID}", title)
    }
}
