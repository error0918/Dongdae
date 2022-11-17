package com.taeyeon.dongdae

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.runtime.Composable

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
        // TODO
    }

}