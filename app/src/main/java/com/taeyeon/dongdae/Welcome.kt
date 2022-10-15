@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import com.google.accompanist.pager.*
import com.taeyeon.core.Settings
import kotlinx.coroutines.*
import java.util.Locale

object Welcome {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var scope: CoroutineScope
    private lateinit var pagerState: PagerState
    private val pageList = listOf<@Composable (paddingValues: PaddingValues) -> Unit>(
        { paddingValues ->  Page1(paddingValues = paddingValues) },
        { Box() {} },
        { Box() {} },
        { Box() {} }
    )

    @Composable
    fun Welcome() {
        scope = rememberCoroutineScope()
        pagerState = rememberPagerState()

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            WelcomeScreen(paddingValues = paddingValues)
        }
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = { Text(text = "환영합니다") }
        )
    }

    @Composable
    fun BottomBar() {
        val view = LocalView.current
        (view.context as Activity).window.navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).toArgb()

        BottomAppBar {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        screen = checkScreen()
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "건너뛰기")
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.Center),
                    activeColor = MaterialTheme.colorScheme.onSurface,
                    inactiveColor = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage - 1 >= 0) pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = null
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage + 1 < pagerState.pageCount) pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                else screen = checkScreen()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Page1(paddingValues: PaddingValues) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                )
        ) {
            val (appNameText, simpleAppExplanationText) = createRefs()

            /* TODO
            Image(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_launcher_round),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .constrainAs(appIconImage) {
                        bottom.linkTo(appNameText.top, margin = 16.dp)
                    }
            )
            TODO */

            Box(
                modifier = Modifier
                    .constrainAs(appNameText) {
                        centerTo(parent)
                    }
            ) {
                if (Locale.getDefault() == Locale.KOREA) {
                    val firstValues = rememberSaveable { listOf("산", "산중", "산고", "산학원", "산학교", "산학생", "산인").shuffled() }
                    var firstIndex by rememberSaveable { mutableStateOf(0) }
                    val secondValues = rememberSaveable { listOf("신", "나무숲", "커뮤").shuffled() }
                    var secondIndex by rememberSaveable { mutableStateOf(0) }
                    LaunchedEffect(true) {
                        while (true) {
                            delay(1000)
                            if (firstIndex + 1 < firstValues.size) firstIndex++
                            else firstIndex = 0
                            if (secondIndex + 1 < secondValues.size) secondIndex++
                            else secondIndex = 0
                        }
                    }

                    Row {
                        if (pagerState.currentPage == 0 && !pagerState.isScrollInProgress) {
                            ShadowedText(
                                text = "동",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        } else {
                            Text(
                                text = "동",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }
                        AnimatedContent(
                            targetState = firstIndex,
                            transitionSpec = {
                                slideInVertically { height -> -height } + fadeIn() with
                                        slideOutVertically { height -> height } + fadeOut()
                            }
                        ) {
                            Text(
                                text = firstValues[it],
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        if (pagerState.currentPage == 0 && !pagerState.isScrollInProgress) {
                            ShadowedText(
                                text = "대",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        } else {
                            Text(
                                text = "대",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }
                        AnimatedContent(
                            targetState = secondIndex,
                            transitionSpec = {
                                slideInVertically { height -> -height } + fadeIn() with
                                        slideOutVertically { height -> height } + fadeOut()
                            }
                        ) {
                            Text(
                                text = secondValues[it],
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }
                    }
                } else {
                    ShadowedText(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.simple_app_explanation),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(simpleAppExplanationText) {
                        top.linkTo(appNameText.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

        }
    }

    @Suppress("DEPRECATION")
    @Composable
    fun WelcomeScreen(paddingValues: PaddingValues) {
        val view = LocalView.current
        (view.context as Activity).window.statusBarColor = MaterialTheme.colorScheme.surface.toArgb()
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !when (darkMode) {
            Settings.SYSTEM_MODE -> isSystemInDarkTheme()
            Settings.LIGHT_MODE -> false
            Settings.DARK_MODE -> true
            else -> isSystemInDarkTheme()
        }

        HorizontalPager(
            count = pageList.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            pageList[page](paddingValues = paddingValues)

        }

    }

     object ShadowedTextDefaults {
         val Shadow = 10.dp
     }

    @Composable
    fun ShadowedText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        style: TextStyle = LocalTextStyle.current
    ) {
        Box(
            modifier = modifier
        ) {
            Popup(alignment = Alignment.Center) {
                Text(
                    text = text,
                    color = color.copy(alpha = 0.6f),
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    letterSpacing = letterSpacing,
                    textDecoration = textDecoration,
                    textAlign = textAlign,
                    lineHeight = lineHeight,
                    overflow = overflow,
                    softWrap = softWrap,
                    maxLines = maxLines,
                    style = style,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .blur(10.dp)
                        .padding(10.dp)
                )
            }
            Text(
                text = text,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                onTextLayout = onTextLayout,
                style = style,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }

}