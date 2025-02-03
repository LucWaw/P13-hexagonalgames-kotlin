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


    /**
     * Get the current user.
     * @return FirebaseUser? The current user.
     */
    fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }

    /**
     * Check if the current user is logged in.
     * @return Boolean True if the user is logged in, false otherwise.
     */
    fun isCurrentUserLogged(): Boolean {
        return (this.getCurrentUser() != null)
    }

    /**
     * Sign out the current user.
     */
    fun signOut(context: Context): Task<Void> {
        return userRepository.signOut(context)
    }

    /**
     * Delete the User from Firestore and Auth.
     * @param context The context of the application.
     * @return Task<Void> The task to delete the user.
     */
    fun deleteUser(context: Context): Task<Void> {
        // Delete user from Firestore
        return userRepository.deleteUserFromFirestore().addOnSuccessListener {
            // Delete the user account from the Auth
            userRepository.deleteUser(context)
        }
    }

    /**
     * Create a User in Firestore.
     */
    fun createUser() {
        userRepository.createUser()
    }

    /**
     * Retrieves the User data from Firestore.
     * Get User data then convert it to a User model Object.
     * @return Task<User>? The User data from Firestore.
     */
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