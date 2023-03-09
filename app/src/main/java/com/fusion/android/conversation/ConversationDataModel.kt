package com.fusion.android.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.toMutableStateList
import com.fusion.android.R
import com.fusion.android.conversation.DestinationScreen.*

enum class WsConnectionStateKind {
    INITIAL, CONNECTING, CONNECTED, FAILED, CLOSING, CLOSED
}

data class WsConnectionState(
    val value: WsConnectionStateKind = WsConnectionStateKind.INITIAL,
    val text: String? = null) {
    companion object {
        fun initial() = WsConnectionState()
    }
}

data class WsConversationState(
    val connection: WsConnectionState,
    val channelName: String,
    val channelMembers: Int,
    val initialMessages: List<WsMessage>,
    val screen: DestinationScreen
) {
    private val _messages: MutableList<WsMessage> = initialMessages.toMutableStateList()
    val messages: List<WsMessage> = _messages

    fun addMessage(msg: WsMessage) {
        _messages.add(0, msg) // Add to the beginning of the list
    }

    companion object {
        fun initial() = WsConversationState(
            WsConnectionState.initial(),
            "", 0,
            listOf(),
            CONVERSATION_SETUP_SCREEN
        )
    }
}

@Immutable
data class WsMessage(
    val author: String,
    val content: String,
    val timestamp: Long,
    val timeFormatted: String? = null,
    val image: Int? = null,
    val authorImage: Int = if (author == "me") R.drawable.ali else R.drawable.someone_else
)

/**
 * Enum representing the various screens a user can be redirected to.
 */
enum class DestinationScreen {
    CONVERSATION_SETUP_SCREEN,
    PROGRESS_SCREEN,
    CONVERSATION_SCREEN,
    FAILURE_SCREEN,
    CLOSED_SCREEN
}
