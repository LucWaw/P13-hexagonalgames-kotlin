package com.openclassrooms.hexagonal.games.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onFABClick: (Boolean) -> Unit,
    postId: String?,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val post = viewModel.post.collectAsStateWithLifecycle()
    val comments = viewModel.comments.collectAsStateWithLifecycle()
    viewModel.loadPost(postId ?: "")

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = post.value.title)
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

        MainDetail(
            modifier = modifier.padding(contentPadding),
            post = post.value,
            comments = comments.value
        )


    }
}

@Composable
fun MainDetail(modifier: Modifier = Modifier, post: Post, comments: List<Comment>) {
    Column(modifier = modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Detail(

            post = post
        )
        post.description?.let {
            Description(
                description = it
            )
        }
        CommentsSection(
            comments = comments
        )
    }

}

@Composable
fun Detail(modifier: Modifier = Modifier, post: Post) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {

        if (post.photoUrl == null){
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "image",
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(28.dp))
            )
        } else{
            AsyncImage(
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(28.dp)),
                model = post.photoUrl,
                imageLoader = LocalContext.current.imageLoader.newBuilder()
                    .logger(DebugLogger())
                    .build(),
                placeholder = painterResource(id = R.drawable.placeholder),
                contentDescription = "image",
                contentScale = ContentScale.Crop
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(
                    id = R.string.by,
                    post.author?.firstname ?: "",
                    post.author?.lastname ?: ""
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }


    }
}

@Composable
fun Description(modifier: Modifier = Modifier, description: String) {
    Text(modifier = modifier, text = description, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun CommentsSection(modifier: Modifier = Modifier, comments: List<Comment>) {
    Headline()
    Comments(modifier = modifier, comments)
}

@Composable
fun Comments(modifier: Modifier = Modifier, comments: List<Comment>) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (comments.isEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.no_comments),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(comments) { comment ->
                CommentItem(comment = comment)
            }
        }


    }
}

@Composable
fun Headline(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(id = R.string.comments), style = MaterialTheme.typography.headlineSmall)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.arrow_forward),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CommentItem(comment: Comment, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .padding(14.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.by,
                comment.author?.firstname ?: "",
                comment.author?.lastname ?: ""
            ),
            style = MaterialTheme.typography.bodySmall
        )
        Text(text = comment.content, style = MaterialTheme.typography.bodyMedium)
    }
}


@PreviewLightDark
@Composable
fun PreviewDetailScreen() {
    MainDetail(
        post = Post(
            id = "1",
            title = "La soupe est bonne !",
            description = "Description",
            photoUrl = "https://www.example.com",
            timestamp = 0,
            author = User(
                id = "1",
                firstname = "John",
                lastname = "Doe"
            )
        ),
        comments = listOf(
            Comment(
                id = "1",
                content = "Comment 1",
                timestamp = 0,
                author = User(
                    id = "1",
                    firstname = "John",
                    lastname = "Doe"
                )
            ),
            Comment(
                id = "2",
                content = "Comment 2",
                timestamp = 0,
                author = User(
                    id = "2",
                    firstname = "Jane",
                    lastname = "Doe"
                )
            )
        )
    )
}