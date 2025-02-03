package com.openclassrooms.hexagonal.games.data.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserManagerTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private val mockFirebaseAuth: FirebaseAuth = mock()
    private val mockFirebaseUser: FirebaseUser = mock()

    @BeforeEach
    fun setUp() {
        // Simule Firebase.auth.currentUser
        whenever(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)
        whenever(userRepository.getCurrentUser()).thenReturn(mockFirebaseUser)
    }

    @Test
    fun getCurrentUser() {
        // When
        val result = userRepository.getCurrentUser()

        // Then
        assertEquals(mockFirebaseUser, result)
    }

    @Test
    fun isCurrentUserLogged() {
    }

    @Test
    fun signOut() {
    }

    @Test
    fun deleteUser() {
    }

    @Test
    fun createUser() {
    }

    @Test
    fun getUserData() {
    }
}