package com.fusion.shared.presenters.conversation.text

import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant

enum class TextConversationStage {
    INITIAL, STARTING, READY, FAILURE, CLOSED
}

data class TextConversationState(
    val stage: TextConversationStage = TextConversationStage.INITIAL,
    val title: String? = null,
    val author: String? = null,
    val participants: List<TextConversationParticipant>? = null,
    val messages: List<TextConversationMessage> = listOf(),
    val error: String? = null
) {
    fun withInsertedMessage(message: TextConversationMessage) = this.copy(
            messages = listOf(message) + this.messages
        )

    fun withInsertedMessages(messages: List<TextConversationMessage>) = this.copy(
        messages = messages + this.messages
    )

    companion object {
        fun initial() = TextConversationState()
    }
}