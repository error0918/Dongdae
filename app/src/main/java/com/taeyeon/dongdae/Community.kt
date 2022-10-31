@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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

            AnimatedVisibility(visible = !lazyListState.isScrollInProgress || (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)) {
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
                    val id = id
                    val name = getName(id)
                    val subName = getSubName(id)
                    val uniqueColor = getUniqueColor(id)

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
                            val (nameText, subNameText, colorView, contentImage, contentText, heart, commentColumn) = createRefs()
                            var nameTextSize by remember { mutableStateOf(IntSize.Zero) }

                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = LocalContentColor.current.copy(0.8f),
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
                                text = "($subName)",
                                style = MaterialTheme.typography.labelSmall,
                                color = LocalContentColor.current.copy(0.4f),
                                modifier = Modifier
                                    .constrainAs(subNameText) {
                                        centerVerticallyTo(nameText)
                                        start.linkTo(nameText.end, margin = 4.dp)
                                    }
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { nameTextSize.height.toDp() })
                                    .height(with(LocalDensity.current) { nameTextSize.height.toDp() })
                                    .background(
                                        color = uniqueColor,
                                        shape = CircleShape
                                    )
                                    .border(
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = LocalContentColor.current
                                        ),
                                        shape = CircleShape
                                    )
                                    .constrainAs(colorView) {
                                        top.linkTo(parent.top)
                                        end.linkTo(parent.end)
                                    }
                            )

                            image?.let {
                                Surface(
                                    modifier = Modifier
                                        .constrainAs(contentImage) {
                                            top.linkTo(nameText.bottom, margin = 8.dp)
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
                                            top.linkTo(if (image == null) nameText.bottom else contentImage.bottom, margin = 8.dp)
                                        }
                                ) {
                                    Text(text = content)
                                }
                            } else {
                                Text(
                                    text = content,
                                    modifier = Modifier
                                        .constrainAs(contentText) {
                                            top.linkTo(if (image == null) nameText.bottom else contentImage.bottom, margin = 8.dp)
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

                            Surface() {

                            }

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