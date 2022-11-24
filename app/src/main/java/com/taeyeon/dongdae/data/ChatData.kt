package com.taeyeon.dongdae.data

data class ChatData(
    val id: String = "",
    val message: String = "",
    val chatId: Int = 0
)

enum class ChatSequence {
    Default, Start, Sequence, SequenceLast
}