package com.openclassrooms.hexagonal.games.data.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Singleton


/**
 * This class provides a repository for accessing and managing User data.
 * It is marked as a Singleton using @Singleton annotation, ensuring there's only one instance throughout the application.
 * The repository is responsible for interacting with the data source.
 */
@Singleton
class UserRepository {
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun signOut(context: Context): Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }

    fun deleteUser(context: Context): Task<Void> {
        return AuthUI.getInstance().delete(context)
    }

}