package com.openclassrooms.hexagonal.games.screen.profile

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.tasks.Task
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.OrangeWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Boolean
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
        }
    ) { contentPadding ->
        val context = LocalContext.current
        Profile(
            modifier = Modifier.padding(contentPadding),
            onDeleteUser = { openDeleteDialog(viewModel::deleteCurrentUser, onBackClick, context) },
            onSignOutUser = { viewModel.signOutCurrentUser(context); onBackClick() })

    }
}

private fun openDeleteDialog(
    deleteUser: (Context) -> Task<Void>,
    onBackClick: () -> Boolean,
    context: Context
) {
    AlertDialog.Builder(context)
        .setMessage(R.string.popup_message_confirmation_delete_account)
        .setPositiveButton(
            R.string.popup_message_choice_yes
        ) { _, _ ->
            deleteUser(context)
                .addOnSuccessListener {
                    onBackClick()
                }
        }
        .setNegativeButton(R.string.popup_message_choice_no, null)
        .show()
}


@Composable
fun Profile(modifier: Modifier = Modifier, onDeleteUser: () -> Unit, onSignOutUser: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.account_circle),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(id = R.string.contentDescription_notification_icon)
        )
        ButtonWithIcon(
            text = stringResource(id = R.string.logout),
            icon = painterResource(id = R.drawable.logout),
            color = OrangeWarning,
            onClick = { onSignOutUser() },
            shape = CircleShape
        )
        ButtonWithIcon(
            text = stringResource(id = R.string.delete_account),
            icon = painterResource(id = R.drawable.delete),
            color = MaterialTheme.colorScheme.error,
            onClick = { onDeleteUser() },
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}

@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    color: Color,
    onClick: () -> Unit,
    shape: Shape
) {
    Button(
        modifier = modifier
            .width(267.dp)
            .height(68.dp),
        shape = shape
        ,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Text(text = text)

            Icon(
                painter = icon,
                contentDescription = text
            )
        }

    }
}

@Preview
@Composable
fun ButtonWithIconPreview() {
    ButtonWithIcon(
        text = stringResource(id = R.string.logout),
        icon = painterResource(id = R.drawable.logout),
        onClick = {},
        color = OrangeWarning,
        shape = MaterialTheme.shapes.large
    )
}


@PreviewLightDark
@PreviewScreenSizes
@Composable
fun ProfileScreenPreview() {
    Profile(
        onDeleteUser = {},
        onSignOutUser = {}
    )
}
