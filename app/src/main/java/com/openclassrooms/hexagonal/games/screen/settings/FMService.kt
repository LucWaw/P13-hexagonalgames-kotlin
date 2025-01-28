package com.openclassrooms.hexagonal.games.screen.settings

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.openclassrooms.hexagonal.games.data.manager.NotificationManagerHelper

class FMService : FirebaseMessagingService() {
    private lateinit var notificationManagerHelper: NotificationManagerHelper

    override fun onCreate() {
        super.onCreate()
        notificationManagerHelper = NotificationManagerHelper(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseMessagingService", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FirebaseMessagingService", "From: ${message.notification?.title}")

        val notification = message.notification
        if (notification != null) {
            notificationManagerHelper.sendNotification(notification.title, notification.body)
        }
    }
}
