package com.openclassrooms.hexagonal.games.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.screen.Screen
import com.openclassrooms.hexagonal.games.screen.account.SignInScreen
import com.openclassrooms.hexagonal.games.screen.addComment.AddCommentScreen
import com.openclassrooms.hexagonal.games.screen.addPost.AddScreen
import com.openclassrooms.hexagonal.games.screen.detail.DetailScreen
import com.openclassrooms.hexagonal.games.screen.homefeed.HomeFeedScreen
import com.openclassrooms.hexagonal.games.screen.profile.ProfileScreen
import com.openclassrooms.hexagonal.games.screen.settings.SettingsScreen
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the application. This activity serves as the entry point and container for the navigation
 * fragment. It handles setting up the toolbar, navigation controller, and action bar behavior.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            HexagonalGamesTheme {
                HexagonalGamesNavHost(navHostController = navController)
            }
        }
    }

}

@Composable
fun HexagonalGamesNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController, startDestination = Screen.HomeFeed.route
    ) {
        composable(route = Screen.HomeFeed.route) {
            HomeFeedScreen(onPostClick = {
                navHostController.navigate("${Screen.Detail.route}/${it}")
            }, onSettingsClick = {
                navHostController.navigate(Screen.Settings.route)
            }, onAccountClick = { goToProfile ->
                if (goToProfile) navHostController.navigate(Screen.Profile.route)
                else navHostController.navigate(Screen.SignIn.route)
            }, onFABClick = { isUserLogged ->
                if (isUserLogged) {
                    navHostController.navigate(Screen.AddPost.route)
                } else {
                    Toast.makeText(
                        navHostController.context,
                        navHostController.context.getString(R.string.post_account_mandatory),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        composable(route = Screen.AddPost.route) {
            AddScreen(onBackClick = { navHostController.navigateUp() },
                onSaveClick = { navHostController.navigateUp() })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(onBackClick = { navHostController.navigateUp() })
        }
        composable(route = Screen.SignIn.route) {
            SignInScreen(onBackClick = { navHostController.navigateUp() },
                onUserConnected = { navHostController.navigate(Screen.HomeFeed.route) })
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(onBackClick = { navHostController.navigateUp() })
        }
        composable(
            route = "${Screen.Detail.route}/{postId}",
            arguments = Screen.Detail.navArguments
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
            DetailScreen(
                onFABClick = { isUserLogged ->
                    if (isUserLogged) navHostController.navigate(Screen.AddComment.route)
                    else Toast.makeText(
                        navHostController.context,
                        navHostController.context.getString(R.string.comment_account_mandatory),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onBackClick = { navHostController.navigateUp() },
                postId = postId, // Passer postId comme un entier
            )
        }

        composable(route = Screen.AddComment.route) {
            AddCommentScreen()
        }
    }
}