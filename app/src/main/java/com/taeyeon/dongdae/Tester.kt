@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.core.Settings
import com.taeyeon.core.Utils
import com.taeyeon.dongdae.Main.pagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Tester {
    var tester by mutableStateOf(true) // TODO

    private var testDialog by mutableStateOf(false)

    private lateinit var scope: CoroutineScope

    @Composable
    fun Tester() {
        scope = rememberCoroutineScope()

        val dp16 = with(LocalDensity.current) { 16.dp.toPx() }

        val interactionSource = remember { MutableInteractionSource() }

        var isExpanded by rememberSaveable { mutableStateOf(false) }
        var isAttached by rememberSaveable { mutableStateOf(false) }
        var isTestMessageShowing by rememberSaveable { mutableStateOf(true) }

        var size by remember { mutableStateOf(IntSize.Zero) }
        var screenSize by remember { mutableStateOf(IntSize.Zero) }
        screenSize = with(LocalDensity.current) { IntSize(LocalConfiguration.current.screenWidthDp.dp.toPx().toInt(), LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()) }

        var x by remember { mutableStateOf(dp16) }
        var y by remember { mutableStateOf(dp16) }

        if (testDialog) TestDialog()

        Popup(
            offset =
                IntOffset(
                    x = animateIntAsState(targetValue = if (isAttached) 0 else x.toInt()).value,
                    y = y.toInt()
                )
        ) {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .onSizeChanged { intSize ->
                        size = intSize
                    }
            ) {
                val roundDp by animateDpAsState(targetValue = if (isExpanded && !isAttached) getCornerSize(shape = MaterialTheme.shapes.large) else getCornerSize(shape = MaterialTheme.shapes.small))
                val scrollState = rememberScrollState()

                Surface(
                    shape = RoundedCornerShape(
                        topStart = animateDpAsState(targetValue = if (isAttached) 0.dp else roundDp).value,
                        topEnd = roundDp,
                        bottomStart = animateDpAsState(targetValue = if (isAttached) 0.dp else roundDp).value,
                        bottomEnd = animateDpAsState(targetValue = if (isExpanded && !isAttached) 0.dp else getCornerSize(shape = MaterialTheme.shapes.small)).value
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
                                if (!isAttached)
                                    x =
                                        if (x + dragAmount.x < 0) 0f else if (screenSize.width < x + dragAmount.x + size.width) screenSize.width.toFloat() - size.width.toFloat() else x + dragAmount.x
                                y =
                                    if (y + dragAmount.y < 0) 0f else if (screenSize.height < y + dragAmount.y + size.height) screenSize.height.toFloat() - size.height.toFloat() else y + dragAmount.y
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
                            AnimatedContent(
                                targetState = isAttached,
                                transitionSpec = { scaleIn() with scaleOut() },
                                contentAlignment = Alignment.Center
                            ) { attached ->
                                if (attached) {
                                    IconButton(
                                        onClick = { isAttached = false },
                                        modifier = Modifier
                                            .width(28.dp)
                                            .height(56.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowRight,
                                            contentDescription = null // TODO
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .width(250.dp)
                                            .height(300.dp)
                                            .padding(16.dp)
                                    ) {

                                        Text(
                                            text = stringResource(id = R.string.app_name) + "테스터", // TODO
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .clip(MaterialTheme.shapes.medium)
                                                .verticalScroll(
                                                    scrollState,
                                                    enabled = false
                                                ),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {

                                            Text(
                                                text = """
                                                id: $id
                                                name: $name
                                                subName: $subName
                                            """.trimIndent()
                                                        + if (Main.isInitialized()) "\n" + "Main.pagerState.currentPage: ${pagerState.currentPage}" else "",
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

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(IntrinsicSize.Min),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = "uniqueColor",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier
                                                        .background(
                                                            color = MaterialTheme.colorScheme.secondary
                                                                .copy(alpha = 0.2f)
                                                                .compositeOver(MaterialTheme.colorScheme.secondaryContainer),
                                                            shape = MaterialTheme.shapes.medium
                                                        )
                                                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                                                )
                                                Spacer(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxHeight()
                                                        .background(
                                                            color = uniqueColor,
                                                            shape = MaterialTheme.shapes.medium
                                                        )
                                                )
                                            }

                                            val screenList = Screen.values()
                                            Button(
                                                onClick = {
                                                    screen =
                                                        if (screenList.indexOf(screen) == -1 || screenList.indexOf(
                                                                screen
                                                            ) + 1 >= screenList.size
                                                        ) screenList[0]
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
                                                    text = "screen: ${screen.name}"
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    fullScreenMode = !fullScreenMode
                                                    save()
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = animateColorAsState(targetValue = if (fullScreenMode) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)).value,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "fullScreenMode: $fullScreenMode"
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    screenAlwaysOn = !screenAlwaysOn
                                                    save()
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = animateColorAsState(targetValue = if (screenAlwaysOn) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)).value,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "screenAlwaysOn: $screenAlwaysOn"
                                                )
                                            }

                                            val darkModeList = Settings.DarkMode.values()
                                            Button(
                                                onClick = {
                                                    darkMode =
                                                        if (darkModeList.indexOf(darkMode) == -1 || darkModeList.indexOf(darkMode) + 1 >= darkModeList.size) darkModeList[0]
                                                        else darkModeList[darkModeList.indexOf(darkMode) + 1]
                                                    save()
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
                                                    text = "darkMode: $darkMode"
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    dynamicColor = !dynamicColor
                                                    save()
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = animateColorAsState(targetValue = if (dynamicColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)).value,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "dynamicColor: $dynamicColor"
                                                )
                                            }

                                            Button(
                                                onClick = { load() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "load()"
                                                )
                                            }

                                            Button(
                                                onClick = { save() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "save()"
                                                )
                                            }

                                            Button(
                                                onClick = { loadSettings() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "loadSettings()"
                                                )
                                            }

                                            Button(
                                                onClick = { saveSettings() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "loadSettings()"
                                                )
                                            }

                                            Button(
                                                onClick = { Utils.shutDownApp() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Utils.shutDownApp()"
                                                )
                                            }

                                            Button(
                                                onClick = { Utils.restartApp() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Utils.restartApp()"
                                                )
                                            }

                                            Button(
                                                onClick = { Utils.initializeData() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Utils.initializeData()"
                                                )
                                            }

                                            Button(
                                                onClick = { testDialog = true },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondary,
                                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                                ),
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "테스트 다이얼로그" // TODO
                                                )
                                            }
                                            
                                        }

                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .height(56.dp)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
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
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isExpanded && !isAttached,
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
                                onClick = { isAttached = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowLeft,
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


    @Composable
    fun TestDialog() {
        MyView.BaseDialog(
            onDismissRequest = { testDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null // TODO
                )
            },
            title = {
                Text(
                    text = "테스트 다이얼로그" // TODO
                )
            },
            content = {
                Text(
                    text = "Shadow",
                    style = MaterialTheme.typography.displayLarge.copy(
                        shadow = Shadow(
                            color = LocalContentColor.current,
                            blurRadius = 50f
                        )
                    )
                )
            },
            button = {
                MyView.DialogButtonRow {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "신기한 일") // TODO
                    }
                    TextButton(onClick = { testDialog = false }) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            }
        )
    }
    
}