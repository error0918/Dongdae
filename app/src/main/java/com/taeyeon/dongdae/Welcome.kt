@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import com.taeyeon.core.Settings
import kotlinx.coroutines.CoroutineScope

object Welcome {
    private val snackbarHostState = SnackbarHostState()
    lateinit var scope: CoroutineScope

    @Composable
    fun Welcome() {
        scope = rememberCoroutineScope()

        Scaffold(
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            WelcomeScreen(paddingValues = paddingValues)
        }
    }

    @Composable
    fun BottomBar() {
        val view = LocalView.current
        (view.context as Activity).window.navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).toArgb()

        BottomAppBar {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "건너뛰기")
                }

                Text(
                    text = "테스트",
                    modifier = Modifier.align(Alignment.Center)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    FloatingActionButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = null
                        )
                    }
                    FloatingActionButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun WelcomeScreen(paddingValues: PaddingValues) {
        val view = LocalView.current
        (view.context as Activity).window.statusBarColor = MaterialTheme.colorScheme.surface.toArgb()
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !when (darkMode) {
            Settings.SYSTEM_MODE -> isSystemInDarkTheme()
            Settings.LIGHT_MODE -> false
            Settings.DARK_MODE -> true
            else -> isSystemInDarkTheme()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .blur(10.dp)
                    .padding(10.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .align(Alignment.Center)
            )

        }
    }

    @Composable
    fun ShadowedText() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .blur(10.dp)
                    .padding(10.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .align(Alignment.Center)
            )

        }
    }

}