package com.openclassrooms.hexagonal.games.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onFABClick: (Boolean) -> Unit,
    postId: String?,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.my_account_fragment_label))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back)
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFABClick(viewModel.isCurrentUserLogged())
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.description_button_add)
                )
            }
        }
    ) { contentPadding ->
        if (!viewModel.isNetworkAvailable(LocalContext.current)) {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
        val post = viewModel.post.collectAsStateWithLifecycle()
        //TODO load post
        Column(modifier = modifier.padding(contentPadding)) {
            Text(text = if (postId.isNullOrEmpty()) "Post not found" else postId)
        }
        /*

                Detail(
                    modifier = modifier.padding(contentPadding),
                    post = post.value
                )
                Description(

                )
                CommentsSection(

                )*/

    }
}

@Composable
fun Detail(modifier: Modifier = Modifier, post: Post) {
    Column(
        modifier = modifier
    ) {
        Text(text = post.title)

    }
}

@Composable
fun Description(modifier: Modifier = Modifier) {
    //TODO: Not yet implemented
}

@Composable
fun CommentsSection(modifier: Modifier = Modifier) {
    Title()
    Comments()
}

@Composable
fun Comments(modifier: Modifier = Modifier) {
    //TODO: Not yet implemented
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    //TODO: Not yet implemented
}