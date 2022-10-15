@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.dongdae.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope

var screen by mutableStateOf(Screen.Main)

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

        //screen = checkScreen()
        screen = Screen.Welcome

        setContent {
            loadSettings()
            Theme {
                Surface {
                    AnimatedContent(
                        targetState = screen
                    ) {
                        when (it) {
                            Screen.Main -> {
                                load()
                                Box(
                                    modifier = Modifier.animateEnterExit(
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    )
                                ) {
                                    Main.Main()
                                }
                            }
                            Screen.Welcome -> {
                                Box(
                                    modifier = Modifier.animateEnterExit(
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    )
                                ) {
                                    Welcome.Welcome()
                                }
                            }
                            Screen.InternetDisconnected -> {
                                Box(
                                    modifier = Modifier.animateEnterExit(
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    )
                                ) {
                                }
                            }
                        }
                    }
                }
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
    var position by mutableStateOf(1)
    lateinit var scope: CoroutineScope

    @Composable
    fun Main() {
        scope = rememberCoroutineScope()

        Scaffold(
            topBar = { Toolbar() },
            floatingActionButton = partitionList[position].fab ?: {},
            bottomBar = { NavigationBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            MainContent(paddingValues = paddingValues)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Toolbar() {
        val isScrolled = if (partitionList[position].lazyListState != null) partitionList[position].lazyListState!!.firstVisibleItemIndex != 0 || partitionList[position].lazyListState!!.firstVisibleItemScrollOffset != 0 else false
        val toolbarColor by animateColorAsState(
            targetValue =
                if(isScrolled)
                    MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                else
                    MaterialTheme.colorScheme.surface,
            tween(durationMillis = 1000)
        )
        val toolbarContentColor by animateColorAsState(
            targetValue =
                contentColorFor(
                    backgroundColor = if(isScrolled)
                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    else
                        MaterialTheme.colorScheme.surface
                ),
            tween(durationMillis = 1000)
        )

        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = toolbarColor,
                navigationIconContentColor = toolbarContentColor,
                titleContentColor = toolbarContentColor,
                actionIconContentColor = toolbarContentColor
            )
        )

        val view = LocalView.current
        (view.context as Activity).window.statusBarColor = toolbarColor.toArgb()
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !when (darkMode) {
            Settings.SYSTEM_MODE -> isSystemInDarkTheme()
            Settings.LIGHT_MODE -> false
            Settings.DARK_MODE -> true
            else -> isSystemInDarkTheme()
        }
    }

    @Composable
    fun NavigationBar() {
        val view = LocalView.current
        (view.context as Activity).window.navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).toArgb()

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