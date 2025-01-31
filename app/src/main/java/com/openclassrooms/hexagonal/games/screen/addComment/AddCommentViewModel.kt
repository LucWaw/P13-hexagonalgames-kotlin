package com.openclassrooms.hexagonal.games.screen.addComment

import androidx.lifecycle.ViewModel
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCommentViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userManager: UserManager
) : ViewModel() {
}