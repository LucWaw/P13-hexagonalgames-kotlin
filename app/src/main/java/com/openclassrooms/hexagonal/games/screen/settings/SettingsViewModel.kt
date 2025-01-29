package com.openclassrooms.hexagonal.games.screen.settings

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.openclassrooms.hexagonal.games.R
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

        FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener { task ->
            var msg = getApplication<Application>().applicationContext.getString(R.string.subscribe_successful)
            if (!task.isSuccessful) {
                msg = getApplication<Application>().applicationContext.getString(R.string.subscribe_failed)
            }
            Log.d("SettingsViewModel", msg)
            Toast.makeText(getApplication<Application>().applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Disables notifications for the application.
     */
    fun disableNotifications() {
        notificationManagerHelper.disableNotifications()


        //Suppress autorisation for Firebase Messaging
        FirebaseMessaging.getInstance().unsubscribeFromTopic("All").addOnCompleteListener { task ->
            var msg = getApplication<Application>().applicationContext.getString(R.string.unsubscribe_successful)
            if (!task.isSuccessful) {
                msg = getApplication<Application>().applicationContext.getString(R.string.unsubscribe_failed)
            }
            Log.d("SettingsViewModel", msg)
            Toast.makeText(getApplication<Application>().applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

}
