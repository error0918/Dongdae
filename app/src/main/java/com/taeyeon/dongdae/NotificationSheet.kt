@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

object NotificationSheet {

    @Composable
    fun Notification() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text(text = "알림") }, // TODO
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Divider(modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "조금만 기달려주세요! 😄", // TODO
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = """
                    이곳에서는 알림 내용을 보여줄 예정입니다.
                """.trimIndent(), // TODO
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        Main.scope.launch {
                            Main.bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                ) {
                    Text(text = "↓ 내리기 ↓") // TODO
                }

            }
        }
    }
}