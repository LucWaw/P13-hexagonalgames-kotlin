package com.openclassrooms.hexagonal.games.screen.homefeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing data and events related to the HomeFeed.
 * This ViewModel retrieves posts from the PostRepository and exposes them as a Flow<List<Post>>,
 * allowing UI components to observe and react to changes in the posts data.
 */
@HiltViewModel
class HomeFeedViewModel @Inject constructor(private val postRepository: PostRepository, private val userManager: UserManager) :
    ViewModel() {

    private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())

    /**
     * Returns a Flow observable containing the list of posts fetched from the repository.
     *
     * @return A Flow<List<Post>> object that can be observed for changes.
     */
    val posts: StateFlow<List<Post>>
        get() = _posts

    init {
        viewModelScope.launch {
            postRepository.posts.collect {
                _posts.value = it
            }
        }
    }


    fun isCurrentUserLogged(): Boolean {
        return userManager.isCurrentUserLogged()
    }

}
