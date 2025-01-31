package com.openclassrooms.hexagonal.games.screen.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userManager: UserManager
) :
    ViewModel() {

    private val _post = MutableStateFlow(Post())
    val post = _post.asStateFlow()


    fun isCurrentUserLogged(): Boolean {
        return userManager.isCurrentUserLogged()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        return postRepository.isNetworkAvailable(context)
    }

    fun getPost(postId: String) {
        //TODO get POST postRepository.getPost(postId)
    }
}