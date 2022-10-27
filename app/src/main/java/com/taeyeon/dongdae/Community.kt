@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.core.Utils
import kotlinx.coroutines.launch

object Community {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "TEST",
        filledIcon = Icons.Filled.People,
        outlinedIcon = Icons.Outlined.PeopleOutline,
        lazyListState = lazyListState,
        fab = { Fab() },
        composable = { Community() }
    )

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Community() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            var selectedIndex by rememberSaveable { mutableStateOf(0) }

            AnimatedVisibility(visible = !lazyListState.isScrollInProgress) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Main.toolbarColor),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(100) { index ->
                        val selected = index == selectedIndex
                        FilterChip(
                            selected = selected,
                            onClick = { selectedIndex = index },
                            label = { Text(text = "TODO") },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                                    contentDescription = null // TODO
                                )
                            }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(100) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                        )
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                        ) {
                            val (nameText, subNameText, colorView, heartIconButton, heartText, contentImage, contentText, commentColumn) = createRefs()
                            var nameTextSize by remember { mutableStateOf(IntSize.Zero) }

                            Text(
                                text = "이름",
                                modifier = Modifier
                                    .onSizeChanged { intSize ->
                                        nameTextSize = intSize
                                    }
                                    .constrainAs(nameText) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                            )

                            Text(
                                text = "(${"서브네임"})",
                                modifier = Modifier
                                    .constrainAs(nameText) {
                                        centerHorizontallyTo(nameText)
                                        start.linkTo(nameText.start)
                                    }
                            )

                            Spacer(
                                modifier = Modifier
                                    .background(
                                        color = Color.Cyan,
                                        shape = CircleShape
                                    )
                                    .constrainAs(colorView) {

                                    }
                            )


                        }
                    }
                }
            }
        }

        Popup(
            alignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                AnimatedVisibility(
                    visible = Main.pagerState.currentPage == 1,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AnimatedVisibility(
                        visible = !lazyListState.isScrollInProgress && (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FilledIconButton(
                            onClick = {
                                Main.scope.launch {
                                    lazyListState.animateScrollToItem(0)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Fab() {
        ExtendedFloatingActionButton(
            onClick = {
            /*TODO*/
                Utils.vibrate(10)
            }
        ) {
            animateColorAsState(
                targetValue = if (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "글쓰기"
            )
            AnimatedVisibility(visible = !lazyListState.isScrollInProgress) {
                Text(
                    text = "글쓰기",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

}