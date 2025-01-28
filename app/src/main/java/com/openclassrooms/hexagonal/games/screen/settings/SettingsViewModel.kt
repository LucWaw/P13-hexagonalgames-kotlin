package com.openclassrooms.hexagonal.games.screen.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.openclassrooms.hexagonal.games.data.manager.NotificationManagerHelper

/**
 * ViewModel responsible for managing user settings, specifically notification preferences.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationManagerHelper =
        NotificationManagerHelper(getApplication<Application>().applicationContext)


    /**
     * Enables notifications for the application.
     */
    fun enableNotifications() {
        notificationManagerHelper.enableNotifications()

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    /**
     * Disables notifications for the application.
     */
    fun disableNotifications() {
        notificationManagerHelper.disableNotifications()


        //Suppress autorisation for Firebase Messaging
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
    }

}
