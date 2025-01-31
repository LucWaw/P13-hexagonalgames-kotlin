package com.openclassrooms.hexagonal.games.screen.addComment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCommentViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userManager: UserManager
) : ViewModel() {

    /**
     * Internal mutable state flow representing the current post being edited.
     */
    private var _comment = MutableStateFlow(
        Comment(
            id = UUID.randomUUID().toString(),
            content = "",
            timestamp = System.currentTimeMillis(),
            author = null
        )
    )

    /**
     * Public state flow representing the current post being edited.
     * This is immutable for consumers.
     */
    val comment: StateFlow<Comment>
        get() = _comment

    /**
     * StateFlow derived from the post that emits a FormError if the title is empty, null otherwise.
     */
    val error = comment.map {
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
    fun onAction(formEvent: FormEventComment) {
        when (formEvent) {
            is FormEventComment.ContentChanged -> {
                _comment.value = _comment.value.copy(content = formEvent.content)
            }
        }
    }

    /**
     * Attempts to add the current post to the repository after setting the author.
     *
     */
    fun addComment(postId: String): Task<User>? {
        val userdata = userManager.getUserData()

        if (userdata == null) {
            Log.d("AddCommentViewModel", "User data is null")
            return null
        }


        return userdata.addOnSuccessListener { user ->
            _comment.value = _comment.value.copy(
                author = User(
                    id = user.id,
                    firstname = user.firstname,
                    lastname = user.lastname
                )
            )

            postRepository.addComment(
                _comment.value,
                postId
            )
        }.addOnFailureListener() {
            Log.d("AddCommentViewModel", "Error: $it")
        }
    }

    /**
     * Verifies mandatory fields of the post
     * and returns a corresponding FormError if so.
     *
     * @return A FormError.TitleError if title is empty, null otherwise.
     * @return A FormError.ImageDescriptionError if image and description are empty, null otherwise.
     */
    private fun verifyPost(): FormErrorComment? {
        return when {
            _comment.value.content.isEmpty() -> FormErrorComment.ContentCommentError
            else -> null
        }
    }

}