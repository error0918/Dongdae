@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Utils
import com.taeyeon.dongdae.MyView.ChatUnit

object Profile {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "프로필", // TODO
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person,
        lazyListState = lazyListState,
        fab = null,
        composable = { Profile() }
    )

    @Composable
    fun Profile() {
        val blockList = listOf(
            Unit.BlockData(
                title = "프로필", // TODO
                unitList = listOf(
                    {
                        Text("adsf")
                    },
                    {
                        Text("adsf")
                    }
                )
            ),
            Unit.BlockData(
                title = "일반 설정", // TODO
                unitList = listOf(
                    {
                        Text("adsf")
                    },
                    {
                        Text("adsf")
                    }
                )
            ),
            Unit.BlockData(
                title = "테마 설정", // TODO
                unitList = listOf(
                    {
                        Text("adsf")
                    },
                    {
                        Text("adsf")
                    }
                )
            ),
            Unit.BlockData(
                title = "문제 해결", // TODO
                unitList = listOf(
                    {
                        Text("adsf")
                    },
                    {
                        Text("adsf")
                    }
                )
            ),
            Unit.BlockData(
                title = "개발자 기능", // TODO
                unitList = listOf(
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(
                                    state = rememberScrollState()
                                ),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Unit.ThemePreview.values().forEach { themePreview ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Unit.ThemePreviewUnit(
                                    themePreview = themePreview
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    },
                    {
                        Unit.TextUnit(title = "TextUnit")
                    },
                    {
                        Unit.CopyableTextUnit(title = "CopyableTextUnit")
                    },
                    {
                        Unit.TextButtonUnit(title = "TextButtonUnit") { // TODO
                            // TODO
                        }
                    },
                    {
                        Unit.SwitchUnit(title = "SwitchUnit", checked = Tester.tester, onCheckedChange =  { checked -> Tester.tester = checked })
                    },
                    {
                        var value by rememberSaveable { mutableStateOf(5f) }

                        Unit.SliderUnit(
                            title = "SliderUnit",
                            value = value,
                            onValueChange = { value_ -> value = value_},
                            valueRange = 0f .. 10f
                        )
                    }
                )
            ),
            Unit.BlockData(
                title = "앱에 관해서", // TODO
                unitList = listOf(
                    {
                        Text("adsf")
                    },
                    {
                        Text("adsf")
                    }
                )
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(blockList) { blockData ->
                Unit.Block(blockData)
            }
        }
    }

    object Unit {

        data class BlockData(
            val title: String,
            val unitList: List<@Composable ColumnScope.() -> kotlin.Unit>
        )

        @Composable
        fun Block(
            blockData: BlockData
        ) {
            blockData.run {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = getCornerSize(shape = MaterialTheme.shapes.medium))
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 6.dp)
                        ) {
                            unitList.forEachIndexed { index, unit ->
                                unit()
                                if (index != unitList.size - 1) {
                                    val dividerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.5.dp)
                                            .height(1.dp)
                                    ) {
                                        drawLine(
                                            color = dividerColor,
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, 0f),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        fun TextUnit(
            title: String
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }

        @Composable
        fun CopyableTextUnit(
            title: String,
            copyText: String = title
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                Utils.copy(text = copyText)
                            }
                        )
                    }
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                IconButton(
                    onClick = {
                        Utils.copy(text = copyText)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CopyAll,
                        contentDescription = null // TODO
                    )
                }

            }
        }

        @Composable
        fun TextButtonUnit(
            title: String,
            onClick: () -> kotlin.Unit
        ) {
            TextButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        @Composable
        fun SwitchUnit(
            title: String,
            checked: Boolean,
            onCheckedChange: (checked: Boolean) -> kotlin.Unit
        ) {
            TextButton(
                onClick = { onCheckedChange(!checked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )

                    Switch(
                        checked = checked,
                        onCheckedChange = { onCheckedChange(!checked) },
                        modifier = Modifier
                            .height(16.88888888888889.dp)
                            .align(Alignment.CenterEnd)
                    )
                }

            }
        }

        @Composable
        fun SliderUnit(
            title: String,
            value: Float,
            onValueChange: (Float) -> kotlin.Unit,
            valueRange: ClosedFloatingPointRange<Float> = MyView.FullBackgroundSliderDefaults.valueRange,
            steps: Int = MyView.FullBackgroundSliderDefaults.steps,
            isShowingPopup: Boolean = MyView.FullBackgroundSliderDefaults.isShowingPopup,
            roundingDigits: Int = MyView.FullBackgroundSliderDefaults.roundingDigits
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(vertical = 6.dp)
                            .fillMaxHeight()
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(
                                horizontal = 14.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%.${roundingDigits}f", value),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                MyView.FullBackgroundSlider(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    valueRange = valueRange,
                    steps = steps,
                    isShowingPopup = isShowingPopup,
                    roundingDigits = roundingDigits
                )

            }
        }

        enum class ThemePreview {
            Palette, Chat, Screen
        }

        val themePreviewMap = mapOf(
            ThemePreview.Palette to "팔레트",
            ThemePreview.Chat to "체팅",
            ThemePreview.Screen to "화면",
        ) // TODO

        @Composable
        fun ThemePreviewUnit(
            themePreview: ThemePreview = ThemePreview.Palette
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
            ) {
                CompositionLocalProvider(LocalDensity provides Density(density = LocalDensity.current.density / 2f)) {
                    AnimatedContent(
                        targetState = themePreview,
                        modifier = Modifier.fillMaxSize()
                    ) { themePreview ->
                        when(themePreview) {

                            ThemePreview.Palette -> {
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary,
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.surfaceVariant
                                    ).forEach {
                                        Spacer(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .background(it)
                                        )
                                    }
                                }
                            }

                            ThemePreview.Chat -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    userScrollEnabled = false
                                ) {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            this.ChatUnit(
                                                isMe = true,
                                                id = id,
                                                message = "테마 미리보기", // TODO
                                                chatSequence = MyView.ChatSequence.Default
                                            )
                                        }
                                    }
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            this.ChatUnit(
                                                isMe = false,
                                                id = id,
                                                message = "테마 미리보기", // TODO
                                                chatSequence = MyView.ChatSequence.Default
                                            )
                                        }
                                    }
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            this.ChatUnit(
                                                isMe = true,
                                                id = id,
                                                message = "테마 미리보기", // TODO
                                                chatSequence = MyView.ChatSequence.Default
                                            )
                                        }
                                    }
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            this.ChatUnit(
                                                isMe = true,
                                                id = id,
                                                message = "테마 미리보기", // TODO
                                                chatSequence = MyView.ChatSequence.Sequence
                                            )
                                        }
                                    }
                                }
                            }

                            ThemePreview.Screen -> {
                                Scaffold(
                                    topBar = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                                .background(MaterialTheme.colorScheme.surface)
                                                .padding(horizontal = 10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(40.dp)
                                                    .height(10.dp)
                                                    .align(Alignment.CenterStart)
                                                    .background(MaterialTheme.colorScheme.primary)
                                            )
                                        }
                                    },
                                    bottomBar = {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                        3.dp
                                                    )
                                                )
                                        ) {
                                            listOf(
                                                Icons.Filled.Chat,
                                                Icons.Filled.People,
                                                Icons.Filled.Person,
                                            ).forEachIndexed { index, item ->
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxHeight()
                                                        .let {
                                                            if (index == 1) {
                                                                it
                                                                    .padding(
                                                                        vertical = 7.5.dp,
                                                                        horizontal = 20.dp
                                                                    )
                                                                    .background(
                                                                        color = MaterialTheme.colorScheme.secondary,
                                                                        shape = CircleShape
                                                                    )
                                                            } else {
                                                                it.padding(10.dp)
                                                            }
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = item,
                                                        contentDescription = null, // TODO
                                                        tint = if (index == 1) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    floatingActionButton = {
                                        Box(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(40.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.primaryContainer,
                                                    shape = MaterialTheme.shapes.small
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = null, // TODO
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                ) { paddingValues ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(paddingValues = paddingValues)
                                    ) {
                                        listOf(80, 160, 100, 80, 30).forEach { width ->
                                            Box(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .width(width.dp)
                                                    .height(10.dp)
                                                    .background(MaterialTheme.colorScheme.onSurface)
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

    }

}