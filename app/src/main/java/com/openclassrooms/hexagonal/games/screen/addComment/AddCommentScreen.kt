package com.openclassrooms.hexagonal.games.screen.addComment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.hexagonal.games.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AddCommentViewModel = hiltViewModel(),
    postId: String
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.add_fragment_label))
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
        }
    ) { contentPadding ->

        val error by viewModel.error.collectAsStateWithLifecycle()
        val comment by viewModel.comment.collectAsStateWithLifecycle()
        CreateComment(
            modifier = Modifier.padding(contentPadding),
            content = comment.content,
            onContentChanged = {viewModel.onAction(FormEventComment.ContentChanged(it))},
            onSaveClicked = { viewModel.addComment(postId)?.addOnSuccessListener { onBackClick() } },
            error = error
        )
    }
}

@Composable
fun CreateComment(
    modifier: Modifier = Modifier,
    content: String,
    onContentChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    error: FormErrorComment?,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {


            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                isError = error is FormErrorComment.ContentCommentError,
                value = content,
                onValueChange = { onContentChanged(it) },
                label = { Text(stringResource(id = R.string.hint_description)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            if (error is FormErrorComment.ContentCommentError) {
                Text(
                    text = stringResource(id = error.messageRes),
                    color = MaterialTheme.colorScheme.error,
                )
            }

        }


        Button(
            enabled = error == null,
            onClick = { onSaveClicked() }
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.action_save)
            )
        }
    }
}
