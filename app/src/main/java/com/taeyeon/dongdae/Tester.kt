@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.core.Utils
import com.taeyeon.dongdae.Main.pagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Tester {
    private lateinit var scope: CoroutineScope
    var tester by mutableStateOf(true) // TODO
    
    @Composable
    fun Tester() {
        scope = rememberCoroutineScope()

        val dp16 = with(LocalDensity.current) { 16.dp.toPx() }

        val interactionSource = remember { MutableInteractionSource() }

        var isExpanded by rememberSaveable { mutableStateOf(false) }
        var isTestMessageShowing by rememberSaveable { mutableStateOf(true) }
        var x by remember { mutableStateOf(dp16) }
        var y by remember { mutableStateOf(dp16) }

        Popup(
            offset = IntOffset(x = x.toInt(), y = y.toInt())
        ) {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                val roundDp by animateDpAsState(targetValue = if (isExpanded) getCornerSize(shape = MaterialTheme.shapes.large) else getCornerSize(shape = MaterialTheme.shapes.small))
                val scrollState = rememberScrollState()

                Surface(
                    shape = RoundedCornerShape(
                        topStart = roundDp,
                        topEnd = roundDp,
                        bottomStart = roundDp,
                        bottomEnd = animateDpAsState(targetValue = if (isExpanded) 0.dp else getCornerSize(shape = MaterialTheme.shapes.small)).value
                    ),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .indication(
                            interactionSource,
                            LocalIndication.current
                        )
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                x += dragAmount.x
                                y += dragAmount.y
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    if (!isExpanded) {
                                        scope.launch {
                                            Utils.vibrate(10)
                                            val press = PressInteraction.Press(offset)
                                            interactionSource.emit(press)
                                            delay(100)
                                            interactionSource.emit(PressInteraction.Release(press))
                                        }
                                        isExpanded = !isExpanded
                                    }
                                },
                                onLongPress = { offset ->
                                    if (!isExpanded) {
                                        scope.launch {
                                            Utils.vibrate(10)
                                            val press = PressInteraction.Press(offset)
                                            interactionSource.emit(press)
                                            delay(100)
                                            interactionSource.emit(PressInteraction.Release(press))
                                        }
                                        isTestMessageShowing = !isTestMessageShowing
                                    }
                                }
                            )
                        },
                ) {
                    AnimatedContent(
                        targetState = isExpanded,
                        transitionSpec = { scaleIn() with scaleOut() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (it) {
                            Column(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(300.dp)
                                    .padding(16.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .verticalScroll(
                                        scrollState,
                                        enabled = false
                                    ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = """
                                        ${if (Main.isInitialized()) "Main.pagerState.currentPage: ${pagerState.currentPage}" else ""}
                                    """.trimIndent(),
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary
                                                .copy(alpha = 0.2f)
                                                .compositeOver(MaterialTheme.colorScheme.secondaryContainer),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                                )

                                val screenList = Screen.values()
                                Button(
                                    onClick = {
                                        screen =
                                            if (screenList.indexOf(screen) == -1 || screenList.indexOf(screen) + 1 >= screenList.size) screenList[0]
                                            else screenList[screenList.indexOf(screen) + 1]
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "screen: ${screen.name}",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }

                                for(i in 0..10) {
                                    Button(
                                        onClick = {  },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "s"
                                        )
                                    }
                                }

                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .height(56.dp)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Bolt,
                                    contentDescription = null, // TODO
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(24.dp)
                                )
                                AnimatedVisibility(visible = isTestMessageShowing) {
                                    Text(
                                        text = "테스터", // TODO,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandIn(),
                    exit = shrinkOut(),
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            bottomStart = getCornerSize(shape = MaterialTheme.shapes.large),
                            bottomEnd = getCornerSize(shape = MaterialTheme.shapes.large)
                        ),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    x += dragAmount.x
                                    y += dragAmount.y
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    bottom = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ViewQuilt,
                                    contentDescription = null // TODO
                                )
                            }
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        scrollState.animateScrollTo(if (scrollState.value - 100 >= 0) scrollState.value - 100 else 0)
                                    }
                                },
                                enabled = scrollState.value > 0,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = null // TODO
                                )
                            }
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        scrollState.animateScrollTo(if (scrollState.value + 100 <= scrollState.maxValue) scrollState.value + 100 else scrollState.maxValue)
                                    }
                                },
                                enabled = scrollState.value < scrollState.maxValue,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDownward,
                                    contentDescription = null // TODO
                                )
                            }
                            IconButton(
                                onClick = { isExpanded = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FullscreenExit,
                                    contentDescription = null // TODO
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
}