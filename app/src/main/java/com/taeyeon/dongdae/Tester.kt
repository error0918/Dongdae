@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import com.taeyeon.core.Utils
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
            Surface(
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
                                scope.launch {
                                    Utils.vibrate(10)
                                    val press = PressInteraction.Press(offset)
                                    interactionSource.emit(press)
                                    delay(100)
                                    interactionSource.emit(PressInteraction.Release(press))
                                }
                                isExpanded = !isExpanded
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
                shape = RoundedCornerShape(animateDpAsState(targetValue = if (isExpanded) getCornerSize(shape = MaterialTheme.shapes.large) else getCornerSize(shape = MaterialTheme.shapes.small)).value),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shadowElevation = 12.dp,
            ) {
                AnimatedContent(
                    targetState = isExpanded,
                    transitionSpec = { scaleIn() with scaleOut() },
                    contentAlignment = Alignment.Center
                ) {
                    if (it) {
                        Row(
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .height(IntrinsicSize.Min)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = ("adsf".repeat(5) + "\n").repeat(10))
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.small,
                                onClick = {}
                            ) {
                                Box(modifier = Modifier.fillMaxHeight()) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowLeft,
                                        contentDescription = null, // TODO
                                        modifier = Modifier.align(Alignment.Center)
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
        }
    }
    
}