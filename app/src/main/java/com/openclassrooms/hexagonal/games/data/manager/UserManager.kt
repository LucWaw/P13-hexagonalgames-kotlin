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
        // Delete the user account from the Auth
        return userRepository.deleteUser(context).addOnCompleteListener {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore()
        }
    }

    fun createUser() {
        userRepository.createUser()
    }
}