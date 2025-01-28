package com.openclassrooms.hexagonal.games.data.manager

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import javax.inject.Singleton


@Singleton
class UserManager(private val userRepository: UserRepository) {


    fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }

    fun isCurrentUserLogged(): Boolean {
        return (this.getCurrentUser() != null)
    }

    fun signOut(context: Context): Task<Void> {
        return userRepository.signOut(context)
    }

    fun deleteUser(context: Context): Task<Void> {
        return userRepository.deleteUser(context)
    }
}