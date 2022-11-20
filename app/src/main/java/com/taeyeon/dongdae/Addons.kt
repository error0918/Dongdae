@file:OptIn(ExperimentalPagerApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

object Addons {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "부가기능", // TODO
        filledIcon = Icons.Filled.AddCircle,
        outlinedIcon = Icons.Outlined.AddCircleOutline,
        lazyListState = lazyListState,
        fab = null,
        composable = { Addons() }
    )

    @Composable
    fun Addons() {
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
                    시간표, 급식 등 편리한 부가기능을 제공할 예정입니다.
                    현재 커뮤니티 기능 개발으로 지연되었습니다.
                """.trimIndent(), // TODO
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    Main.scope.launch {
                        Main.pagerState.animateScrollToPage(1)
                    }
                }
            ) {
                Text(text = "→ 오른쪽으로 넘기기 →") // TODO
            }

        }
    }

}