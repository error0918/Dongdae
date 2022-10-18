@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.net.ConnectivityManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.getSystemService
import com.taeyeon.core.Core
import com.taeyeon.core.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object InternetDisconnected {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var scope: CoroutineScope

    @Composable
    fun InternetDisconnected() {
        scope = rememberCoroutineScope()
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp,
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                    )
                    .fillMaxSize()
            ) {
                val (closeButton, appImage, appName, message, retry) = createRefs()

                Button(
                    onClick = { Utils.shutDownApp() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier
                        .constrainAs(closeButton) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                    Text(
                        text = stringResource(id = R.string.close),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Image(
                    imageVector = Icons.Filled.Close,//painter = painterResource(id = R.drawable.ic_launcher_round),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(200.dp)
                        .constrainAs(appImage) {
                            bottom.linkTo(appName.top, margin = 8.dp)
                            centerHorizontallyTo(parent)
                        }
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .constrainAs(appName) {
                            centerTo(parent)
                        }
                )

                Text(
                    text = "테스트", //stringResource(id = R.string.main_network_disconnected_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = MaterialTheme.shapes.medium
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                        .constrainAs(message) {
                            top.linkTo(appName.bottom)
                            bottom.linkTo(retry.top)
                            centerHorizontallyTo(parent)
                        }
                )

                OutlinedButton(
                    onClick = {
                        val isNetworkConnected = Core.getContext().getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnectedOrConnecting ?: false // Check Network Connected
                        if (isNetworkConnected) screen = checkScreen()
                        scope.launch {
                            //snackbarHostState.showSnackbar(if (isNetworkConnected) Core.getContext().getString(R.string.main_network_connect_success) else Core.getContext().getString(R.string.main_network_connect_fail))
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(retry) {
                            bottom.linkTo(parent.bottom)
                            centerHorizontallyTo(parent)
                        }
                ) {
                    Text(
                        text = "테스트", //stringResource(id = R.string.main_network_network_retry),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

}