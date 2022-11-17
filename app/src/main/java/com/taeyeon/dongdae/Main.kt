@file:OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.taeyeon.core.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Main {
    private val snackbarHostState = SnackbarHostState()
    private val partitionList = listOf(
        Notification.partition,
        Chat.partition,
        Community.partition,
        Profile.partition
    )

    lateinit var scope: CoroutineScope
    lateinit var bottomSheetScaffoldState: BottomSheetScaffoldState
    lateinit var pagerState: PagerState

    var toolbarColor by mutableStateOf(Color.Transparent)


    fun isInitialized(): Boolean = ::pagerState.isInitialized


    @Composable
    fun Main() {
        scope = rememberCoroutineScope()
        bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        pagerState = rememberPagerState()

        BottomSheetScaffold(
            modifier = Modifier.fillMaxWidth(),
            scaffoldState = bottomSheetScaffoldState,
            sheetShape = MaterialTheme.shapes.extraLarge.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
            sheetPeekHeight = 0.dp,
            sheetBackgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            sheetContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            sheetContent = { BottomSheetContent() },
            content = {
                Scaffold(
                    topBar = { TopAppBar() },
                    floatingActionButton = { if (!pagerState.isScrollInProgress) partitionList[pagerState.currentPage].fab?.let { it() } },
                    bottomBar = { BottomBar() },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->
                    MainContent(paddingValues = paddingValues)
                }
            }
        )
    }

    @Suppress("DEPRECATION")
    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun TopAppBar() {
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
            navigationIcon = {
                IconButton(
                    onClick = { /* TODO */ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null // TODO
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded)
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            else
                                bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) null else null // TODO
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
    fun BottomSheetContent() {
        Surface(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    max = (LocalConfiguration.current.screenHeightDp - 72).dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(getCornerSize(shape = MaterialTheme.shapes.extraLarge)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(32.dp)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = CircleShape
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        Utils.vibrate(10)
                                    }
                                )
                            }
                    )
                }

                Text(text = "TODO ".repeat(1000))

            }
        }
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
                pagerState.scrollToPage(defaultTab)
            }
        }
    }

}