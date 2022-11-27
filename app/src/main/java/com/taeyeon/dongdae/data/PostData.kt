package com.taeyeon.dongdae.data

import androidx.compose.ui.graphics.ImageBitmap

data class PostData(
    val id: String = "FFFFFFFFFFFFFFFFF",

    val content: String = "",
    val image: Pair<ImageBitmap, String?>? = null,
    val heartList: List<String> = listOf(),
    var postCategory: PostCategory = PostCategory.Unspecified,

    @JvmField
    val isSelectable: Boolean = true,
    @JvmField
    val isHeartAble: Boolean = true,
    val password: String = "0000",

    val commentList: List<ChatData> = listOf(),

    val time: String = "",
    val postId: Int = 0
)

enum class PostCategory {
    Unspecified, Study, SchoolLife, Tip, Game, QA
}

val postCategoryNameList by lazy {
    listOf("없음", "공부", "학교 생활", "팁", "게임", "Q&A")
} // TODO