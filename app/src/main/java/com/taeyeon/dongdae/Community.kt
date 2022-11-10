@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.core.Utils
import kotlinx.coroutines.launch

object Community {
    private var isWritingPost by mutableStateOf(false)

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
            var isDropDownMenuExpanded by remember { mutableStateOf(false) }
            var buttonSize by remember { mutableStateOf(IntSize.Zero) }

            AnimatedVisibility(visible = !lazyListState.isScrollInProgress || (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    LazyRow(
                        modifier = Modifier
                            .weight(2f)
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

                    OutlinedButton(
                        onClick = { isDropDownMenuExpanded = !isDropDownMenuExpanded },
                        shape = RoundedCornerShape(percent = 20),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .onSizeChanged { intSize ->
                                buttonSize = intSize
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ("최신 순"),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = null // TODO
                            )
                        }

                        DropdownMenu(
                            expanded = isDropDownMenuExpanded,
                            onDismissRequest = { isDropDownMenuExpanded = false },
                            modifier = Modifier.width(with(LocalDensity.current) { buttonSize.width.toDp() }),
                            offset = DpOffset(x = (-24).dp, y = 8.dp)
                        ) {
                            for (i in 1..10) {
                                DropdownMenuItem(
                                    text = { Text(text = "TODO $i") },
                                    onClick = { /*TODO*/ }
                                )
                            }
                        }
                    }

                }

            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 32.dp + 16.dp
                )
            ) {

                item {

                    /*val id = id

                    val image: ImageBitmap? = null

                    val isSelectable = true
                    val content = "게시 "

                    val isHeartAble = true
                    var isHeart by rememberSaveable { mutableStateOf(false) }
                    var heartCount by rememberSaveable { mutableStateOf(12) }
                    val onHeartClicked = { checked: Boolean ->
                        heartCount += if (checked) 1 else -1
                        isHeart = checked
                    }

                    val commentList = listOf(
                        MyView.ChatData(
                            isMe = true,
                            id = id,
                            message = "댓글 테스트",
                            chatSequence = MyView.ChatSequence.Start
                        ),
                        MyView.ChatData(
                            isMe = false,
                            id = id,
                            message = "댓글 테스트"
                        )
                    )*/

                    MyView.PostUnit(
                        id = id,
                        content = "얘들아 오늘 급식 어떤 거 나와? 🤤",
                        commentList = listOf(
                            MyView.ChatData(
                                isMe = false,
                                id = id,
                                message = "오늘은 탕수육 나온대!",
                                chatSequence = MyView.ChatSequence.Start
                            ),
                            MyView.ChatData(
                                isMe = true,
                                id = id,
                                message = "와 정말? 맛있겠다!!",
                                chatSequence = MyView.ChatSequence.Start
                            ),
                            MyView.ChatData(
                                isMe = false,
                                id = id,
                                message = "응! 나도 기대하고 있어",
                                chatSequence = MyView.ChatSequence.Start
                            )
                        )
                    )

                }

                item {
                    MyView.PostUnit(
                        id = id,
                        content = "한국사 수행평가 범위 어디야?",
                        commentList = listOf(
                            MyView.ChatData(
                                isMe = false,
                                id = id,
                                message = "아마 우리가 2학기 때 배우는 거 전체일걸?",
                                chatSequence = MyView.ChatSequence.Start
                            ),
                            MyView.ChatData(
                                isMe = true,
                                id = id,
                                message = "고마워~",
                                chatSequence = MyView.ChatSequence.Start
                            )
                        )
                    )
                }

                item {
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
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                            contentColor =  MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                        ) {
                            Text(
                                text = "개시물을 모두 다 보셨습니다. 😄",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
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
                    visible = Main.pagerState.currentPage == 2,
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

        if (isWritingPost) {
            WritePostDialog()
        }
    }

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Fab() {
        ExtendedFloatingActionButton(
            onClick = {
                Utils.vibrate(10)
                isWritingPost = true
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

    @Composable
    fun WritePostDialog() {
        var image by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }
        var isSelectable by rememberSaveable { mutableStateOf(true) }
        var content by rememberSaveable { mutableStateOf("") }
        var isHeartAble by rememberSaveable { mutableStateOf(false) }
        var postCategory by rememberSaveable { mutableStateOf(MyView.PostCategory.Unspecified) }
        
        LaunchedEffect(true) {
            // TODO
        }
        
        MyView.BaseDialog(
            onDismissRequest = { isWritingPost = false },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            icon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null // TODO
                )
            },
            title = { Text(text = "글 작성하기") },
            content = {
                Column(modifier = Modifier.fillMaxSize()) {

                }
            },
            button = {
                MyView.DialogButtonRow {
                    TextButton(
                        onClick = { isWritingPost = false }
                    ) {
                        Text(text = "닫기") // TODO
                    }
                    TextButton(
                            onClick = { /* TODO */ }
                            ) {
                        Text(text = "개시하기") // TODO
                    }
                }
            }
        )
    }

}