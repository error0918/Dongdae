@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.taeyeon.core.SharedPreferencesManager
import com.taeyeon.core.Utils
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object Community {
    private var isWritingPost by mutableStateOf(false)

    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "TEST",
        filledIcon = Icons.Filled.People,
        outlinedIcon = Icons.Outlined.PeopleOutline,
        lazyListState = lazyListState,
        fab = { Fab() },
        composable = { Community() }
    )

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Community() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            var subTopBarHeight by remember { mutableStateOf(0) }

            var categoryIndex by rememberSaveable { mutableStateOf(0) }
            var sortingIndex by rememberSaveable { mutableStateOf(0) }

            AnimatedVisibility(visible = !lazyListState.isScrollInProgress || (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Main.toolbarColor)
                        .onSizeChanged {
                            subTopBarHeight = it.height
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    LazyRow(
                        modifier = Modifier
                            .weight(2f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(MyView.postCategoryNameList.size) { index ->
                            val selected = index == categoryIndex
                            FilterChip(
                                selected = selected,
                                onClick = { categoryIndex = index },
                                label = { Text(text = if (index == 0) "전체" else MyView.postCategoryNameList[index]) }, // TODO
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                                        contentDescription = null // TODO
                                    )
                                }
                            )
                        }
                    }

                    val sortingList = listOf("최신순", "좋아요 순", "랜덤")
                    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
                    var buttonSize by remember { mutableStateOf(IntSize.Zero) }

                    OutlinedButton(
                        onClick = { isDropDownMenuExpanded = !isDropDownMenuExpanded },
                        shape = RoundedCornerShape(percent = 20),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .onSizeChanged { intSize ->
                                buttonSize = intSize
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = (sortingList[sortingIndex]),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = null // TODO
                            )
                        }

                        DropdownMenu(
                            expanded = isDropDownMenuExpanded,
                            onDismissRequest = { isDropDownMenuExpanded = false },
                            modifier = Modifier.width(with(LocalDensity.current) { buttonSize.width.toDp() }),
                            offset = DpOffset(x = (-24).dp, y = 8.dp)
                        ) {
                            sortingList.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        sortingIndex = index
                                        isDropDownMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                }

            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = { /*TODO*/ },
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        scale = true,
                        backgroundColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 8.dp + with(LocalDensity.current) { subTopBarHeight.toDp() }
                    )
                ) {

                    item {

                        MyView.PostUnit(
                            id = id,
                            content = "얘들아 오늘 급식 어떤 거 나와? 🤤",
                            commentList = listOf(
                                MyView.ChatData(
                                    isMe = false,
                                    id = id,
                                    message = "오늘은 탕수육 나온대!",
                                    chatSequence = MyView.ChatSequence.Start
                                ),
                                MyView.ChatData(
                                    isMe = true,
                                    id = id,
                                    message = "와 정말? 맛있겠다!!",
                                    chatSequence = MyView.ChatSequence.Start
                                ),
                                MyView.ChatData(
                                    isMe = false,
                                    id = id,
                                    message = "응! 나도 기대하고 있어",
                                    chatSequence = MyView.ChatSequence.Start
                                )
                            )
                        )

                    }

                    item {
                        MyView.PostUnit(
                            id = id,
                            content = "한국사 수행평가 범위 어디야?",
                            commentList = listOf(
                                MyView.ChatData(
                                    isMe = false,
                                    id = id,
                                    message = "아마 우리가 2학기 때 배우는 거 전체일걸?",
                                    chatSequence = MyView.ChatSequence.Start
                                ),
                                MyView.ChatData(
                                    isMe = true,
                                    id = id,
                                    message = "고마워~",
                                    chatSequence = MyView.ChatSequence.Start
                                )
                            )
                        )
                    }

                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 10.dp,
                                pressedElevation = 10.dp,
                                focusedElevation = 10.dp,
                                hoveredElevation = 10.dp,
                                draggedElevation = 10.dp,
                                disabledElevation = 10.dp,
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                            ) {
                                Text(
                                    text = "개시물을 모두 다 보셨습니다. 😄",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }

                }

            }

        }

        Popup(
            alignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                AnimatedVisibility(
                    visible = Main.pagerState.currentPage == 2,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AnimatedVisibility(
                        visible = !lazyListState.isScrollInProgress && (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        FilledIconButton(
                            onClick = {
                                Main.scope.launch {
                                    lazyListState.animateScrollToItem(0)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

        if (isWritingPost) {
            Write.WritePostDialog()
        }
    }

    @SuppressLint("FrequentlyChangedStateReadInComposition")
    @Composable
    fun Fab() {
        ExtendedFloatingActionButton(
            onClick = {
                Utils.vibrate(10)
                isWritingPost = true
            }
        ) {
            animateColorAsState(
                targetValue = if (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "글쓰기"
            )
            AnimatedVisibility(visible = !lazyListState.isScrollInProgress) {
                Text(
                    text = "글쓰기",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    object Write {
        private lateinit var sharedPreferencesManager: SharedPreferencesManager

        private const val TEMPORARY_SAVING_KEY = "TEMPORARY_SAVING"

        private const val timeKey = "time"
        private const val imageKey = "image"
        private const val isSelectableKey = "isSelectable"
        private const val contentKey = "content"
        private const val isHeartAbleKey = "isHeartAble"
        private const val postCategoryKey = "postCategory"
        private const val passwordKey = "password"

        enum class WritingPostPage {
            TemporarySaving, Writing, CheckBeforeUploading, NoticeSuccess, Wait
        }

        @SuppressLint("NewApi")
        @Composable
        fun WritePostDialog() {
            var writingPostPage by rememberSaveable { mutableStateOf(WritingPostPage.Wait) }

            var time by rememberSaveable { mutableStateOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm"))) }
            var image by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }
            var isSelectable by rememberSaveable { mutableStateOf(true) }
            var content by rememberSaveable { mutableStateOf("") }
            var isHeartAble by rememberSaveable { mutableStateOf(true) }
            var postCategory by rememberSaveable { mutableStateOf(MyView.PostCategory.Unspecified) }
            var password by rememberSaveable { mutableStateOf("0000") }

            LaunchedEffect(true) {
                if (!::sharedPreferencesManager.isInitialized) {
                    sharedPreferencesManager = SharedPreferencesManager(TEMPORARY_SAVING_KEY)
                }

                writingPostPage = if (sharedPreferencesManager.contains(timeKey) && sharedPreferencesManager.getString(contentKey).isNotBlank()) {
                    WritingPostPage.TemporarySaving
                } else {
                    WritingPostPage.Writing
                }
            }

            when (writingPostPage) {

                WritingPostPage.Wait -> {
                    Popup(alignment = Alignment.Center) {
                        val primaryColor = MaterialTheme.colorScheme.primary

                        val startAngle by rememberInfiniteTransition().animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            )
                        )
                        val sweepAngle by rememberInfiniteTransition().animateFloat(
                            initialValue = 60f,
                            targetValue = 300f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Canvas(
                            modifier = Modifier
                                .shadow(
                                    elevation = 10.dp,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .size(150.dp)
                                .padding(30.dp)
                        ) {

                            drawArc(
                                color = primaryColor,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                size = Size(90.dp.toPx(), 90.dp.toPx()),
                                style = Stroke(
                                    width = 15.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )

                        }
                    }
                }

                WritingPostPage.TemporarySaving -> {
                    MyView.MessageDialog(
                        onDismissRequest = { isWritingPost = false },
                        modifier = Modifier.padding(16.dp),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.QuestionMark,
                                contentDescription = null // TODO
                            )
                        },
                        title = { Text(text = "임시 저장") }, // TODO
                        text = {
                            Text(
                                text = "${sharedPreferencesManager.getString(timeKey, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")))}에 저장한 글이 있습니다. 불러오시겠습니까?",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }, // TODO
                        button = {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    onClick = { isWritingPost = false }
                                ) {
                                    Text(text = "닫기") // TODO
                                }
                                MyView.DialogButtonRow(
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    TextButton(
                                        onClick = {
                                            time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm"))
                                            image = null
                                            isSelectable = true
                                            content = ""
                                            isHeartAble = true
                                            postCategory = MyView.PostCategory.Unspecified
                                            password = "0000"

                                            writingPostPage = WritingPostPage.Writing
                                        }
                                    ) {
                                        Text(text = "새로 작성하기") // TODO
                                    }
                                    TextButton(
                                        onClick = {
                                            time = sharedPreferencesManager.getString(timeKey, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")))
                                            image = sharedPreferencesManager.getAny<ImageBitmap?>(imageKey, ImageBitmap::class.java, null)
                                            isSelectable = sharedPreferencesManager.getBoolean(isSelectableKey, true)
                                            content = sharedPreferencesManager.getString(contentKey, "")
                                            isHeartAble = sharedPreferencesManager.getBoolean(isHeartAbleKey, true)
                                            postCategory = sharedPreferencesManager.getAny(postCategoryKey, MyView.PostCategory::class.java, MyView.PostCategory.Unspecified)
                                            password = sharedPreferencesManager.getString(passwordKey, "0000")

                                            writingPostPage = WritingPostPage.Writing
                                        }
                                    ) {
                                        Text(text = "불러오기") // TODO
                                    }
                                }
                            }
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    )
                }

                WritingPostPage.Writing -> {
                    MyView.BaseDialog(
                        onDismissRequest = { isWritingPost = false },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null // TODO
                            )
                        },
                        title = { Text(text = "글 작성하기") },
                        content = {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    tonalElevation = 5.dp
                                ) {
                                    var isExpanded by rememberSaveable { mutableStateOf(true) }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        AnimatedVisibility(visible = isExpanded) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(getCornerSize(shape = MaterialTheme.shapes.medium)),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(50.dp)
                                                ) {
                                                    Text(
                                                        text = "내용 복사 가능", // TODO
                                                        style = MaterialTheme.typography.labelLarge,
                                                        modifier = Modifier
                                                            .align(Alignment.CenterStart)
                                                    )
                                                    Switch(
                                                        checked = isSelectable,
                                                        onCheckedChange = { selected ->
                                                            isSelectable = selected
                                                        },
                                                        modifier = Modifier
                                                            .align(Alignment.CenterEnd)
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(50.dp)
                                                ) {
                                                    Text(
                                                        text = "좋아요 기능", // TODO
                                                        style = MaterialTheme.typography.labelLarge,
                                                        modifier = Modifier
                                                            .align(Alignment.CenterStart)
                                                    )
                                                    Switch(
                                                        checked = isHeartAble,
                                                        onCheckedChange = { selected ->
                                                            isHeartAble = selected
                                                        },
                                                        modifier = Modifier
                                                            .align(Alignment.CenterEnd)
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(50.dp)
                                                ) {
                                                    var isDropDownMenuExpanded by remember {
                                                        mutableStateOf(
                                                            false
                                                        )
                                                    }

                                                    Text(
                                                        text = "카테고리", // TODO
                                                        style = MaterialTheme.typography.labelLarge,
                                                        modifier = Modifier
                                                            .align(Alignment.CenterStart)
                                                    )

                                                    OutlinedButton(
                                                        onClick = {
                                                            isDropDownMenuExpanded =
                                                                !isDropDownMenuExpanded
                                                        },
                                                        shape = RoundedCornerShape(percent = 20),
                                                        contentPadding = PaddingValues(),
                                                        modifier = Modifier
                                                            .width(100.dp)
                                                            .align(Alignment.CenterEnd)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(horizontal = 8.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = (MyView.postCategoryNameList[MyView.PostCategory.values()
                                                                    .indexOf(postCategory)]),
                                                                textAlign = TextAlign.Center,
                                                                maxLines = 1,
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Filled.KeyboardArrowDown,
                                                                contentDescription = null // TODO
                                                            )
                                                        }

                                                        DropdownMenu(
                                                            expanded = isDropDownMenuExpanded,
                                                            onDismissRequest = {
                                                                isDropDownMenuExpanded = false
                                                            },
                                                            modifier = Modifier.width(100.dp)
                                                        ) {
                                                            MyView.postCategoryNameList.forEachIndexed { index, item ->
                                                                DropdownMenuItem(
                                                                    text = { Text(text = item) },
                                                                    onClick = {
                                                                        postCategory =
                                                                            MyView.PostCategory.values()[index]
                                                                        isDropDownMenuExpanded =
                                                                            false
                                                                    }
                                                                )
                                                            }
                                                        }
                                                    }

                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(50.dp)
                                                ) {
                                                    var helpPopup by rememberSaveable {
                                                        mutableStateOf(
                                                            false
                                                        )
                                                    }

                                                    Row(
                                                        modifier = Modifier
                                                            .align(Alignment.CenterStart)
                                                    ) {
                                                        Text(
                                                            text = "비밀번호", // TODO
                                                            style = MaterialTheme.typography.labelLarge
                                                        )
                                                        IconButton(
                                                            onClick = { helpPopup = !helpPopup },
                                                            modifier = Modifier
                                                                .padding(start = 8.dp)
                                                                .size(10.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.QuestionMark,
                                                                contentDescription = null // TODO
                                                            )
                                                        }
                                                    }

                                                    if (helpPopup) {
                                                        Popup() {
                                                            Row(
                                                                modifier = Modifier
                                                                    .background(
                                                                        color = MaterialTheme.colorScheme.primaryContainer,
                                                                        shape = RoundedCornerShape(8.dp)
                                                                    )
                                                                    .padding(8.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(
                                                                    text = "글 수정, 글 삭제 등에 필요합니다.", // TODO
                                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
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

                                                    OutlinedTextField(
                                                        value = password,
                                                        onValueChange = { value ->
                                                            if (value.length <= 4)
                                                                password = value.replace(".", "")
                                                                    .replace("-", "")
                                                        },
                                                        trailingIcon = {
                                                            IconButton(
                                                                onClick = { password = getDigitNumber(Random.nextInt(10000), 4) }
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
                                                        textStyle = MaterialTheme.typography.labelMedium.copy(
                                                            textAlign = TextAlign.Center
                                                        ),
                                                        maxLines = 1,
                                                        modifier = Modifier
                                                            .width(100.dp)
                                                            .align(Alignment.CenterEnd)
                                                    )

                                                }

                                            }

                                        }

                                        Surface(
                                            modifier =  Modifier.fillMaxWidth(),
                                            shape = MaterialTheme.shapes.medium,
                                            color =  Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.primary,
                                            onClick = {
                                                isExpanded = !isExpanded
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                                contentDescription = null, // TODO
                                                modifier = Modifier.padding(getCornerSize(MaterialTheme.shapes.medium))
                                            )
                                        }

                                    }

                                }

                                var isImageDialog by rememberSaveable { mutableStateOf(false) }

                                if (!isImageDialog) {
                                    /*MyView.ListDialog(
                                        onDismissRequest = { isImageDialog = false },
                                        items = ,
                                        onItemClick =
                                    )*/
                                }

                                OutlinedButton(
                                    onClick = { isImageDialog = !isImageDialog },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "이미지: 업로드되지 않음") // TODO
                                }

                                OutlinedTextField(
                                    value = content,
                                    onValueChange = { value -> content = value },
                                    label = { Text(text = "내용") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )

                                Text(
                                    text = "$time: 임시 저장되었습니다.",
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )

                            }
                        },
                        button = {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    onClick = { isWritingPost = false }
                                ) {
                                    Text(text = "닫기") // TODO
                                }
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    onClick = { /* TODO */ }
                                ) {
                                    Text(text = "게시하기") // TODO
                                }
                            }
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    )

                    LaunchedEffect(image, isSelectable, content, isHeartAble, postCategory, password) {
                        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm"))

                        sharedPreferencesManager.putString(timeKey, time)
                        sharedPreferencesManager.putAny(imageKey, image ?: Any())
                        sharedPreferencesManager.putBoolean(isSelectableKey, isSelectable)
                        sharedPreferencesManager.putString(contentKey, content)
                        sharedPreferencesManager.putBoolean(isHeartAbleKey, isHeartAble)
                        sharedPreferencesManager.putAny(postCategoryKey, postCategory)
                        sharedPreferencesManager.putString(passwordKey, password)
                    }
                }

                WritingPostPage.CheckBeforeUploading -> {
                    MyView.MessageDialog(
                        onDismissRequest = { isWritingPost = false },
                        modifier = Modifier.padding(16.dp),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null // TODO
                            )
                        },
                        title = { Text(text = "업로드") }, // TODO
                        text = {
                            Text(
                                text = "TODO",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }, // TODO
                        button = {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    onClick = { isWritingPost = false }
                                ) {
                                    Text(text = "취소하기") // TODO
                                }
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    onClick = {
                                        /* TODO */
                                    }
                                ) {
                                    Text(text = "업로드하기") // TODO
                                }
                            }
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    )
                }

                WritingPostPage.NoticeSuccess -> {
                    MyView.MessageDialog(
                        onDismissRequest = { isWritingPost = false },
                        modifier = Modifier.padding(16.dp),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null // TODO
                            )
                        },
                        title = { Text(text = "업로드 성공") }, // TODO
                        button = {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    onClick = { isWritingPost = false }
                                ) {
                                    Text(text = "닫기") // TODO
                                }
                            }
                        }
                    )
                }

            }
        }

        /*suspend fun writePost(
            postData: MyView.PostData
        ) {
            // TODO
        }*/

    }

}