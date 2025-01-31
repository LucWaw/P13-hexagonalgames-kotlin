package com.openclassrooms.hexagonal.games.screen.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userManager: UserManager
) :
    ViewModel() {

    private val _comments: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val comments = _comments.asStateFlow()

    private val _post = MutableStateFlow(Post())
    val post = _post.asStateFlow()


    fun isCurrentUserLogged(): Boolean {
        return userManager.isCurrentUserLogged()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        return postRepository.isNetworkAvailable(context)
    }

    fun loadPost(postId: String) {
        val postData = postRepository.getPost(postId)

        if (postData == null) {
            Log.d("AddPostViewModel", "User data is null")
            return
        }
        viewModelScope.launch {
            postRepository.comments(postId).collect {
                _comments.value = it
            }
        }

        postData.addOnSuccessListener { post ->
            _post.value = post

        }.addOnFailureListener {
            Log.d("AddPostViewModel", "Error getting post data", it)
        }
    }
}