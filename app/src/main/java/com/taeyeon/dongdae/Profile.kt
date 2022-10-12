package com.taeyeon.dongdae

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object Profile {
    val partition = Partition(
        title = "asdf",
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person,
        fab = null,
        composable = { Profile() }
    )

    @Composable
    fun Profile() {
        Text("0".repeat(100))
    }

}