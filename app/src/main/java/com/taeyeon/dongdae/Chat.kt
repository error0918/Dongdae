@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.launch

object Chat {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "asdf",
        filledIcon = Icons.Filled.Chat,
        outlinedIcon = Icons.Outlined.Chat,
        lazyListState = lazyListState,
        fab = null,
        composable = { Chat() }
    )

    @Composable
    fun Chat() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 80.dp + 16.dp * 2)
            ) {
                items(100) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ChatUnit(
                            isMe = false,
                            name = "안녕 나 애기 사람",
                            subName = "abcde",
                            message = "Message".repeat(100)
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ChatUnit(
                            isMe = true,
                            name = "안녕 나 애기 사람",
                            subName = "abcde",
                            message = "Message"
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ChatUnit(
                            isMe = true,
                            name = "안녕 나 애기 사람",
                            subName = "abcde",
                            message = "Message",
                            chatSequence = ChatSequence.SequenceLast
                        )
                    }
                }
            }

            var text by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                shape = MaterialTheme.shapes.medium,
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        Popup(
            alignment = Alignment.BottomCenter,
            offset = IntOffset(
                x = 0,
                y = with(LocalDensity.current) {
                    -(80.dp + 16.dp * 2).toPx().toInt()
                }
            )
        ) {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                AnimatedVisibility(
                    visible = Main.position == 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AnimatedVisibility(
                        visible = !lazyListState.isScrollInProgress && lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != lazyListState.layoutInfo.totalItemsCount - 1,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FilledIconButton(
                            onClick = {
                                Main.scope.launch {
                                    lazyListState.animateScrollToItem(99)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(true) {
            lazyListState.scrollToItem(99)
        }
    }

    enum class ChatSequence {
        Default, Sequence, SequenceLast
    }

    // TODO: IMAGE
    @Composable
    fun BoxScope.ChatUnit(
        isMe: Boolean,
        name: String,
        subName: String,
        message: String,
        chatSequence: ChatSequence = ChatSequence.Default
    ) {
        val surfaceColor = if (isMe) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surfaceVariant
        Surface(
            modifier = Modifier
                .padding(
                    top = if (chatSequence == ChatSequence.Default) 16.dp else 8.dp,
                    bottom = 0.dp,
                    start = if (isMe) 80.dp else 8.dp,
                    end = if (isMe) 8.dp else 80.dp
                )
                .align(if (isMe) Alignment.CenterEnd else Alignment.CenterStart),
            shape = RoundedCornerShape(
                topStart = if (isMe) 16.dp else 0.dp,
                topEnd = if (isMe) 0.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = surfaceColor
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColorFor(backgroundColor = surfaceColor).copy(0.8f),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "($subName)",
                        color = contentColorFor(backgroundColor = surfaceColor).copy(0.4f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(text = message)
            }
        }
    }

}