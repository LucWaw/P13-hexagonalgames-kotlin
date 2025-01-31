package com.openclassrooms.hexagonal.games.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object HomeFeed : Screen("homeFeed")

    data object AddPost : Screen("addPost")

    data object Settings : Screen("settings")

    data object SignIn : Screen("signIn")

    data object Profile : Screen("profile")

    data object Detail : Screen("detail", navArguments = listOf(navArgument("postId") { type = NavType.StringType }))

    data object AddComment : Screen("addComment", navArguments = listOf(navArgument("postId") { type = NavType.StringType }))
}