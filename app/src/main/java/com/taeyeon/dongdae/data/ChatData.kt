package com.taeyeon.dongdae.data

data class ChatData(
    val id: String = "",
    val message: String = ""
) {
    companion object {
        enum class ChatSequence {
            Default, Start, Sequence, SequenceLast
        }
    }
}