package com.openclassrooms.hexagonal.games.screen.account

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.openclassrooms.hexagonal.games.R


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Boolean,
    signInViewModel: SignInViewModel = hiltViewModel(),
    onUserConnected: () -> Unit
) {

    val onSignInSuccess = signInViewModel::createUser

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }


    val context = LocalContext.current


    val launcher =
        rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            handleResponseAfterSignIn(
                result,
                onBackClick = onBackClick,
                onUserConnected = onUserConnected,
                onSignInSuccess = onSignInSuccess,
                context = context
            )

        }

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.Theme_HexagonalGames)
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(false, true)
        .setLogo(R.mipmap.ic_launcher)
        .build()

    SideEffect {
        launcher.launch(signInIntent)
    }


}

fun handleResponseAfterSignIn(
    result: FirebaseAuthUIAuthenticationResult?,
    onUserConnected: () -> Unit,
    onBackClick: () -> Boolean,
    context: Context,
    onSignInSuccess: () -> Unit
) {
    if (result?.resultCode == RESULT_OK) {
        // Handle sign-in success
        onSignInSuccess()
        onUserConnected()
        Toast.makeText(context, context.getString(R.string.SuccessfulConnexion), Toast.LENGTH_LONG)
            .show()
    } else {
        // ERRORS
        if (result == null) {
            Toast.makeText(context, context.getString(R.string.unknow_error), Toast.LENGTH_LONG)
                .show()
            onBackClick()
        } else if (result.resultCode == ErrorCodes.NO_NETWORK) {
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG)
                .show()
            onBackClick()
        } else if (result.resultCode == ErrorCodes.UNKNOWN_ERROR) {
            Toast.makeText(context, context.getString(R.string.unknow_error), Toast.LENGTH_LONG)
                .show()
            onBackClick()
        }
    }
}
