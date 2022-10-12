package com.taeyeon.dongdae

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Community {
    private val lazyListState = LazyListState()
    val partition = Partition(
        title = "asdf",
        filledIcon = Icons.Filled.People,
        outlinedIcon = Icons.Outlined.PeopleOutline,
        lazyListState = lazyListState,
        fab = { Fab() },
        composable = { Community() }
    )

    @Composable
    fun Community() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(100) {
                Text(
                    text = "dfsaaaaaa${lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp)
                )
            }
        }
    }

    @Composable
    fun Fab() {
        ExtendedFloatingActionButton(
            onClick = { /*TODO*/ }
        ) {
            animateColorAsState(
                targetValue = if (lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "글쓰기"
            )
            AnimatedVisibility(visible = !lazyListState.isScrollInProgress) {
                Text(text = "글쓰기")
            }
        }
    }

}