package com.openclassrooms.hexagonal.games.screen.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.theme.HexagonalGamesTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.action_settings))
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
        Settings(
            modifier = Modifier.padding(contentPadding),
            onNotificationDisabledClicked = { viewModel.disableNotifications() },
            onNotificationEnabledClicked = {
                viewModel.enableNotifications()
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    onNotificationEnabledClicked: () -> Unit,
    onNotificationDisabledClicked: () -> Unit,
    mockNotificationPermissionState: PermissionState? = null // Pour la prévisualisation
) {
    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mockNotificationPermissionState ?: rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_notifications),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(id = R.string.contentDescription_notification_icon)
        )
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (notificationsPermissionState?.status?.isGranted == false) {
                        notificationsPermissionState.launchPermissionRequest()
                    }
                }

                onNotificationEnabledClicked()
            }
        ) {
            Text(text = stringResource(id = R.string.notification_enable))
        }
        Button(
            onClick = { onNotificationDisabledClicked() }
        ) {
            Text(text = stringResource(id = R.string.notification_disable))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
private fun SettingsPreview() {
    val mockPermissionState = object : PermissionState {
        override val permission = android.Manifest.permission.POST_NOTIFICATIONS
        override val status = PermissionStatus.Granted
        override fun launchPermissionRequest() {}
    }

    HexagonalGamesTheme {
        Settings(
            onNotificationEnabledClicked = { },
            onNotificationDisabledClicked = { },
            mockNotificationPermissionState = mockPermissionState
        )
    }
}
