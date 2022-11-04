package com.taeyeon.dongdae

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable

object Notification {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "TEST 알림!!!!!!!!",
        filledIcon = Icons.Filled.Notifications,
        outlinedIcon = Icons.Outlined.Notifications,
        lazyListState = lazyListState,
        fab = null,
        composable = { Notification() } // TODO
    )

    @Composable
    fun Notification() {
        // TODO
    }

}