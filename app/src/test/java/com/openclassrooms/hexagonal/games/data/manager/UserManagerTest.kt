package com.openclassrooms.hexagonal.games.data.manager

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.domain.model.User
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class UserManagerTest {
    val documentId = "123"
    val document = mutableMapOf<String, Any>(
        "id" to documentId,
        "firstname" to "John",
        "lastname" to "Doe"
    )


    private lateinit var userRepository: UserRepository
    private lateinit var userManager: UserManager

    private val mockFirebaseAuth: FirebaseAuth = mockk()
    private val mockFirebaseUser: FirebaseUser = mockk()

    /**
     * Mocks the simplest behaviour of a task so .await() can return task or throw exception
     */
    inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
        val task: Task<T> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        every { task.result } returns result

        every { task.addOnCompleteListener(any()) } answers {
            firstArg<OnCompleteListener<T>>().onComplete(task)
            task
        }
        return task
    }


    @BeforeEach
    fun setUp() {
        userRepository = mockk() // Remplace l'annotation @Mock
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        userManager = UserManager(userRepository)
    }

    @Test
    fun getCurrentUser() {
        // Given
        every { userRepository.getCurrentUser() } returns mockFirebaseUser

        // When
        val result = userManager.getCurrentUser()

        // Then
        assertEquals(mockFirebaseUser, result)
    }

    @Test
    fun isCurrentUserLogged() {
        //Given
        every { userRepository.getCurrentUser() } returns (mockFirebaseUser)

        //When
        val result = userManager.isCurrentUserLogged()

        //Then
        assertEquals(true, result)

    }

    @Test
    fun signOut() {
        // Given
        val mockDeleteTokenTask: Task<Void> = mockk()
        val context: Context = mockk()

        every { userRepository.signOut(context) } returns mockDeleteTokenTask
        every { mockDeleteTokenTask.isSuccessful } returns true

        // When
        val result = userManager.signOut(context)

        // Then
        assertEquals(mockDeleteTokenTask, result)
        assertEquals(mockDeleteTokenTask.isSuccessful, result.isSuccessful)
    }

    @Test
    fun `Testing getUserData()`() {
        // Arrange
        val documentSnapshot = mockk<DocumentSnapshot> {
            every { id } returns "123"
            every { data } returns document
        }


        val mockUserRepository: UserRepository = mockk {
            every { getUserData() } returns mockTask<DocumentSnapshot>(documentSnapshot)
        }


        val userManagerSpyk = spyk(UserManager(mockUserRepository), recordPrivateCalls = true)
        runBlocking {
            // Act
            val resultTask: Task<User>? = userManagerSpyk.getUserData()


            // Assert
            coVerify(exactly = 1) {
                userManagerSpyk.getUserData()
            }

            assertNotNull(resultTask)

            verify(exactly = 1) { mockUserRepository.getUserData() }

            //Pourquoi ne pas verifier la sortie : La tache continue with ne peut etre lancé que si la tache mocké est encore executable ce qui n'est pas le cas
            //Pas de possibilité de mocker continue with pour matcher le resultat

            // Match impossible ici Not enough information to infer type variable T
            /*every { resultTask.continueWith(match { it is Continuation<DocumentSnapshot, User> }) } answers {
     val continuation = firstArg<Continuation<DocumentSnapshot, User>>()
     val newResult = continuation.then(mockTaskDoc)
     mockTask(newResult) // Retourne une nouvelle Task<User>*/
        }


    }

    @Test
    fun createUser() {
        // Given
        every { userRepository.createUser() } returns Unit

        // When
        userManager.createUser()

        // Then
        verify(exactly = 1) { userRepository.createUser() }
    }


}