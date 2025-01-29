package com.openclassrooms.hexagonal.games.data.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.hexagonal.games.domain.model.User
import javax.inject.Singleton


/**
 * This class provides a repository for accessing and managing User data.
 * It is marked as a Singleton using @Singleton annotation, ensuring there's only one instance throughout the application.
 * The repository is responsible for interacting with the data source.
 */
@Singleton
class UserRepository {

    private val COLLECTION_NAME = "users"


    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun signOut(context: Context): Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }

    fun deleteUser(context: Context): Task<Void> {
        return AuthUI.getInstance().delete(context)
    }

    // Create User in Firestore
    fun createUser() {
        val user = getCurrentUser()
        if (user != null) {
            // Extracts the first and last name from the user's display name, defaulting to empty strings if the name is null or empty.
            val username: List<String> = (user.displayName?.split(" ", limit = 2)) ?: listOf("", "")
            val uid = user.uid

            val userToCreate = User(uid, username[0], if (username.size == 2) username[1] else "")

            getUsersCollection().document(uid).set(userToCreate)
        }
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // Delete the User from Firestore
    fun deleteUserFromFirestore() {
        val uid: String? = getCurrentUserUID()
        if (uid != null) {
            getUsersCollection().document(uid).delete()
        }
    }

    // Get User Data from Firestore
    fun getUserData(): Task<DocumentSnapshot>? {
        val uid: String? = getCurrentUserUID()
        return if (uid != null) {
            getUsersCollection().document(uid).get()
        } else {
            null
        }
    }

    // Get the current user's UID
    private fun getCurrentUserUID(): String? {
        val user: FirebaseUser? = getCurrentUser()
        return user?.uid
    }


}