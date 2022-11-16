@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.taeyeon.core.Settings
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
                        var boxWidth by remember { mutableStateOf(0) }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .onSizeChanged { intSize ->
                                    boxWidth = intSize.width
                                }
                        ) {
                            var helpPopup by rememberSaveable {
                                mutableStateOf(
                                    false
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Surface(
                                    color = uniqueColor,
                                    shape = CircleShape,
                                    border = BorderStroke(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .size(120.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .align(Alignment.Center)
                                                .background(
                                                    color = Color(
                                                        red = 1 - uniqueColor.red,
                                                        green = 1 - uniqueColor.green,
                                                        blue = 1 - uniqueColor.blue,
                                                        alpha = 0.2f
                                                    ),
                                                    shape = CircleShape
                                                )
                                        )

                                        Box(
                                            modifier = Modifier
                                                .width(60.dp)
                                                .height(30.dp)
                                                .align(Alignment.BottomCenter)
                                                .background(
                                                    color = Color(
                                                        red = 1 - uniqueColor.red,
                                                        green = 1 - uniqueColor.green,
                                                        blue = 1 - uniqueColor.blue,
                                                        alpha = 0.2f
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        topStartPercent = 75,
                                                        topEndPercent = 75,
                                                        bottomStartPercent = 0,
                                                        bottomEndPercent = 0
                                                    )
                                                )
                                        )

                                    }
                                }

                                Text(
                                    text = name,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    text = "($subName)",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    style = MaterialTheme.typography.titleMedium
                                )

                            }

                            IconButton(
                                onClick = { helpPopup = !helpPopup },
                                modifier = Modifier
                                    .size(10.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.QuestionMark,
                                    contentDescription = null, // TODO
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (helpPopup) {
                                Popup(
                                    alignment = Alignment.TopEnd,
                                    offset = IntOffset(x = with(LocalDensity.current) { (-10).dp.toPx().toInt() }, y = 0)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .sizeIn(maxWidth = with(LocalDensity.current) { boxWidth.toDp() })
                                            .background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = """
                                                아이디: 안드로이드의 고유 식별자입니다. 일부 상황에서 달라질 수 있습니다.
                                                나만의 색: 아이디의 마지막 6글자를 HEX 컬러화한 값입니다.
                                                이름: 아이디의 일부를 숫자로 변환해 그 순서에 맞는 멸조위기종 이름 중 하나를 가집니다.
                                                서브이름: 아이디의 마지막 6글자입니다.
                                            """.trimIndent(), // TODO
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .sizeIn(maxWidth = with(LocalDensity.current) { boxWidth.toDp() - 16.dp - 15.dp - 10.dp }) // 16.dp: Row Shape Padding, 15.dp: IconButton Size, 10.dp: Offset
                                        )

                                        IconButton(
                                            onClick = { helpPopup = false },
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                                .size(15.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = null, // TODO
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    },
                    {
                        Unit.DoubleTextUnit(
                            title = "아이디",
                            subTitle = id,
                            copyText = id,
                            isJumpLine = true
                        )
                    }
                )
            ),
            Unit.BlockData(
                title = "기능", // TODO
                unitList = listOf {
                    Unit.TextButtonUnit(
                        title = "환영 인사 보기" // TODO
                    ) {
                        screen = Screen.Welcome
                    }
                }
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
                unitList = arrayListOf<@Composable ColumnScope.() -> kotlin.Unit>(
                    {
                        Unit.ThemeSelectUnit(
                            title = "다크 모드", // TODO
                            selected = Settings.DarkMode.values().indexOf(darkMode),
                            onSelected = { selected ->
                                darkMode = Settings.DarkMode.values()[selected]
                                save()
                            },
                            themeDataMap = mapOf(
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = isSystemInDarkTheme(),
                                        content = content
                                    )
                                } to "시스템 설정", // TODO
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = false,
                                        content = content
                                    )
                                } to "라이트 모드", // TODO
                                @Composable { content: @Composable () -> kotlin.Unit ->
                                    Theme(
                                        darkTheme = true,
                                        content = content
                                    )
                                } to "다크 모드" // TODO
                            )
                        )
                    },
                    {
                        Text("adsf")
                    }
                ).also {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
                        it.add(index = 1) {
                            Unit.ThemeSelectUnit(
                                title = "컬러", // TODO
                                selected = if (dynamicColor) 1 else 0,
                                onSelected = { selected ->
                                    dynamicColor = selected == 1
                                    save()
                                },
                                themeDataMap = mapOf(
                                    @Composable { content: @Composable () -> kotlin.Unit ->
                                        Theme(
                                            dynamicColor = false,
                                            content = content
                                        )
                                    } to "기본 테마", // TODO
                                    @Composable { content: @Composable () -> kotlin.Unit ->
                                        Theme(
                                            dynamicColor = true,
                                            content = content
                                        )
                                    } to "다이나믹 컬러" // TODO
                                )
                            )
                        }
                    }
                }.toList()
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
                        Unit.DoubleTextUnit(
                            title = "DoubleTextUnit",
                            subTitle = "isJumpLine = true",
                            copyText = "DoubleTextUnit (isJumpLine = true)",
                            isJumpLine = true
                        )
                    },
                    {
                        Unit.DoubleTextUnit(
                            title = "DoubleTextUnit",
                            subTitle = "isJumpLine = false",
                            copyText = "DoubleTextUnit (isJumpLine = false)",
                            isJumpLine = false
                        )
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
                        var value by rememberSaveable { mutableStateOf("") }

                        Unit.TextFieldUnit(
                            title = "TextFieldUnit (isJumpLine = true)",
                            value = value,
                            onValueChange = { value_ ->
                                value = value_
                            },
                            isJumpLine = true
                        )
                    },
                    {
                        var value by rememberSaveable { mutableStateOf("") }

                        Unit.TextFieldUnit(
                            title = "TextFieldUnit (isJumpLine = false)",
                            value = value,
                            onValueChange = { value_ ->
                                value = value_
                            },
                            isJumpLine = false
                        )
                    },
                    {
                        Unit.SwitchUnit(
                            title = "SwitchUnit",
                            checked = Tester.tester,
                            onCheckedChange = { checked -> Tester.tester = checked })
                    },
                    {
                        var selected by rememberSaveable { mutableStateOf(0) }
                        val list = listOf("하나", "둘", "셋")

                        Unit.RadioUnit(
                            title = "RadioUnit",
                            selected = selected,
                            onSelected = { selected_, _ ->
                                selected = selected_
                            },
                            list = list
                        )
                    },
                    {
                        val checked = remember { mutableStateListOf<Int>() }
                        val list = listOf("하나", "둘", "셋")

                        Unit.CheckBoxUnit(
                            title = "CheckBoxUnit",
                            checked = checked.toSet(),
                            onChecked = { checked_, _ ->
                                if (checked.contains(checked_))
                                    checked.remove(checked_)
                                else
                                    checked.add(checked_)
                            },
                            list = list
                        )
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
                            title = "ThemeSelectUnit", // TODO
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
                title = "개발자 기능", // TODO
                unitList = listOf {
                    Unit.SwitchUnit(
                        title = "테스터", // TODO
                        checked = Tester.tester,
                        onCheckedChange = { checked ->
                            Tester.tester = checked // TODO
                            save()
                        }
                    )
                }
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
                                    DottedLineBorder()
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        fun DottedLineBorder() {
            val dividerColor =
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.5.dp)
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
        fun DoubleTextUnit(
            title: String,
            subTitle: String,
            copyText: String? = null,
            isJumpLine: Boolean = false
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
                    .let {
                        if (copyText == null) {
                            it
                        } else {
                            it.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        Utils.copy(text = copyText)
                                    }
                                )
                            }
                        }
                    }
            ) {
                var titleTextWidth by remember { mutableStateOf(0) }

                Column(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .onSizeChanged { intSize ->
                                titleTextWidth = intSize.width
                            }
                    )
                    if (isJumpLine) {
                        Text(
                            text = subTitle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                if (!isJumpLine) {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(start = with(LocalDensity.current) { titleTextWidth.toDp() + 8.dp })
                    )
                }

                copyText?.let {
                    IconButton(
                        onClick = { Utils.copy(text = copyText) },
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CopyAll,
                            contentDescription = null, // TODO
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
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
        fun TextFieldUnit(
            title: String,
            value: String,
            onValueChange: (value: String) -> kotlin.Unit,
            isJumpLine: Boolean = false
        ) {
            val focusRequester by remember { mutableStateOf(FocusRequester()) }
            val focusManager = LocalFocusManager.current
            var isFocused by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    onClick = {
                        if (isFocused)
                            focusManager.clearFocus()
                        else
                            focusRequester.requestFocus()
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var titleTextWidth by remember { mutableStateOf(0) }

                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .onSizeChanged { intSize ->
                                    titleTextWidth = intSize.width
                                }
                        )

                        if (!isJumpLine) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = with(LocalDensity.current) { titleTextWidth.toDp() + 8.dp })
                                    .align(Alignment.CenterEnd)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BasicTextField(
                                    value = value,
                                    onValueChange = onValueChange,
                                    textStyle = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                    maxLines = 1,
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .onFocusChanged {
                                            isFocused = it.isFocused
                                        }
                                )
                            }
                        }

                    }
                }

                if (isJumpLine) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 12.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            textStyle = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            maxLines = 1,
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    isFocused = it.isFocused
                                }
                        )
                    }
                }

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
        ) {
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
        fun RadioUnit(
            title: String,
            selected: Int,
            onSelected: (selected: Int, item: String) -> kotlin.Unit,
            list: List<String>
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(
                        start = 12.dp - getCornerSize(shape = MaterialTheme.shapes.medium),
                        end = 12.dp
                    )
            ) {

                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                    onClick = { onSelected(if (selected + 1 >= list.size) 0 else selected + 1, list[if (selected + 1 >= list.size) 0 else selected + 1]) },
                    modifier = Modifier // TODO
                        .width(IntrinsicSize.Min)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(9.dp),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        list.forEachIndexed { index, item ->
                            Surface(
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.medium,
                                onClick = { onSelected(index, item) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    RadioButton(
                                        selected = selected == index,
                                        onClick = null
                                    )
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

        @Composable
        fun CheckBoxUnit(
            title: String,
            checked: Set<Int>,
            onChecked: (checked: Int, item: String) -> kotlin.Unit,
            list: List<String>
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = title,
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
                        list.forEachIndexed { index, item ->
                            Surface(
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.medium,
                                onClick = { onChecked(index, item) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Checkbox(
                                        checked = checked.contains(index),
                                        onCheckedChange = null
                                    )
                                    Text(
                                        text = item,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelSmall
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
                                                                    shape = RoundedCornerShape(
                                                                        percent = 20
                                                                    )
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
                                                                    shape = RoundedCornerShape(
                                                                        percent = 20
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
                    }
                }
            }
        }

        @Composable
        fun ThemeSelectUnit(
            title: String,
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
                RadioUnit(
                    title = title, // TODO
                    selected = themePreviewMap.keys.indexOf(themePreview),
                    onSelected = { index, _ ->
                        themePreview = themePreviewMap.keys.toList()[index]
                    },
                    list = themePreviewMap.values.toList()
                )
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