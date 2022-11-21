@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Utils

object InfoSheet {

    @Composable
    fun Info() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TopAppBar(
                title = { Text(text = "앱 정보") }, // TODO
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Divider(modifier = Modifier.fillMaxWidth())
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dividerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = Color.Transparent,
                            border = BorderStroke(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = CircleShape,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = stringResource(id = R.string.app_name)
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }


                    listOf(
                        "패키지" to com.taeyeon.core.Info.getPackage(),
                        "버전 이름" to com.taeyeon.core.Info.getVersionName(),
                        "버전 코드" to com.taeyeon.core.Info.getVersionCode().toString(),
                        "메이커" to com.taeyeon.core.Info.getMaker()
                    ).forEach { (title, info) ->

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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 12.dp)
                                .let {
                                    it.pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                Utils.copy(text = info)
                                            }
                                        )
                                    }
                                }
                        ) {

                            Column(
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = info,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            IconButton(
                                onClick = { Utils.copy(text = info) },
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
            }
        }
    }

}