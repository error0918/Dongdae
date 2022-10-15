package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup

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
        val Shape: Shape
            @Composable get() { return MaterialTheme.shapes.medium }
        val ContainerColor: Color
            @Composable get() { return MaterialTheme.colorScheme.surface }
        val TonalElevation = 2.dp
        val ListTextAlign = TextAlign.Start
        val IconContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val TitleContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val TextContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val ContentColor: Color
            @Composable get() { return contentColorFor(backgroundColor = ContainerColor) }
        val ListContentColor: Color
            @Composable get() { return MaterialTheme.colorScheme.primary }
        val ButtonContentColor: Color
            @Composable get() { return MaterialTheme.colorScheme.primary }
        val TitleTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.headlineSmall }
        val TextTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.bodyMedium }
        val ContentTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.bodyMedium }
        val ListTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.labelLarge }
        val ButtonTextStyle: TextStyle
            @Composable get() { return MaterialTheme.typography.labelLarge }
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