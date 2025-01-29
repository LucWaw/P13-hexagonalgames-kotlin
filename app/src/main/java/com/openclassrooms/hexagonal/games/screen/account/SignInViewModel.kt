package com.openclassrooms.hexagonal.games.screen.account

import androidx.lifecycle.ViewModel
import com.openclassrooms.hexagonal.games.data.manager.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    fun createUser() {
        userManager.createUser()
    }
}