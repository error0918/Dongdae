package com.taeyeon.dongdae.data

import androidx.compose.ui.graphics.ImageBitmap

enum class PostCategory {
    Unspecified, Study, SchoolLife, Tip, Game, QA
}

val postCategoryNameList by lazy {
    listOf("없음", "공부", "학교 생활", "팁", "게임", "Q&A")
} // TODO

data class PostData(
    val time: String,
    val id: String,
    val image: ImageBitmap? = null,
    val contentDescription: String? = null,
    val isSelectable: Boolean = true,
    val content: String,
    val heartCount: Int,
    val isHeartAble: Boolean = true,
    var postCategory: PostCategory = PostCategory.Unspecified,
    val password: String = "0000",
    val commentList: List<ChatData> = listOf(),
    val postId: Int
)