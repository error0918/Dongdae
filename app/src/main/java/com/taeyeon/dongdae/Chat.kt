package com.taeyeon.dongdae

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object Chat {
    val partition = Partition(
        title = "asdf",
        filledIcon = Icons.Filled.Chat,
        outlinedIcon = Icons.Outlined.Chat,
        fab = null,
        composable = { Chat() }
    )

    @Composable
    fun Chat() {
        Text("1".repeat(100))
    }

}