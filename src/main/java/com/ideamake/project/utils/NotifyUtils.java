package com.ideamake.project.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;

/**
 * 通知消息
 */
public class NotifyUtils {

    private static final NotificationGroup NOTIFICATION = new NotificationGroup("arthas", NotificationDisplayType.BALLOON, false);

    /**
     * 消息
     *
     * @param project
     * @param message
     */
    public static void notifyMessage(Project project, String message) {
        try {
            Notification currentNotify = NOTIFICATION.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(currentNotify, project);
        } catch (Exception e) {
            //
        }
    }
}
