package com.taeyeon.dongdae.data

import androidx.compose.ui.graphics.ImageBitmap

/*data class PostData(
    val id: String,
    val content: String,
    val image: ImageBitmap? = null,
    val contentDescription: String? = null,
    val isSelectable: Boolean = true,
    val isHeartAble: Boolean = true,
    var postCategory: PostCategory = PostCategory.Unspecified,
    val password: String = "0000",
    val time: String,
    val heartCount: Int,
    val commentList: List<ChatData> = listOf(),
    val postId: Int
) {
    companion object {
        enum class PostCategory {
            Unspecified, Study, SchoolLife, Tip, Game, QA
        }

        val postCategoryNameList by lazy {
            listOf("없음", "공부", "학교 생활", "팁", "게임", "Q&A")
        } // TODO
    }
}*/

data class PostData(
    val id: String,
    val content: String,
    val image: Pair<ImageBitmap, String?>? = null,
    val heartCount: Int,
    val isSelectable: Boolean = true,
    val isHeartAble: Boolean = true,
    var postCategory: PostCategory = PostCategory.Unspecified,
    val password: String = "0000",
    val time: String,
    val commentList: List<ChatData> = listOf(),
    val postId: Int
)

enum class PostCategory {
    Unspecified, Study, SchoolLife, Tip, Game, QA
}

val postCategoryNameList by lazy {
    listOf("없음", "공부", "학교 생활", "팁", "게임", "Q&A")
} // TODO