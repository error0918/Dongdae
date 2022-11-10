@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class, ExperimentalMaterial3Api::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.dongdae.MyView.ChatUnit
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

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Chat() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
                                isMe = true,
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
                                isMe = true,
                                id = id,
                                message = "Message",
                                chatSequence = MyView.ChatSequence.SequenceLast
                            )
                        }
                    }
                }
            }

            var text by rememberSaveable { mutableStateOf("") }
            val maxLength = 300
            OutlinedTextField(
                value = text,
                onValueChange = { if(it.length <= maxLength) text = it },
                isError = text.length >= maxLength,
                shape = MaterialTheme.shapes.medium,
                label =
                    if (text.isNotEmpty()) { { Text(text = "${text.length}/$maxLength") } }
                    else null,
                trailingIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        enabled = text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null
                        )
                    }
                },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Justify),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.outlineVariant,
                    containerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.outlineVariant,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.outlineVariant,
                    errorTrailingIconColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.outlineVariant,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    placeholderColor = MaterialTheme.colorScheme.primary,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedSupportingTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.outlineVariant,
                    errorSupportingTextColor = MaterialTheme.colorScheme.error
                ),
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