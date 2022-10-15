@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
import androidx.core.view.ViewCompat
import com.taeyeon.core.Settings
import kotlinx.coroutines.*
import java.util.Locale

object Welcome {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var scope: CoroutineScope

    @Composable
    fun Welcome() {
        scope = rememberCoroutineScope()

        Scaffold(
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            WelcomeScreen(paddingValues = paddingValues)
        }
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
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "건너뛰기")
                }

                Text(
                    text = "테스트",
                    modifier = Modifier.align(Alignment.Center)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    FloatingActionButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = null
                        )
                    }
                    FloatingActionButton(
                        onClick = { /*TODO*/ }
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                )
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

                Row(modifier = Modifier.align(Alignment.Center)) {
                    ShadowedText(
                        text = "동",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge
                    )
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

                    ShadowedText(
                        text = "대",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge
                    )
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
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

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