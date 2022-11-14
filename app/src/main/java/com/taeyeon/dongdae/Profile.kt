@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Core
import com.taeyeon.core.Utils
import kotlinx.coroutines.launch

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
                        Unit.TextUnit(title = "앱 재시작") { // TODO
                            Utils.restartApp()
                        }
                    },
                    {
                        Unit.TextUnit(title = "앱 초기화") { // TODO
                            Utils.initializeData()
                            Main.scope.launch {
                                /*if (snackbarHostState.currentSnackbarData == null) {
                                    val snackbarResult =
                                        snackbarHostState.showSnackbar(
                                            message = Core.getContext().resources.getString(R.string.settings_restart_message),
                                            actionLabel = Core.getContext().resources.getString(R.string.settings_title_initialize_app_restart),
                                            duration = SnackbarDuration.Short
                                        )
                                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                                        Utils.restartApp()
                                    }
                                }*/
                            }
                        }
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
            contentPadding = PaddingValues(vertical = 16.dp)
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
        fun Block(blockData: BlockData) {
            blockData.run {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp,
                            horizontal = 32.dp
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
                                .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                        ) {
                            unitList.forEachIndexed { index, unit ->
                                unit()
                                if (index != unitList.size - 1) {
                                    val dividerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(3.5.dp)
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
        fun TextUnit(title: String, onClick: () -> kotlin.Unit) {
            TextButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
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

    }

}