@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.dongdae.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

        setContent {
            //screen = rememberSaveable { checkScreen() }
            screen = rememberSaveable { Screen.Welcome }

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
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    )
                                ) {
                                    Main.Main()
                                }
                            }
                            Screen.Welcome -> {
                                Box(
                                    modifier = Modifier.animateEnterExit(
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    )
                                ) {
                                    Welcome.Welcome()
                                }
                            }
                            Screen.InternetDisconnected -> {
                                Box(
                                    modifier = Modifier.animateEnterExit(
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    )
                                ) {
                                    InternetDisconnected.InternetDisconnected()
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
    lateinit var pagerState: PagerState

    private val snackbarHostState = SnackbarHostState()
    private val partitionList = listOf(
        Chat.partition,
        Community.partition,
        Profile.partition
    )
    lateinit var scope: CoroutineScope

    @Composable
    fun Main() {
        scope = rememberCoroutineScope()
        pagerState = rememberPagerState()

        Scaffold(
            topBar = { Toolbar() },
            floatingActionButton = { if (!pagerState.isScrollInProgress) partitionList[pagerState.currentPage].fab?.let { it() } },
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            MainContent(paddingValues = paddingValues)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Toolbar() {
        val isScrolled = if (partitionList[pagerState.currentPage].lazyListState != null) partitionList[pagerState.currentPage].lazyListState!!.firstVisibleItemIndex != 0 || partitionList[pagerState.currentPage].lazyListState!!.firstVisibleItemScrollOffset != 0 else false
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

        SetStatusBarColor(
            color = toolbarColor
        )
    }

    @Composable
    fun BottomBar() {
        SetNavigationBarColor()

        NavigationBar {
            partitionList.forEachIndexed { index, partition ->
                val selected = index == pagerState.currentPage
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
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
            HorizontalPager(
                count = partitionList.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                partitionList[it].composable()
            }
            LaunchedEffect(true) {
                pagerState.scrollToPage(1)
            }
        }
    }

}