@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.DialogProperties
import com.taeyeon.core.Error
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

        @Composable
        fun EditIntUnit(
            title: String,
            value: Int,
            valueRange: IntRange,
            onValueChange: (value: Int) -> kotlin.Unit
        ) {
            var openEditValueDialog by rememberSaveable { mutableStateOf(false) }
            var openErrorValueDialog by rememberSaveable { mutableStateOf(false) }

            var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

            if (openEditValueDialog) {
                var editingValue by remember { mutableStateOf(value) }
                var editingValueString by remember { mutableStateOf(value.toString()) }

                errorMessage = null

                MyView.BaseDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null, //TODO //stringResource(id = R.string.edit),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    title = {
                        Text(text = "asdfasdf"/*stringResource(id = R.string.edit)*/)
                    },
                    text = {
                        Column {

                            Text(
                                text = "adsfasdf"/*Core.getContext().resources.getString(
                                    R.string.edit_rules_settings,
                                    "${valueRange.first} ~ ${valueRange.last}"
                                )*/,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (errorMessage != null) {
                                Text(
                                    text = "하나", // TODO //Core.getContext().resources.getString(R.string.error_message, errorMessage ?: ""),
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }
                    },
                    content = {
                        OutlinedTextField(
                            value = editingValueString,
                            onValueChange = { value ->
                                editingValueString = value
                                editingValue =
                                    try {
                                        val valueInt = value.toInt()

                                        errorMessage = if (valueInt < valueRange.first) {
                                            "하나" // TODO //Core.getContext().resources.getString(R.string.edit_too_small_error_message)
                                        } else if (valueInt > valueRange.last) {
                                            "하나" // TODO //Core.getContext().resources.getString(R.string.edit_too_big_error_message)
                                        } else {
                                            null
                                        }

                                        valueInt
                                    } catch (exception: NumberFormatException) {
                                        val error = Error(exception)

                                        errorMessage = if (error.message.indexOf("For input string") != -1) {
                                            "하나" // TODO //Core.getContext().resources.getString(R.string.edit_for_input_string_error_message)
                                        } else if (error.message.indexOf("multiple points") != -1) {
                                            "하나" // TODO //Core.getContext().resources.getString(R.string.edit_multiple_points_error_message)
                                        } else if (error.message.indexOf("empty String") != -1) {
                                            "하나" // TODO //Core.getContext().resources.getString(R.string.edit_empty_string_error_message)
                                        } else {
                                            error.message
                                        }

                                        0
                                    }
                            },
                            textStyle = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                            shape = MaterialTheme.shapes.large,
                            label = { Text(text = "fads"/*stringResource(id = R.string.edit_message)*/) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    button = {
                        MyView.DialogButtonRow {
                            TextButton(
                                onClick = {
                                    openEditValueDialog = false
                                },
                            ) {
                                Text(text = "afdasf"/*stringResource(id = R.string.dismiss)*/)
                            }

                            TextButton(
                                onClick = {
                                    openEditValueDialog = false
                                    if (errorMessage != null) {
                                        openErrorValueDialog = true
                                    } else {
                                        onValueChange(editingValue)
                                    }
                                },
                            ) {
                                Text(text = "adfa"/*stringResource(id = R.string.edit)*/)
                            }
                        }
                    },
                    onDismissRequest = {
                        openEditValueDialog = false
                    }
                )
            }

            if (openErrorValueDialog) {
                MyView.MessageDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "하나", // TODO //stringResource(id = R.string.value_error),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    title = {
                        Text(text = "adsfasdf"/*stringResource(id = R.string.value_error)*/)
                    },
                    text = {
                        Text(
                            text = "adf"/*Core.getContext().resources.getString(
                                R.string.value_error_message_settings,
                                errorMessage
                            )*/,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openErrorValueDialog = false
                                openEditValueDialog = true
                            }
                        ) {
                            Text(text = "adsfadfs"/*stringResource(id = R.string.value_reedit)*/)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openErrorValueDialog = false
                            }
                        ) {
                            Text(text = "adsfasdf"/*stringResource(id = R.string.dismiss)*/)
                        }
                    },
                    onDismissRequest = {
                        openErrorValueDialog = false
                    }
                )
            }

            TextButton(
                onClick = {
                    openEditValueDialog = true
                },
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
                    Box(
                        modifier = Modifier
                            .width(55.dp)
                            .height(35.dp)
                            .align(Alignment.CenterEnd)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(percent = 100)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.4f
                                ),
                                shape = RoundedCornerShape(percent = 100)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        @Suppress("LocalVariableName")
        @Composable
        fun PreviewScreen(modifier: Modifier, scale: Float, originalHeight: Dp = 600.dp, composableClickable: Boolean = false) {
            val DEFAULT_SMALL_TOP_APP_BAR_HEIGHT = 64.dp // Verified
            val DEFAULT_SMALL_TOP_APP_BAR_PADDING = 6.dp // Not Verified
            val DEFAULT_SMALL_TOP_APP_BAR_TEXT_START_PADDING = 6.dp // Not Verified
            val DEFAULT_SMALL_TOP_APP_BAR_ICON_BUTTON_SIZE = 48.dp // Verified
            val DEFAULT_SMALL_TOP_APP_BAR_ICON_SIZE = 24.dp // Verified

            val DEFAULT_NAVIGATION_BAR_HEIGHT = 56.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_HEIGHT = 56.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_PADDING = 6.dp // Not Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_ICON_SIZE = 24.dp // Verified
            val DEFAULT_BOTTOM_NAVIGATION_ITEM_TEXT_HEIGHT = 21.714285.dp // Verified

            val DEFAULT_FLOATING_ACTION_BUTTON_HEIGHT = 56.dp // Verified
            val DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT = 24.dp // Verified

            Surface(
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
            ) {
                Scaffold(
                    modifier = Modifier.requiredHeight(originalHeight * scale),
                    bottomBar = {
                        Surface(
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DEFAULT_NAVIGATION_BAR_HEIGHT * scale),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val list = listOf(
                                    Icons.Filled.Image to "하나", // TODO //stringResource(id = R.string.settings_preview_tab_image),
                                    Icons.Filled.Folder to "하나", // TODO //stringResource(id = R.string.settings_preview_tab_file),
                                    Icons.Filled.AccountCircle to "하나" // TODO //stringResource(id = R.string.settings_preview_tab_account)
                                )
                                list.forEach { item ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(DEFAULT_BOTTOM_NAVIGATION_ITEM_HEIGHT * scale)
                                            .also {
                                                if (composableClickable) it.clickable { }
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Spacer(
                                            modifier = Modifier.height(DEFAULT_BOTTOM_NAVIGATION_ITEM_PADDING * scale)
                                        )
                                        Icon(
                                            imageVector = item.first,
                                            contentDescription = item.second,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(DEFAULT_BOTTOM_NAVIGATION_ITEM_ICON_SIZE * scale)
                                        )
                                        Text(
                                            text = item.second,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.titleSmall.copy(fontSize = MaterialTheme.typography.titleSmall.fontSize * scale),
                                            modifier = Modifier.height(DEFAULT_BOTTOM_NAVIGATION_ITEM_TEXT_HEIGHT * scale)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        Button(
                            onClick = {  },
                            shape = MaterialTheme.shapes.medium.copy(
                                CornerSize(
                                    MaterialTheme.shapes.medium.topStart.toPx(
                                        with(LocalDensity.current) { DpSize(DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale, DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale).toSize() },
                                        LocalDensity.current
                                    ) * scale
                                )
                            ),
                            modifier = Modifier
                                .size(DEFAULT_FLOATING_ACTION_BUTTON_HEIGHT * scale)
                                .offset(x = 10.dp, y = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(0.dp),
                            enabled = composableClickable
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null, // TODO //stringResource(id = R.string.settings_preview_add),
                                modifier = Modifier.size(DEFAULT_FLOATING_ACTION_BUTTON_ICON_HEIGHT * scale)
                            )
                        }
                    },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues.calculateTopPadding() * scale,
                                bottom = paddingValues.calculateBottomPadding() * scale,
                                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) * scale,
                                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) * scale
                            )
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DEFAULT_SMALL_TOP_APP_BAR_HEIGHT * scale)
                                    .padding(DEFAULT_SMALL_TOP_APP_BAR_PADDING * scale)
                            ) {
                                Text(
                                    text = "하나", // TODO //stringResource(id = R.string.settings_preview_title),
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = MaterialTheme.typography.titleMedium.fontSize * scale),
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = DEFAULT_SMALL_TOP_APP_BAR_TEXT_START_PADDING)
                                )
                                IconButton(
                                    onClick = {  },
                                    modifier = Modifier
                                        .size(DEFAULT_SMALL_TOP_APP_BAR_ICON_BUTTON_SIZE * scale)
                                        .align(Alignment.CenterEnd),
                                    enabled = composableClickable
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null, // TODO //stringResource(id = R.string.settings_preview_refresh),
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(DEFAULT_SMALL_TOP_APP_BAR_ICON_SIZE * scale)
                                    )
                                }
                            }
                        }

                        for (i in 0 until 5) {
                            var cardSize by remember { mutableStateOf(IntSize.Zero) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp * scale, horizontal = 16.dp * scale)
                                    .onSizeChanged { size ->
                                        cardSize = size
                                    },
                                shape = MaterialTheme.shapes.large.copy(
                                    CornerSize(
                                        MaterialTheme.shapes.large.topStart.toPx(
                                            cardSize.toSize(),
                                            LocalDensity.current
                                        ) * scale
                                    )
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp * scale)
                                ) {
                                    var imageSize by remember { mutableStateOf(IntSize.Zero) }
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(with(LocalDensity.current) { (imageSize.width / 3).toDp() })
                                            .onSizeChanged { size ->
                                                if (imageSize == IntSize.Zero)
                                                    imageSize = size
                                            },
                                        shape = MaterialTheme.shapes.large.copy(
                                            CornerSize(
                                                MaterialTheme.shapes.large.topStart.toPx(
                                                    imageSize.toSize(),
                                                    LocalDensity.current
                                                ) * scale
                                            )
                                        ),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = "하나", // TODO //stringResource(id = R.string.settings_preview_image),
                                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * scale),
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp * scale))

                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        var textHeight by remember { mutableStateOf(IntSize.Zero) }
                                        Text(
                                            text = "하나", // TODO //stringResource(id = R.string.settings_preview_image_description),
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * scale),
                                            modifier = Modifier
                                                .align(Alignment.CenterStart)
                                                .onSizeChanged { size ->
                                                    textHeight = size
                                                }
                                        )
                                        IconButton(
                                            onClick = {  },
                                            modifier = Modifier
                                                .size(with(LocalDensity.current) { textHeight.height.toDp() })
                                                .align(Alignment.CenterEnd),
                                            enabled = composableClickable
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = null, // TODO //stringResource(id = R.string.settings_preview_edit),
                                                tint = MaterialTheme.colorScheme.onSurface
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

        @Composable
        fun SelectableThemeUnit(
            title: String,
            selected: Boolean,
            onClick: () -> kotlin.Unit,
            scale: Float,
            theme: @Composable (@Composable () -> kotlin.Unit) -> kotlin.Unit,
            modifier: Modifier = Modifier,
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    theme {
                        PreviewScreen(
                            modifier = Modifier.fillMaxWidth(),
                            scale = scale
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Theme {
                        RadioButton(
                            selected = selected,
                            onClick = onClick,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.primary,
                                disabledSelectedColor = MaterialTheme.colorScheme.primary,
                                disabledUnselectedColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(20.dp)
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        @Composable
        fun SelectThemeUnit(
            title: String,
            nameList: List<String>,
            themeList: List<@Composable (content: @Composable () -> kotlin.Unit) -> kotlin.Unit>,
            index: Int,
            onSelected: (index: Int) -> kotlin.Unit
        ) {
            if (nameList.size == themeList.size) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val size = nameList.size
                        for (index_ in 0 until size) {
                            SelectableThemeUnit(
                                title = nameList[index_],
                                selected = index_ == index,
                                onClick = { onSelected(index_) },
                                scale = 1f / size,
                                theme = { content ->
                                    themeList[index_](
                                        content = content
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )

                            if (index_ != size - 1) Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }

    }

}