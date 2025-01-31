package com.openclassrooms.hexagonal.games.screen.addPost

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddScreen.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 */
@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uriImage: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val uriImage = _uriImage.asStateFlow()

    /**
     * Internal mutable state flow representing the current post being edited.
     */
    private var _post = MutableStateFlow(
        Post(
            id = UUID.randomUUID().toString(),
            title = "",
            description = "",
            photoUrl = null,
            timestamp = System.currentTimeMillis(),
            author = null
        )
    )

    /**
     * Public state flow representing the current post being edited.
     * This is immutable for consumers.
     */
    val post: StateFlow<Post>
        get() = _post

    /**
     * StateFlow derived from the post that emits a FormError if the title is empty, null otherwise.
     */
    val error = post.combine(uriImage){
        post, uriImage -> Pair(post, uriImage)
    }.map {
        verifyPost()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    /**
     * Handles form events like title and description changes.
     *
     * @param formEvent The form event to be processed.
     */
    fun onAction(formEvent: FormEvent) {
        when (formEvent) {
            is FormEvent.DescriptionChanged -> {
                _post.value = _post.value.copy(
                    description = formEvent.description
                )
            }

            is FormEvent.TitleChanged -> {
                _post.value = _post.value.copy(
                    title = formEvent.title
                )
            }

            is FormEvent.ImageChanged -> {
                _uriImage.value = formEvent.image
            }
        }
    }

    /**
     * Attempts to add the current post to the repository after setting the author.
     *
     */
    fun addPost(uri: Uri?): Task<User>? {
        val userdata = userManager.getUserData()

        if (userdata == null) {
            Log.d("AddPostViewModel", "User data is null")
            return null
        }


        return userdata.addOnSuccessListener { user ->
            _post.value = _post.value.copy(
                author = User(
                    id = user.id,
                    firstname = user.firstname,
                    lastname = user.lastname
                )
            )

            postRepository.addPost(
                _post.value,
                uri
            )
        }.addOnFailureListener() {
            Log.d("AddPostViewModel", "Error: $it")
        }
    }

    /**
     * Verifies mandatory fields of the post
     * and returns a corresponding FormError if so.
     *
     * @return A FormError.TitleError if title is empty, null otherwise.
     * @return A FormError.ImageDescriptionError if image and description are empty, null otherwise.
     */
    private fun verifyPost(): FormError? {
        return when {
            _post.value.title.isEmpty() -> FormError.TitleError
            _post.value.description.isNullOrEmpty() && uriImage.value == null
                -> FormError.ImageDescriptionError

            else -> null
        }
    }


}
