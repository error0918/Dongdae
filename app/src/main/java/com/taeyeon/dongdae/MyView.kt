@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.dongdae.data.*
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

object MyView {

    object DialogDefaults {
        val Modifier: Modifier = androidx.compose.ui.Modifier
        val MinWidth = 280.dp
        val MaxWidth = 560.dp
        val MinHeight = 0.dp
        val MaxHeight = Dp.Infinity
        val ContainerPadding = PaddingValues(
            start = 24.dp,
            top = 24.dp,
            end = 24.dp,
            bottom = 18.dp
        )
        val Shape: Shape @Composable get() = MaterialTheme.shapes.medium
        val ContainerColor: Color @Composable get() = MaterialTheme.colorScheme.surface
        val TonalElevation = 2.dp
        val ListTextAlign = TextAlign.Start
        val IconContentColor: Color @Composable get() = contentColorFor(backgroundColor = ContainerColor)
        val TitleContentColor: Color @Composable get() = contentColorFor(backgroundColor = ContainerColor)
        val TextContentColor: Color @Composable get() = contentColorFor(backgroundColor = ContainerColor)
        val ContentColor: Color @Composable get() = contentColorFor(backgroundColor = ContainerColor)
        val ListContentColor: Color @Composable get() = MaterialTheme.colorScheme.primary
        val ButtonContentColor: Color @Composable get() = MaterialTheme.colorScheme.primary
        val TitleTextStyle: TextStyle @Composable get() = MaterialTheme.typography.headlineSmall
        val TextTextStyle: TextStyle @Composable get() = MaterialTheme.typography.bodyMedium
        val ContentTextStyle: TextStyle @Composable get() = MaterialTheme.typography.bodyMedium
        val ListTextStyle: TextStyle @Composable get() = MaterialTheme.typography.labelLarge
        val ButtonTextStyle: TextStyle @Composable get() = MaterialTheme.typography.labelLarge
        val Properties = DialogProperties()
    }

    @Composable
    fun DialogButtonRow(
        modifier: Modifier = Modifier,
        button: @Composable RowScope.() -> Unit
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = button
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun SurfaceDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        properties: DialogProperties = DialogDefaults.Properties,
        content: @Composable () -> Unit
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Surface(
                modifier = modifier.then(
                    Modifier.sizeIn(
                        minWidth = minWidth,
                        maxWidth = maxWidth,
                        minHeight = minHeight,
                        maxHeight = maxHeight
                    )
                ),
                shape = shape,
                color = containerColor,
                tonalElevation = tonalElevation,
                content = content
            )
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun BaseDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        content: (@Composable () -> Unit)? = null,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        contentColor: Color = DialogDefaults.ContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        contentTextStyle: TextStyle = DialogDefaults.ContentTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        SurfaceDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            properties = properties
        ) {
            Column(
                modifier = Modifier.padding(containerPadding),
                horizontalAlignment = if (icon != null) Alignment.CenterHorizontally else Alignment.Start
            ) {

                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                        Box(
                            modifier = Modifier.padding(PaddingValues(bottom = 16.dp))
                        ) {
                            icon()
                        }
                    }
                }

                title?.let {
                    CompositionLocalProvider(LocalContentColor provides titleContentColor) {
                        ProvideTextStyle(titleTextStyle) {
                            Box(
                                modifier = Modifier.padding(PaddingValues(bottom = 16.dp))
                            ) {
                                title()
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(weight = 1f, fill = false)
                ) {
                    text?.let {
                        CompositionLocalProvider(LocalContentColor provides textContentColor) {
                            ProvideTextStyle(textTextStyle) {
                                Box(
                                    modifier = Modifier
                                        .padding(PaddingValues(bottom = 16.dp))
                                        .align(Alignment.Start)
                                ) {
                                    text()
                                }
                            }
                        }
                    }

                    content?.let {
                        CompositionLocalProvider(LocalContentColor provides contentColor) {
                            ProvideTextStyle(contentTextStyle) {
                                Box(
                                    modifier = Modifier
                                        .padding(PaddingValues(bottom = 16.dp))
                                        .align(Alignment.Start)
                                ) {
                                    content()
                                }
                            }
                        }
                    }
                }

                button?.let {
                    CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
                        ProvideTextStyle(buttonTextStyle) {
                            Box(
                                modifier = Modifier
                                    .padding(PaddingValues(top = 2.dp))
                                    .align(Alignment.End),
                            ) {
                                button()
                            }
                        }
                    }
                }

            }
        }

    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            button = button,
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        dismissButton: (@Composable () -> Unit)? = null,
        confirmButton: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        MessageDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            button = {
                DialogButtonRow {
                    if (dismissButton != null) dismissButton()
                    if (confirmButton != null) confirmButton()
                }
            },
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MessageDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        MessageDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = if (icon != null) { -> Icon(imageVector = icon, contentDescription = null) } else null,
            title = if (title != null) { -> Text(text = title) } else null,
            text = if (text != null) { -> Text(text = text) } else null,
            dismissButton = if (dismissButtonText != null && onDismissButtonClick != null)
                { ->
                    TextButton(onClick = onDismissButtonClick) {
                        Text(text = dismissButtonText)
                    }
                } else null,
            confirmButton = if (confirmButtonText != null && onConfirmButtonClick != null)
                { ->
                    TextButton(onClick = onConfirmButtonClick) {
                        Text(text = confirmButtonText)
                    }
                } else null,
            shape= shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        itemCount: Int,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            items(itemCount) { index ->
                                itemContent(index)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: List<T>,
        itemContent: @Composable LazyItemScope.(item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            items(items) { item ->
                                itemContent(item)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: Array<T>,
        itemContent: @Composable LazyItemScope.(item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            itemContent = itemContent,
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: List<T>,
        itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        BaseDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            content = {
                CompositionLocalProvider(LocalContentColor provides listContentColor) {
                    ProvideTextStyle(listTextStyle) {
                        LazyColumn {
                            itemsIndexed(items) { index, item ->
                                itemContent(index, item)
                            }
                        }
                    }
                }
            },
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun <T> ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        minWidth: Dp = DialogDefaults.MinWidth,
        maxWidth: Dp = DialogDefaults.MaxWidth,
        minHeight: Dp = DialogDefaults.MinHeight,
        maxHeight: Dp = DialogDefaults.MaxHeight,
        containerPadding: PaddingValues = DialogDefaults.ContainerPadding,
        icon: (@Composable () -> Unit)? = null,
        title: (@Composable () -> Unit)? = null,
        text: (@Composable () -> Unit)? = null,
        items: Array<T>,
        itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
        button: (@Composable () -> Unit)? = null,
        shape: Shape = DialogDefaults.Shape,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            containerPadding = containerPadding,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            itemContent = itemContent,
            button = button,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        items: List<String>,
        listTextAlign: TextAlign = DialogDefaults.ListTextAlign,
        onItemClick: (index: Int, item: String) -> Unit,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = if (icon != null) { -> Icon(imageVector = icon, contentDescription = null) } else null,
            title = if (title != null) { -> Text(text = title) } else null,
            text = if (text != null) { -> Text(text = text) } else null,
            items = items,
            itemContent = { index, item ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { onItemClick(index, item) },
                        shape = RectangleShape
                    ) {
                        Text(
                            text = item,
                            textAlign = listTextAlign,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            },
            button = {
                DialogButtonRow {
                    if (dismissButtonText != null && onDismissButtonClick != null) {
                        TextButton(onClick = onDismissButtonClick) {
                            Text(text = dismissButtonText)
                        }
                    }
                    if (confirmButtonText != null && onConfirmButtonClick != null) {
                        TextButton(onClick = onConfirmButtonClick) {
                            Text(text = confirmButtonText)
                        }
                    }
                }
            },
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun ListDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = DialogDefaults.Modifier,
        icon: ImageVector? = null,
        title: String? = null,
        text: String? = null,
        items: Array<String>,
        listTextAlign: TextAlign = DialogDefaults.ListTextAlign,
        onItemClick: (index: Int, item: String) -> Unit,
        dismissButtonText: String? = null,
        confirmButtonText: String? = null,
        onDismissButtonClick: (() -> Unit)? = onDismissRequest,
        onConfirmButtonClick: (() -> Unit)? = null,
        containerColor: Color = DialogDefaults.ContainerColor,
        tonalElevation: Dp = DialogDefaults.TonalElevation,
        iconContentColor: Color = DialogDefaults.IconContentColor,
        titleContentColor: Color = DialogDefaults.TitleContentColor,
        textContentColor: Color = DialogDefaults.TextContentColor,
        listContentColor: Color = DialogDefaults.ListContentColor,
        buttonContentColor: Color = DialogDefaults.ButtonContentColor,
        titleTextStyle: TextStyle = DialogDefaults.TitleTextStyle,
        textTextStyle: TextStyle = DialogDefaults.TextTextStyle,
        listTextStyle: TextStyle = DialogDefaults.ListTextStyle,
        buttonTextStyle: TextStyle = DialogDefaults.ButtonTextStyle,
        properties: DialogProperties = DialogDefaults.Properties
    ) {
        ListDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            icon = icon,
            title = title,
            text = text,
            items = items.toList(),
            listTextAlign = listTextAlign,
            onItemClick = onItemClick,
            dismissButtonText = dismissButtonText,
            confirmButtonText = confirmButtonText,
            onDismissButtonClick = onDismissButtonClick,
            onConfirmButtonClick = onConfirmButtonClick,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            listContentColor = listContentColor,
            buttonContentColor = buttonContentColor,
            titleTextStyle = titleTextStyle,
            textTextStyle = textTextStyle,
            listTextStyle = listTextStyle,
            buttonTextStyle = buttonTextStyle,
            properties = properties
        )
    }


    object MyTextFieldDefaults {
        val modifier: Modifier @Composable get() = Modifier
        const val enabled: Boolean = true
        const val readOnly: Boolean = false
        val textStyle: TextStyle @Composable get() = LocalTextStyle.current
        val leadingIcon: @Composable (() -> Unit)? = null
        val trailingIcon: @Composable (() -> Unit)? = null
        val visualTransformation: VisualTransformation = VisualTransformation.None
        val keyboardOptions: KeyboardOptions = KeyboardOptions.Default
        val keyboardActions: KeyboardActions = KeyboardActions.Default
        const val singleLine: Boolean = false
        val onTextLayout: (TextLayoutResult) -> Unit = {}
        val interactionSource: MutableInteractionSource @Composable get() = remember { MutableInteractionSource() }
        val cursorBrush: Brush @Composable get() = SolidColor(MaterialTheme.colorScheme.primary)
        val shape: CornerBasedShape @Composable get() = MaterialTheme.shapes.medium
        val containerColor: Color @Composable get() = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
        val contentColor: Color @Composable get() = LocalContentColor.current
        val border: BorderStroke @Composable get() = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        val textFiledAlignment: Alignment = Alignment.CenterStart
        val padding: BoxPadding = BoxPadding.All

        enum class BoxPadding {
            None, All, Vertical, Horizontal
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun MyTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = MyTextFieldDefaults.modifier,
        enabled: Boolean = MyTextFieldDefaults.enabled,
        readOnly: Boolean = MyTextFieldDefaults.readOnly,
        textStyle: TextStyle = MyTextFieldDefaults.textStyle,
        leadingIcon: @Composable (() -> Unit)? = MyTextFieldDefaults.leadingIcon,
        trailingIcon: @Composable (() -> Unit)? = MyTextFieldDefaults.trailingIcon,
        visualTransformation: VisualTransformation = MyTextFieldDefaults.visualTransformation,
        keyboardOptions: KeyboardOptions = MyTextFieldDefaults.keyboardOptions,
        keyboardActions: KeyboardActions = MyTextFieldDefaults.keyboardActions,
        singleLine: Boolean = MyTextFieldDefaults.singleLine,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = MyTextFieldDefaults.onTextLayout,
        interactionSource: MutableInteractionSource = MyTextFieldDefaults.interactionSource,
        cursorBrush: Brush = MyTextFieldDefaults.cursorBrush,
        shape: CornerBasedShape = MyTextFieldDefaults.shape,
        containerColor: Color = MyTextFieldDefaults.containerColor,
        contentColor: Color = MyTextFieldDefaults.contentColor,
        textColor: Color = contentColor,
        border: BorderStroke = MyTextFieldDefaults.border,
        textFiledAlignment: Alignment = MyTextFieldDefaults.textFiledAlignment,
        padding: MyTextFieldDefaults.BoxPadding = MyTextFieldDefaults.padding
    ) {
        var size by remember { mutableStateOf(Size.Zero) }
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        var isFocused by remember { mutableStateOf(false) }

        Surface(
            color = containerColor,
            contentColor = contentColor,
            shape = shape,
            border = border,
            onClick = {
                if (isFocused)
                    focusManager.clearFocus()
                else
                    focusRequester.requestFocus()
            },
            modifier = modifier
                .onSizeChanged { intSize ->
                    size = intSize.toSize()
                }
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = when (padding) {
                                MyTextFieldDefaults.BoxPadding.None -> 0.dp
                                MyTextFieldDefaults.BoxPadding.All -> getCornerSize(
                                    shape = shape,
                                    size = size
                                )
                                MyTextFieldDefaults.BoxPadding.Horizontal -> getCornerSize(
                                    shape = shape,
                                    size = size
                                )
                                MyTextFieldDefaults.BoxPadding.Vertical -> 0.dp
                            },
                            vertical = when (padding) {
                                MyTextFieldDefaults.BoxPadding.None -> 0.dp
                                MyTextFieldDefaults.BoxPadding.All -> getCornerSize(
                                    shape = shape,
                                    size = size
                                )
                                MyTextFieldDefaults.BoxPadding.Horizontal -> 0.dp
                                MyTextFieldDefaults.BoxPadding.Vertical -> getCornerSize(
                                    shape = shape,
                                    size = size
                                )
                            }
                        )
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    leadingIcon?.let {
                        it()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = textFiledAlignment
                    ) {
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    isFocused = it.isFocused
                                },
                            enabled = enabled,
                            readOnly = readOnly,
                            textStyle = textStyle.copy(
                                color = textColor
                            ),
                            keyboardOptions = keyboardOptions,
                            keyboardActions = keyboardActions,
                            singleLine = singleLine,
                            maxLines = maxLines,
                            visualTransformation = visualTransformation,
                            onTextLayout = onTextLayout,
                            interactionSource = interactionSource,
                            cursorBrush = cursorBrush
                        )
                    }
                    trailingIcon?.let {
                        it()
                    }
                }
            }
        }
    }


    object FullBackgroundSliderDefaults {
        const val enabled = true
        val valueRange = 0.01f .. 10f
        const val steps = 0
        const val isShowingPopup = true
        const val roundingDigits = 2
    }

    @Composable
    fun FullBackgroundSlider(
        value: Float,
        onValueChange: (Float) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = FullBackgroundSliderDefaults.enabled,
        valueRange: ClosedFloatingPointRange<Float> = FullBackgroundSliderDefaults.valueRange,
        steps: Int = FullBackgroundSliderDefaults.steps,
        onValueChangeFinished: (() -> Unit)? = {},
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        colors: SliderColors =
            SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),

        isShowingPopup: Boolean = FullBackgroundSliderDefaults.isShowingPopup,
        roundingDigits: Int = FullBackgroundSliderDefaults.roundingDigits
    ) {
        var trackWidth by remember { mutableStateOf(0.dp) }
        var valueRatio by remember { mutableStateOf(0f) }
        var sliderWidth = 0f

        val sliderPosition by Delegates.observable(if (value in valueRange) value else valueRange.start) { _, _, _ ->
            trackWidth = (sliderWidth - 32f - 20f).dp * valueRatio + 10.dp
        }

        val valueSize = (valueRange.endInclusive - valueRange.start)
        valueRatio = (sliderPosition - valueRange.start) / valueSize

        val sliderOffset = remember { mutableStateOf(Offset.Zero) }
        val sliderSize = remember { mutableStateOf(IntSize.Zero) }
        sliderWidth = with (LocalDensity.current) { sliderSize.value.width.toDp().value }
        val isSliding = remember { mutableStateOf(false) }

        trackWidth = (sliderWidth - 32f - 20f).dp * valueRatio + 10.dp

        val popupSize = remember { mutableStateOf(IntSize.Zero) }
        val popupOffset =
            with (LocalDensity.current) {
                IntOffset(
                    (sliderOffset.value.x + (16.dp + 10.dp).toPx()
                            + (sliderSize.value.width - (32.dp + 20.dp).toPx()) * valueRatio
                            - popupSize.value.width / 2).toInt(),
                    (sliderOffset.value.y - popupSize.value.height).toInt()
                )
            }

        if (isSliding.value && isShowingPopup) {
            Popup(
                properties = PopupProperties(dismissOnBackPress = false),
                popupPositionProvider = object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        return popupOffset
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .onSizeChanged { size ->
                            popupSize.value = size
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${String.format("%.${roundingDigits}f", sliderPosition)} / ${valueRange.endInclusive}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(percent = 100)
                )
                .onSizeChanged {
                    sliderSize.value = it
                }
                .onPlaced { onPlaced ->
                    sliderOffset.value =
                        Offset(onPlaced.positionInWindow().x, onPlaced.positionInWindow().y)
                }
        ) {
            Spacer(
                modifier = Modifier
                    .width(if (trackWidth > 20.dp) trackWidth + 16.dp else 20.dp + 16.dp)
                    .height(20.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        shape = if (trackWidth > 15.dp) RoundedCornerShape(
                            topStartPercent = 100,
                            topEndPercent = 0,
                            bottomStartPercent = 100,
                            bottomEndPercent = 0
                        ) else CircleShape
                    )
            )


            Slider(
                value = sliderPosition,
                onValueChange = { value ->
                    onValueChange(value)
                    isSliding.value = true
                    valueRatio = (value - valueRange.start) / valueSize
                },
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = {
                    isSliding.value = false
                    if (onValueChangeFinished != null) onValueChangeFinished()
                },
                interactionSource = interactionSource,
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }


    @Composable
    fun LoadingPopup() {
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

            androidx.compose.foundation.Canvas(
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


    @Composable
    fun AppNameText(
        modifier: Modifier = Modifier,
        isShadowed: Boolean = true
    ) {
        val shadowStyle = MaterialTheme.typography.displayLarge.copy(
            shadow = if (isShadowed) Shadow(
                color = LocalContentColor.current.copy(alpha = 0.5f),
                blurRadius = 10f
            ) else Shadow()
        )

        Box(modifier = modifier) {
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
                    Text(
                        text = "동",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = shadowStyle
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
                            style = shadowStyle
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "대",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = shadowStyle
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
                            style = shadowStyle
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = shadowStyle
                )
            }
        }
    }


    @Composable
    fun NameUnit(
        modifier: Modifier = Modifier,
        id: String,
        surfaceColor: Color? = null
    ) {
        NameUnit(
            modifier = modifier,
            name = getName(id),
            subName = getSubName(id),
            uniqueColor = getUniqueColor(id),
            surfaceColor = surfaceColor
        )
    }

    @Composable
    fun NameUnit(
        modifier: Modifier = Modifier,
        name: String,
        subName: String? = null,
        uniqueColor: Color? = null,
        surfaceColor: Color? = null
    ) {
        Box(
            modifier = modifier
        ) {
            var nameTextSize by remember { mutableStateOf(IntSize.Zero) }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = with(LocalDensity.current) { nameTextSize.height.toDp() } + 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (surfaceColor != null) contentColorFor(backgroundColor = surfaceColor) else LocalContentColor.current).copy(0.8f),
                    modifier = Modifier
                        .onSizeChanged { intSize ->
                            nameTextSize = intSize
                        }
                )

                subName?.let {
                    Text(
                        text = "($subName)",
                        style = MaterialTheme.typography.labelSmall,
                        color = (if (surfaceColor != null) contentColorFor(backgroundColor = surfaceColor) else LocalContentColor.current).copy(0.4f),
                        modifier = Modifier
                    )
                }
            }

            uniqueColor?.let {
                Spacer(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { nameTextSize.height.toDp() })
                        .height(with(LocalDensity.current) { nameTextSize.height.toDp() })
                        .align(Alignment.CenterEnd)
                        .background(
                            color = uniqueColor,
                            shape = CircleShape
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = LocalContentColor.current
                            ),
                            shape = CircleShape
                        )
                )
            }
        }
    }


    @Composable
    fun BoxScope.ChatUnit(
        chatData: ChatData,
        chatSequence: ChatSequence = ChatSequence.Default,
        isMe: Boolean = com.taeyeon.dongdae.id == id,
    ) {
        chatData.run {
            ChatUnit(
                id = id,
                message = message,
                chatSequence = chatSequence,
                isMe = isMe
            )
        }
    }

    @Composable
    fun BoxScope.ChatUnit(
        id: String,
        message: String,
        chatSequence: ChatSequence = ChatSequence.Default,
        isMe: Boolean = com.taeyeon.dongdae.id == id,
    ) {
        val surfaceColor = if (isMe) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surfaceVariant
        Surface(
            modifier = Modifier
                .padding(
                    top = if (chatSequence == ChatSequence.Default) 16.dp else 8.dp,
                    bottom = 0.dp,
                    start = if (isMe) 80.dp else 8.dp,
                    end = if (isMe) 8.dp else 80.dp
                )
                .align(if (isMe) Alignment.CenterEnd else Alignment.CenterStart),
            shape = RoundedCornerShape(
                topStart = if (isMe) 16.dp else 0.dp,
                topEnd = if (isMe) 0.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = surfaceColor
        ) {
            var nameBoxSize by remember { mutableStateOf(IntSize.Zero) }
            var isNameBoxSizeInitialized by remember { mutableStateOf(false) }
            var messageSize by remember { mutableStateOf(IntSize.Zero) }
            var isMessageSizeInitialized by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .run {
                        if (isMessageSizeInitialized && isNameBoxSizeInitialized && nameBoxSize.width < messageSize.width) fillMaxWidth()
                        else this
                    }
            ) {
                NameUnit(
                    modifier = Modifier
                        .onSizeChanged { intSize ->
                            if (!isNameBoxSizeInitialized) {
                                nameBoxSize = intSize
                                isNameBoxSizeInitialized = true
                            }
                        }
                        .run {
                            if (isMessageSizeInitialized && isNameBoxSizeInitialized && nameBoxSize.width < messageSize.width) fillMaxWidth()
                            else this
                        },
                    id = id,
                    surfaceColor = surfaceColor
                )
                SelectionContainer {
                    Text(
                        text = message,
                        modifier = Modifier
                            .onSizeChanged { intSize ->
                                if (!isMessageSizeInitialized) {
                                    messageSize = intSize
                                    isMessageSizeInitialized = true
                                }
                            }
                    )
                }
            }
        }
    }


    @Composable
    fun PostUnit(
        postData: PostData
    ) {
        postData.run {
            PostUnit(
                time = time,
                id = id,
                image = image,
                contentDescription = contentDescription,
                isSelectable = isSelectable,
                content = content,
                heartCount = heartCount,
                isHeartAble = isHeartAble,
                postCategory = postCategory,
                password = password,
                commentList = commentList,
                postId = postId
            )
        }
    }

    @SuppressLint("NewApi")
    @Composable
    fun PostUnit(
        time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")),
        id: String,
        image: ImageBitmap? = null,
        contentDescription: String? = null,
        isSelectable: Boolean = true,
        content: String,
        heartCount: Int,
        isHeartAble: Boolean = true,
        postCategory: PostCategory = PostCategory.Unspecified,
        password: String = "0000",
        commentList: List<ChatData> = listOf(),
        postId: Int
    ) {
        var isHeart by rememberSaveable { mutableStateOf(false) }
        val onHeartClicked = { checked: Boolean ->
            // todo
            isHeart = checked
        }

        var isCommentShowing by rememberSaveable { mutableStateOf(false) }
        var isCommenting by rememberSaveable { mutableStateOf(false) }
        var isDropDownMenuExpanded by remember { mutableStateOf(false) }

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
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f).compositeOver(background = MaterialTheme.colorScheme.surface),
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 16.dp)
                    ) {
                        val (nameUnit, contentImage, contentText, categoryText, timeText, heart, dropDownMenuButton) = createRefs()

                        NameUnit(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(nameUnit) {
                                    top.linkTo(parent.top)
                                },
                            id = id
                        )

                        image?.let {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .heightIn(min = 100.dp, max = 200.dp)
                                    .constrainAs(contentImage) {
                                        top.linkTo(nameUnit.bottom, margin = 8.dp)
                                        centerHorizontallyTo(parent)
                                    }
                            ) {
                                Image(
                                    bitmap = image,
                                    contentDescription = contentDescription
                                )
                            }
                        }

                        if (isSelectable) {
                            SelectionContainer(
                                modifier = Modifier
                                    .constrainAs(contentText) {
                                        top.linkTo(
                                            if (image == null) nameUnit.bottom else contentImage.bottom,
                                            margin = 8.dp
                                        )
                                    }
                            ) {
                                Text(
                                    text = content,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .constrainAs(contentText) {
                                        top.linkTo(
                                            if (image == null) nameUnit.bottom else contentImage.bottom,
                                            margin = 8.dp
                                        )
                                    }
                            )
                        }

                        Text(
                            text = "카테고리: ${postCategoryNameList[PostCategory.values().indexOf(postCategory)]}",
                            color = LocalContentColor.current.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .constrainAs(categoryText) {
                                    top.linkTo(contentText.bottom, margin = 8.dp)
                                    start.linkTo(parent.start)
                                }
                        )

                        Text(
                            text = time,
                            color = LocalContentColor.current.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .constrainAs(timeText) {
                                    top.linkTo(contentText.bottom, margin = 8.dp)
                                    end.linkTo(parent.end)
                                }
                        )

                        if (isHeartAble) {
                            Surface(
                                onClick = { onHeartClicked(!isHeart) },
                                color = Color.Transparent,
                                shape = CircleShape,
                                modifier = Modifier
                                    .constrainAs(heart) {
                                        top.linkTo(timeText.bottom, margin = 8.dp)
                                        centerHorizontallyTo(parent)
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isHeart) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        tint = if (isHeart) Color.Red else LocalContentColor.current,
                                        contentDescription = null // TODO
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "$heartCount")
                                }
                            }
                        }

                        IconButton(
                            onClick = { isDropDownMenuExpanded = !isDropDownMenuExpanded },
                            modifier = Modifier
                                .constrainAs(dropDownMenuButton) {
                                    top.linkTo(timeText.bottom, margin = 8.dp)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null // TODO
                            )

                            DropdownMenu(
                                expanded = isDropDownMenuExpanded,
                                onDismissRequest = { isDropDownMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "TODO") }, // TODO
                                    onClick = {
                                        /*TODO*/
                                        isDropDownMenuExpanded = false
                                    }
                                )
                            }
                        }

                    }

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium) + 8.dp)
                ) {

                    AnimatedVisibility(visible = isCommentShowing) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            commentList.forEach { comment ->
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ChatUnit(chatData = comment)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    AnimatedContent(
                        targetState = isCommenting
                    ) {
                        if (it) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape
                            ) {
                                var commentValue by rememberSaveable { mutableStateOf("") }
                                MyTextField(
                                    value = commentValue,
                                    onValueChange = { value ->
                                        commentValue = value
                                    },
                                    leadingIcon = {
                                        IconButton(
                                            onClick = { isCommenting = false }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                                contentDescription = null // TODO
                                            )
                                        }
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { /*TODO*/ }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Send,
                                                contentDescription = null // TODO
                                            )
                                        }
                                    },
                                    textStyle = MaterialTheme.typography.bodySmall,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    shape = CircleShape,
                                    padding = MyTextFieldDefaults.BoxPadding.Horizontal,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                        .height(40.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { isCommentShowing = !isCommentShowing },
                                    modifier =
                                    Modifier
                                        .align(
                                            BiasAlignment(
                                                verticalBias = 0f,
                                                horizontalBias = animateFloatAsState(targetValue = if (isCommentShowing) -1f else 0f).value
                                            )
                                        )
                                ) {
                                    Row {
                                        AnimatedVisibility(visible = !isCommentShowing) {
                                            Icon(
                                                imageVector = Icons.Filled.Chat,
                                                contentDescription = null, // TODO
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                        }
                                        Icon(
                                            imageVector = if (isCommentShowing) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                            contentDescription = null // TODO
                                        )
                                    }
                                }

                                androidx.compose.animation.AnimatedVisibility(
                                    visible = isCommentShowing,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Button(
                                        onClick = { isCommenting = true }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Chat,
                                            contentDescription = null // TODO
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