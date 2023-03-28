package com.fusion.shared.presenters.conversation.text

import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant

enum class TextConversationStage {
    INITIAL, STARTING, READY, FAILURE, CLOSED
}

enum class TextConversationScreen {
    STARTING, PROGRESS, CONVERSATION, FAILURE,
}

data class TextConversationState(
    val stage: TextConversationStage = TextConversationStage.INITIAL,
    val participants: List<TextConversationParticipant>? = null,
    val screen: TextConversationScreen = TextConversationScreen.STARTING,
    val error: String? = null
) {
    private val _messages: MutableList<TextConversationMessage> = mutableListOf()
    val messages: List<TextConversationMessage> = _messages

    fun addMessage(message: TextConversationMessage) {
        _messages.add(0, message) // Add to the beginning of the list
    }

    fun addMessages(messages: List<TextConversationMessage>) {
        _messages.addAll(messages)
    }

    companion object {
        fun initial() = TextConversationState()
    }
}