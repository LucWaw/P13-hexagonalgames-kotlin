package com.openclassrooms.hexagonal.games.screen.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for managing the user profile screen.
 * This ViewModel provides the logic for signing out and deleting the current user.
 * It interacts with the UserManager to perform these actions.
 * @param userManager The UserManager instance used to interact with the user data.
 * @constructor Creates a new ProfileViewModel instance.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    fun signOutCurrentUser(context: Context) {
        userManager.signOut(context)
    }

    fun deleteCurrentUser(context: Context) : Task<Void> {
        return userManager.deleteUser(context)
    }
}