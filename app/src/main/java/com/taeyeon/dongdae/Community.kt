@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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

            AnimatedVisibility(visible = !lazyListState.isScrollInProgress || (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            animateColorAsState(
                                targetValue =
                                if (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0)
                                    MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                                else
                                    MaterialTheme.colorScheme.surface,
                                tween(durationMillis = 1000)
                            ).value
                        ),
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
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(vertical = 32.dp)
            ) {
                items(100) {
                    val id = id

                    val image: ImageBitmap? = null

                    val isSelectable = true
                    val content = "게시물 테스트 ".repeat(30)

                    val isHeartAble = true
                    var isHeart by rememberSaveable { mutableStateOf(false) }
                    var heartCount by rememberSaveable { mutableStateOf(12) }
                    val onHeartClicked = { checked: Boolean ->
                        heartCount += if (checked) 1 else -1
                        isHeart = checked
                    }

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 32.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 10.dp,
                            focusedElevation = 10.dp,
                            hoveredElevation = 10.dp,
                            draggedElevation = 10.dp,
                            disabledElevation = 10.dp,
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                                contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                            ) {

                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                                ) {
                                    val (nameUnit, contentImage, contentText, heart, commentColumn) = createRefs()

                                    Chat.NameUnit(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .constrainAs(nameUnit) {
                                                top.linkTo(parent.top)
                                            },
                                        id = id
                                    )

                                    image?.let {
                                        Surface(
                                            modifier = Modifier
                                                .constrainAs(contentImage) {
                                                    top.linkTo(nameUnit.bottom, margin = 8.dp)
                                                }
                                        ) {
                                            Image(
                                                bitmap = image,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    if (isSelectable) {
                                        SelectionContainer(
                                            modifier = Modifier
                                                .constrainAs(contentText) {
                                                    top.linkTo(
                                                        if (image == null) nameUnit.bottom else contentImage.bottom,
                                                        margin = 8.dp
                                                    )
                                                }
                                        ) {
                                            Text(text = content)
                                        }
                                    } else {
                                        Text(
                                            text = content,
                                            modifier = Modifier
                                                .constrainAs(contentText) {
                                                    top.linkTo(
                                                        if (image == null) nameUnit.bottom else contentImage.bottom,
                                                        margin = 8.dp
                                                    )
                                                }
                                        )
                                    }

                                    if (isHeartAble) {
                                        Surface(
                                            onClick = { onHeartClicked(!isHeart) },
                                            color = Color.Transparent,
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .constrainAs(heart) {
                                                    top.linkTo(contentText.bottom, margin = 8.dp)
                                                    centerHorizontallyTo(parent)
                                                }
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isHeart) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                                    tint = if (isHeart) Color.Red else LocalContentColor.current,
                                                    contentDescription = null // TODO
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = "$heartCount")
                                            }
                                        }
                                    }
                                }

                            }

                            Text("adfajhklhgjhklhjhdsf".repeat(100))

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