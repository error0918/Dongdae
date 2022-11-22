@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Power
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.taeyeon.core.*
import com.taeyeon.dongdae.MyView.ChatUnit
import com.taeyeon.dongdae.data.ChatSequence
import com.taeyeon.dongdae.ui.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


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
        var isLoading by rememberSaveable { mutableStateOf(false) }
        var isRestartDialog by rememberSaveable { mutableStateOf(false) }
        var isInitializingDialog by rememberSaveable { mutableStateOf(false) }

        if (isLoading) {
            MyView.LoadingPopup()
        }

        if (isRestartDialog) {
            MyView.MessageDialog(
                onDismissRequest = { isRestartDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Power,
                        contentDescription = "재시작" // TODO
                    )
                },
                title = { Text(text = "재시작") }, // TODO
                text = {
                    Text(
                        text = "정상적인 작동을 위해서는 재시작이 필요합니다.\n" +
                                "재시작하시겠습니까?"
                    )
                },
                dismissButton = {
                    TextButton(onClick = { isRestartDialog = false }) {
                        Text(text = "닫기") // TODO
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            Main.scope.launch {
                                isRestartDialog = false
                                isLoading = true
                                SharedPreferencesManager.Companion.Public.putBoolean("isRestarted", true)
                                delay(500)
                                isLoading = false
                                Utils.restartApp()
                            }
                        }
                    ) {
                        Text(text = "재시작") // TODO
                    }
                }
            )
        }

        if (isInitializingDialog) {
            MyView.MessageDialog(
                onDismissRequest = { isRestartDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "초기화" // TODO
                    )
                },
                title = { Text(text = "초기화") }, // TODO
                text = {
                    Text(
                        text = "앱의 모든 정보를 초기화시키겠습니까?"
                    )
                },
                dismissButton = {
                    TextButton(onClick = { isInitializingDialog = false }) {
                        Text(text = "닫기") // TODO
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            Utils.initializeData()
                            isInitializingDialog = false
                            isRestartDialog = true
                        }
                    ) {
                        Text(text = "초기화") // TODO
                    }
                }
            )
        }

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
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    Utils.copy(text = "#${subName.uppercase()}")
                                                }
                                            )
                                        }
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
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    Utils.copy(text = name)
                                                }
                                            )
                                        }
                                )

                                Text(
                                    text = "($subName)",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    Utils.copy(text = subName)
                                                }
                                            )
                                        }
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
                        Unit.DropDownUnit(
                            title = "기본 탭", // TODO
                            selected = defaultTab,
                            onSelected = { selected, _ ->
                                defaultTab = selected
                                save()
                            },
                            list = listOf("알림", "채팅", "커뮤니티", "프로필") // TODO
                        ) // TODO
                    },
                    {
                        Unit.TextFieldUnit(
                            title = "기본 비밀번호", // TODO
                            value = defaultPassword,
                            onValueChange = { value ->
                                if (value.length <= 4)
                                    defaultPassword = value.replace(".", "")
                                        .replace("-", "")
                                save()
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { defaultPassword = getDigitNumber(Random.nextInt(10000), 4) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null // TODO
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            textStyle = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            textFiledAlignment = Alignment.Center
                        )
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
                        Unit.SwitchUnit(
                            title = "전체화면", // TODO
                            checked = fullScreenMode,
                            onCheckedChange = { checked ->
                                fullScreenMode = checked
                                Settings.applyFullScreenMode()
                                save()
                                isRestartDialog = true
                            }
                        )
                    },
                    {
                        Unit.SwitchUnit(
                            title = "화면 항상 켜짐", // TODO
                            checked = screenAlwaysOn,
                            onCheckedChange = { checked ->
                                screenAlwaysOn = checked
                                Settings.applyScreenAlwaysOn()
                                save()
                                isRestartDialog = true
                            }
                        )
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
                        } // TODO: 다양한 컬러 팔레트 지원 (구현 시 안드로이드도 12 이하도 지원으로 변경)
                    }
                }.toList()
            ),
            Unit.BlockData(
                title = "문제 해결", // TODO
                unitList = listOf(
                    {
                        Unit.TextButtonUnit(
                            title = "앱 재시작" // TODO
                        ) { isRestartDialog = true }
                    },
                    {
                        Unit.TextButtonUnit(
                            title = "앱 초기화" // TODO
                        ) { isInitializingDialog = true }
                    }
                )
            ),
            Unit.BlockData(
                title = "개발자 기능", // TODO
                unitList = listOf {
                    Unit.SwitchUnit(
                        title = "테스터", // TODO
                        checked = tester,
                        onCheckedChange = { checked ->
                            tester = checked // TODO
                            save()
                        }
                    )
                }
            ),
            Unit.BlockData(
                title = "앱에 관해서", // TODO
                unitList = listOf(
                    {
                        Unit.TextButtonUnit(
                            title = "시스템 설정" // TODO
                        ) {
                            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + Info.getPackage()))
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            Core.getActivity().startActivity(intent)
                        }
                    },
                    {
                        Unit.TextButtonUnit(
                            title = "깃허브" // TODO
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/error0918/Dongdae"))
                            Core.getActivity().startActivity(intent)
                        }
                    },
                    {
                        Unit.TextButtonUnit(
                            title = "메일 문의" // TODO
                        ) {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "plain/text"
                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("developer.taeyeon@gmail.com"))
                            intent.putExtra(Intent.EXTRA_SUBJECT, "동대 애플리케이션 관련 문의") // TODO
                            intent.putExtra(Intent.EXTRA_TEXT, "동대에 관한 문의") // TODO
                            Core.getActivity().startActivity(intent)
                        }
                    },
                    {
                        Unit.TextButtonUnit(
                            title = "라이선스" // TODO
                        ) {
                            Main.scope.launch {
                                Main.sheetIndex = 1
                                if (Main.bottomSheetScaffoldState.bottomSheetState.isExpanded)
                                    Main.bottomSheetScaffoldState.bottomSheetState.collapse()
                                else if (Main.bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                                    Main.bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    },
                    {
                        Unit.TextButtonUnit(
                            title = "앱 정보" // TODO
                        ) {
                            Main.scope.launch {
                                Main.sheetIndex = 2
                                if (Main.bottomSheetScaffoldState.bottomSheetState.isExpanded)
                                    Main.bottomSheetScaffoldState.bottomSheetState.collapse()
                                else if (Main.bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                                    Main.bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
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
            title: String,
            copyText: String? = null
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

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                copyText?.let {
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
            isJumpLine: Boolean = false,
            textStyle: TextStyle = MaterialTheme.typography.labelSmall,
            leadingIcon: @Composable() (() -> kotlin.Unit)? = MyView.MyTextFieldDefaults.leadingIcon,
            trailingIcon: @Composable() (() -> kotlin.Unit)? = MyView.MyTextFieldDefaults.trailingIcon,
            keyboardOptions: KeyboardOptions = MyView.MyTextFieldDefaults.keyboardOptions,
            keyboardActions: KeyboardActions = MyView.MyTextFieldDefaults.keyboardActions,
            singleLine: Boolean = MyView.MyTextFieldDefaults.singleLine,
            maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
            interactionSource: MutableInteractionSource = MyView.MyTextFieldDefaults.interactionSource,
            textFiledAlignment: Alignment = Alignment.Center
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
                            MyView.MyTextField(
                                value = value,
                                onValueChange = onValueChange,
                                textStyle = textStyle,
                                leadingIcon = leadingIcon,
                                trailingIcon = trailingIcon,
                                keyboardOptions = keyboardOptions,
                                keyboardActions = keyboardActions,
                                singleLine = singleLine,
                                maxLines = maxLines,
                                interactionSource = interactionSource,
                                textFiledAlignment = textFiledAlignment,
                                modifier = Modifier
                                    .width(200.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterEnd)
                                    .padding(start = with(LocalDensity.current) { titleTextWidth.toDp() + 8.dp })
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        isFocused = it.isFocused
                                    }
                            )
                        }

                    }
                }

                if (isJumpLine) {
                    MyView.MyTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = textStyle,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        singleLine = singleLine,
                        maxLines = maxLines,
                        interactionSource = interactionSource,
                        textFiledAlignment = textFiledAlignment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 12.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocused = it.isFocused
                            }
                    )
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
                                },
                                modifier = Modifier
                                    .let {
                                        if (index == selected)
                                            it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                        else
                                            it
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
                    modifier = Modifier // TOD
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                ) {
                    Box(
                        modifier = Modifier
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
                    .width(120.dp + 3.dp)
                    .height(120.dp + 3.dp)
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
                                                            chatSequence = ChatSequence.Default
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
                                                            chatSequence = ChatSequence.Default
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
                                                            chatSequence = ChatSequence.Default
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
                                                            chatSequence = ChatSequence.Sequence
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
                modifier = Modifier.fillMaxWidth(),
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
                                            style = MaterialTheme.typography.labelSmall
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