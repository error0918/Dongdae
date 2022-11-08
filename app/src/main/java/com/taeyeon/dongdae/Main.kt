@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Main {
    lateinit var pagerState: PagerState
    var toolbarColor by mutableStateOf(Color.Transparent)

    fun isInitialized(): Boolean = ::pagerState.isInitialized

    private val snackbarHostState = SnackbarHostState()
    private val partitionList = listOf(
        Notification.partition,
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
        toolbarColor = animateColorAsState(
            targetValue =
            if(isScrolled)
                MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            else
                MaterialTheme.colorScheme.surface,
            tween(durationMillis = 1000)
        ).value
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
                pagerState.scrollToPage(2)
            }
        }
    }

}