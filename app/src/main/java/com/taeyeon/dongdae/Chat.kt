@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.taeyeon.dongdae.MyView.ChatUnit
import com.taeyeon.dongdae.data.ChatSequence
import kotlinx.coroutines.launch

object Chat {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "채팅", // TODO
        filledIcon = Icons.Filled.Chat,
        outlinedIcon = Icons.Outlined.Chat,
        lazyListState = lazyListState,
        fab = null,
        composable = { Chat() }
    )

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Chat() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val database = Firebase.database.getReference("chat")

            database.addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 100.dp + 8.dp * 2)
            ) {
                for (i in 0 until 100) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ChatUnit(
                                isMe = false,
                                id = id,
                                message = "Message".repeat(100)
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ChatUnit(
                                id = id,
                                message = "Message"
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ChatUnit(
                                id = id,
                                message = "Message",
                                chatSequence = ChatSequence.SequenceLast
                            )
                        }
                    }
                }
            }

            var text by rememberSaveable { mutableStateOf("") }
            val maxLength = 300
            val contentColor by animateColorAsState(targetValue = if (text.length >= maxLength) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            MyView.MyTextField(
                value = text,
                onValueChange = { if(it.length <= maxLength) text = it },
                /*label =
                    if (text.isNotEmpty()) { { Text(text = "${text.length}/$maxLength") } }
                    else null,*/
                trailingIcon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            enabled = text.isNotBlank(),
                            modifier = Modifier
                                .width(40.dp)
                                .height(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = null // TODO
                            )
                        }
                        if (text.isNotEmpty())
                            Text(
                                text = "${text.length}/$maxLength",
                                style = MaterialTheme.typography.bodySmall
                            )
                    }
                },
                textFiledAlignment = Alignment.CenterStart,
                cursorBrush = SolidColor(contentColor),
                containerColor = Color.Transparent,
                contentColor = contentColor,
                textColor = contentColor,
                border = BorderStroke(width = 1.dp, color = contentColor),
                modifier = Modifier
                    .padding(8.dp)
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
                    -(80.dp + 8.dp * 2).toPx().toInt()
                }
            )
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
                        visible = !lazyListState.isScrollInProgress && lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != lazyListState.layoutInfo.totalItemsCount - 1,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FilledIconButton(
                            onClick = {
                                Main.scope.launch {
                                    lazyListState.animateScrollToItem(299)
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
            lazyListState.scrollToItem(299)
        }
    }

}