package com.openclassrooms.hexagonal.games.data.manager

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.domain.model.User
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
        // Delete user from Firestore
        return userRepository.deleteUserFromFirestore().addOnSuccessListener {
            // Delete the user account from the Auth
            userRepository.deleteUser(context)
        }
    }

    fun createUser() {
        userRepository.createUser()
    }

    fun getUserData(): Task<User>? {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData()
            ?.continueWith { task: Task<DocumentSnapshot> ->
                task.result.toObject(
                    User::class.java
                )
            }
    }

}