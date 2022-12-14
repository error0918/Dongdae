@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.*
import kotlinx.coroutines.*

object Welcome {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var scope: CoroutineScope
    private lateinit var pagerState: PagerState
    private val pageList = listOf<@Composable (paddingValues: PaddingValues) -> Unit>(
        { paddingValues -> Page1(paddingValues = paddingValues) },
        { paddingValues -> Page2(paddingValues = paddingValues) },
        { paddingValues -> Page3(paddingValues = paddingValues) },
        { paddingValues -> Page4(paddingValues = paddingValues) },
        { paddingValues -> PageLast(paddingValues = paddingValues) }
    )

    @Composable
    fun Welcome() {
        scope = rememberCoroutineScope()
        pagerState = rememberPagerState()

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            WelcomeContent(paddingValues = paddingValues)
        }
    }

    @Composable
    fun TopBar() {
        SetStatusBarColor(
            color = MaterialTheme.colorScheme.surface
        )

        TopAppBar(
            title = { Text(text = "???????????????") }
        )
    }

    @Composable
    fun BottomBar() {
        SetNavigationBarColor(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )

        BottomAppBar {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        screen = checkScreen()
                    },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "????????????")
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = pagerState.pageCount - 1,
                    modifier = Modifier.align(Alignment.Center),
                    activeColor = MaterialTheme.colorScheme.onSurface,
                    inactiveColor = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage - 1 >= 0) pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = null
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage + 2 < pagerState.pageCount) pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                else screen = checkScreen()
                            }
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
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
    fun Page1(paddingValues: PaddingValues = PaddingValues()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                )
        ) {
            val (appIconImage, appNameText, simpleAppExplanationText) = createRefs()

            /* TODO
            Image(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_launcher_round),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(200.dp)
                    .constrainAs(appIconImage) {
                        bottom.linkTo(appNameText.top, margin = 16.dp)
                    }
            )
            TODO */

            Spacer(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .constrainAs(appIconImage) {
                        centerTo(parent)
                    }
            )

            MyView.AppNameText(
                modifier = Modifier
                    .constrainAs(appNameText) {
                        top.linkTo(appIconImage.bottom, margin = 8.dp)
                        centerHorizontallyTo(parent)
                    }
            )

            Text(
                text = stringResource(id = R.string.simple_app_explanation),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(simpleAppExplanationText) {
                        top.linkTo(appNameText.bottom, margin = 8.dp)
                        centerHorizontallyTo(parent)
                    }
            )

        }
    }

    @Composable
    fun Page2(paddingValues: PaddingValues = PaddingValues()) {
        Text(text = stringResource(id = R.string.app_explanation), modifier = Modifier.padding(paddingValues))
    }

    @Composable
    fun Page3(paddingValues: PaddingValues = PaddingValues()) {
        Page1(paddingValues = paddingValues)
    }

    @Composable
    fun Page4(paddingValues: PaddingValues = PaddingValues()) {
        Page1(paddingValues = paddingValues)
    }

    @Composable
    fun PageLast(paddingValues: PaddingValues = PaddingValues()) {
        Surface(
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "????????????",
                    letterSpacing = 10.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .rotate(90f)
                        .align(Alignment.CenterStart)
                )
            }
        }

        if (pagerState.currentPage == pagerState.pageCount - 1) screen = checkScreen()
    }

    @Suppress("DEPRECATION")
    @Composable
    fun WelcomeContent(paddingValues: PaddingValues = PaddingValues()) {
        HorizontalPager(
            count = pageList.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            pageList[page](paddingValues = paddingValues)
        }

    }

}