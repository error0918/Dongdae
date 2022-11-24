@file:OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
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
import com.google.firebase.database.ktx.snapshots
import com.taeyeon.dongdae.MyView.ChatUnit
import com.taeyeon.dongdae.data.ChatData
import kotlinx.coroutines.flow.collectIndexed
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

    private val chatDataList =  mutableStateListOf<ChatData>()

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Chat() {
        LaunchedEffect(true) {
            FDManager.initializeChat(
                onInitialized = {
                    Main.scope.launch {
                        FDManager.chatDatabase.snapshots.collectIndexed { _, snapshot ->
                            if (snapshot.hasChildren()) {
                                val value = snapshot.children.first()
                                value.getValue(ChatData::class.java)?.let {
                                    chatDataList.add(it)
                                }
                            }
                        }
                    }
                },
                onChildAdded = { snapshot, _ ->
                    if (snapshot.hasChildren()) {
                        val value = snapshot.children.first()
                        value.getValue(ChatData::class.java)?.let { chatData ->
                            if (chatDataList.indexOf(chatData) == -1)
                                chatDataList.add(chatData)

                            // TODO
                        }
                    }
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 100.dp + 8.dp * 2)
            ) {

                items(chatDataList) { chatData ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (chatData.id.isNotBlank() && chatData.message.isNotBlank())
                            ChatUnit(
                                chatData = chatData
                            )
                    }
                }

            }

            var text by rememberSaveable { mutableStateOf("") }
            val maxLength = 300
            val contentColor by animateColorAsState(targetValue = if (text.length >= maxLength) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)

            MyView.MyTextField(
                value = text,
                onValueChange = { if(it.length <= maxLength) text = it },
                trailingIcon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                FDManager.chatDatabase.child(chatDataList.size.toString()).push().setValue(
                                    ChatData(
                                        id = id,
                                        message = text,
                                        chatId = chatDataList.size
                                    )
                                )
                                text = ""
                            },
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

            Popup(
                alignment = Alignment.BottomCenter,
                offset = IntOffset(
                    x = 0,
                    y = with(LocalDensity.current) {
                        -(80.dp - 12.dp).toPx().toInt()
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(24.dp)
                        .let {
                            if (Main.pagerState.currentPage == 1 && Main.bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                                it.background(
                                    color = animateColorAsState(targetValue = if (text.length >= maxLength) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer).value,
                                    shape = MaterialTheme.shapes.medium
                                )
                            else it
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedVisibility(
                        visible = Main.pagerState.currentPage == 1,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        AnimatedVisibility(
                            visible = text.isNotEmpty() && Main.bottomSheetScaffoldState.bottomSheetState.isCollapsed,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            Text(
                                text = "${text.length}/$maxLength",
                                style = MaterialTheme.typography.bodySmall,
                                color = animateColorAsState(targetValue = if (text.length >= maxLength) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer).value
                            )
                        }
                    }
                }
            }
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
                        visible = !lazyListState.isScrollInProgress && lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != lazyListState.layoutInfo.totalItemsCount - 1 && Main.bottomSheetScaffoldState.bottomSheetState.isCollapsed,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FilledIconButton(
                            onClick = {
                                Main.scope.launch {
                                    lazyListState.animateScrollToItem(chatDataList.size - 1)
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