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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Utils
import com.taeyeon.dongdae.MyView.ChatUnit
import com.taeyeon.dongdae.ui.theme.Theme

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
                title = "설정", // TODO
                unitList = listOf(
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
                        var selected by rememberSaveable { mutableStateOf(0) }
                        val list = listOf("하나", "둘", "셋")

                        Unit.DropDownUnit(
                            title = "DropDownUnit",
                            selected = selected,
                            onSelected = { selected_, _ ->
                                selected = selected_
                            },
                            list = list
                        )
                    },
                    {

                    },
                    {
                        Unit.SwitchUnit(
                            title = "SwitchUnit",
                            checked = Tester.tester,
                            onCheckedChange = { checked -> Tester.tester = checked })
                    },
                    {
                        var value by rememberSaveable { mutableStateOf(5f) }

                        Unit.SliderUnit(
                            title = "SliderUnit",
                            value = value,
                            onValueChange = { value_ -> value = value_ },
                            valueRange = 0f..10f
                        )
                    },
                    {
                        var value by rememberSaveable { mutableStateOf(0) }

                        Unit.ThemeSelectUnit(
                            selected = value,
                            onSelected = { selected ->
                                value = selected
                            },
                            themeDataMap = mapOf(
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = false,
                                        content = content
                                    )
                                } to "라이트 모드",
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = true,
                                        content = content
                                    )
                                } to "다크 모드",
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = isSystemInDarkTheme(),
                                        content = content
                                    )
                                } to "시스템 설정"
                            )
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
                                    val dividerColor =
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
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
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(
                                                    10f,
                                                    10f
                                                )
                                            )
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
        fun DropDownUnit(
            title: String,
            selected: Int,
            onSelected: (selected: Int, item: String) -> kotlin.Unit,
            list: List<String>
        ) { // TODO
            var isDropDownMenuExpanded by remember { mutableStateOf(false) }
            var unitWidth by remember { mutableStateOf(0) }

            TextButton(
                onClick = { isDropDownMenuExpanded = !isDropDownMenuExpanded },
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .onSizeChanged { intSize ->
                        unitWidth = intSize.width
                    }
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

                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = list[selected],
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null // TODO
                        )
                    }

                    DropdownMenu(
                        expanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false },
                        modifier = Modifier.width(with(LocalDensity.current) { unitWidth.toDp() }),
                        offset = DpOffset(x = (-12).dp, y = 8.dp)
                    ) {
                        list.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    onSelected(index, item)
                                    isDropDownMenuExpanded = false
                                }
                            )
                            if (index != list.size - 1) {
                                val dividerColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                ) {
                                    drawLine(
                                        color = dividerColor,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                10f,
                                                10f
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
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

        private val themePreviewMap = mapOf(
            ThemePreview.Palette to "팔레트",
            ThemePreview.Chat to "채팅",
            ThemePreview.Screen to "화면",
        ) // TODO

        @Composable
        fun ThemePreviewUnit(
            theme: @Composable (@Composable () -> kotlin.Unit) -> kotlin.Unit = @Composable { content ->
                Theme(
                    content = content
                )
            },
            themePreview: ThemePreview = ThemePreview.Palette
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .width(150.dp + 3.dp)
                    .height(100.dp + 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.5.dp)
                ) {
                    theme {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            //
                            CompositionLocalProvider(LocalDensity provides Density(density = LocalDensity.current.density / 2f)) {
                                AnimatedContent(
                                    targetState = themePreview,
                                    modifier = Modifier.fillMaxSize()
                                ) { themePreview ->
                                    when (themePreview) {

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
                                                                .align(Alignment.Center)
                                                                .background(
                                                                    color = MaterialTheme.colorScheme.primary,
                                                                    shape = RoundedCornerShape(percent = 20)
                                                                )
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
                                                            )
                                                            .padding(10.dp),
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
                                                                .background(
                                                                    color = MaterialTheme.colorScheme.onSurface,
                                                                    shape = RoundedCornerShape(percent = 20)
                                                                )
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
        }

        @Composable
        fun ThemeSelectUnit(
            selected: Int,
            onSelected: (selected: Int) -> kotlin.Unit,
            themeDataMap: Map<@Composable (@Composable () -> kotlin.Unit) -> kotlin.Unit, String>
        ) {
            var themePreview by rememberSaveable { mutableStateOf(ThemePreview.Palette) }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "테마",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(9.dp),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ThemePreview.values().forEach { themePreview_ ->
                                Surface(
                                    color = Color.Transparent,
                                    shape = MaterialTheme.shapes.medium,
                                    onClick = { themePreview = themePreview_ }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        RadioButton(
                                            selected = themePreview == themePreview_,
                                            onClick = null
                                        )
                                        Text(
                                            text = themePreviewMap[themePreview_]!!,
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(
                                state = rememberScrollState()
                            ),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        themeDataMap.entries.forEachIndexed { index, pair ->
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.medium,
                                onClick = { onSelected(index) }
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ThemePreviewUnit(
                                        theme = pair.key,
                                        themePreview = themePreview
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        RadioButton(
                                            selected = selected == index,
                                            onClick = null
                                        )
                                        Text(
                                            text = pair.value,
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }

    }

}