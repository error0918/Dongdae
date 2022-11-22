package com.taeyeon.dongdae.data

enum class ChatSequence {
    Default, Start, Sequence, SequenceLast
}

data class ChatData(
    val id: String,
    val message: String
)