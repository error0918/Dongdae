package com.taeyeon.dongdae

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object Community {
    val partition = Partition(
        title = "asdf",
        filledIcon = Icons.Filled.People,
        outlinedIcon = Icons.Outlined.PeopleOutline,
        fab = null,
        composable = { Community() }
    )

    @Composable
    fun Community() {
        Text("2".repeat(100))
    }

}