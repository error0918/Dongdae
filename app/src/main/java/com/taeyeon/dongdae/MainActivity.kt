@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.dongdae.ui.theme.Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofPropertyValuesHolder(
                    splashScreenView.iconView,
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f)
                ).run {
                    interpolator = AnticipateInterpolator()
                    duration = 100L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }
        installSplashScreen()


        Core.initialize(applicationContext)

        super.onCreate(savedInstanceState)

        Core.activityCreated(this)

        setContent {
            load()
            Theme {
                Main.Main()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Settings.applyFullScreenMode()
        Settings.applyScreenAlwaysOn()
    }

    override fun onRestart() {
        super.onRestart()
        Core.activityCreated(this)
    }

}

object Main {
    private val snackbarHostState = SnackbarHostState()
    private val partitionList = listOf(
        Chat.partition,
        Community.partition,
        Profile.partition
    )
    private var position by mutableStateOf(1)

    @Composable
    fun Main() {
        Scaffold(
            topBar = { Toolbar() },
            floatingActionButton = {},
            bottomBar = { NavigationBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            MainContent(paddingValues = paddingValues)
        }
    }

    @Composable
    fun Toolbar() {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) }
        )
    }

    @Composable
    fun NavigationBar() {
        NavigationBar {
            partitionList.forEachIndexed { index, partition ->
                val selected = index == position
                NavigationBarItem(
                    selected = selected,
                    onClick = { position = index },
                    icon = {
                        Icon(
                            imageVector = if (selected) partition.filledIcon else partition.outlinedIcon,
                            contentDescription = partition.title
                        )
                    },
                    label = {
                        Text(text = partition.title)
                    }
                )
            }
        }
    }

    @Composable
    fun MainContent(paddingValues: PaddingValues) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Crossfade(targetState = position) {
                partitionList[it].composable()
            }
        }
    }

}