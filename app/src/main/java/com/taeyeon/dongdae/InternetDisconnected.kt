@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object InternetDisconnected {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var scope: CoroutineScope

    @Composable
    fun InternetDisconnected() {
        scope = rememberCoroutineScope()
        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            InternetDisconnectedContent(paddingValues = paddingValues)
        }
    }

    @Composable
    fun TopBar() {
        SetStatusBarColor()

        var isShowingDialog by rememberSaveable { mutableStateOf(false) }
        if (isShowingDialog) {
            MyView.MessageDialog(
                onDismissRequest = { isShowingDialog = false },
                icon = Icons.Filled.Close,
                title = "닫기",
                text = "앱을 종료하시겠습니까?",
                dismissButtonText = "취소",
                confirmButtonText = "닫기",
                onConfirmButtonClick = {
                    Utils.shutDownApp()
                }
            )
        }

        TopAppBar(
            title = { Text(text = "문제 발생") },
            actions = {
                IconButton(
                    onClick = {
                        isShowingDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "닫기",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }

    @Composable
    fun BottomBar() {
        SetNavigationBarColor()

        BottomAppBar {
            Surface(
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    val isNetworkConnected = Utils.checkInternet()
                    if (isNetworkConnected) {
                        screen = checkScreen()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("연결 실패")
                        }
                    }
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "다시시도",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Composable
    fun InternetDisconnectedContent(paddingValues: PaddingValues) {
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
            val (appIconImage, appNameText, messageText) = createRefs()

            /* TODO
            Image(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_launcher_round),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(200.dp)
                    .constrainAs(appIconImage) {
                        bottom.linkTo(appNameText.top, margin = 16.dp)
                    }
            )
            TODO */

            Spacer(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .constrainAs(appIconImage) {
                        centerTo(parent)
                    }
            )

            MyView.AppNameText(
                modifier = Modifier
                    .constrainAs(appNameText) {
                        top.linkTo(appIconImage.bottom, margin = 8.dp)
                        centerHorizontallyTo(parent)
                    }
            )

            Text(
                text = "인터넷 안됨 ㅇㅇ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .constrainAs(messageText) {
                        top.linkTo(appNameText.bottom)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    }
            )

        }
    }

}