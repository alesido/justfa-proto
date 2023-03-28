package com.fusion.shared.domain.models

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class TextConversationMessage (
    val messageId: String,
    val timeStamp: Instant,

    val senderId: String,
    val senderName: String,

    val content: String,

    val isEmpty: Boolean = false
) {
    val timeFormatted: String get() {
        val l = timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${l.hour}:${l.minute}"
    }

    companion object {
        fun empty() = TextConversationMessage(
            "", Instant.DISTANT_PAST, "", "", "",
            isEmpty = true
        )
    }
}

enum class PersonRole {
    SYSTEM_ADMINISTRATOR,
    COMPANY_ADMINISTRATOR,
    ADVISER,
    SUPPORT,
    ASSISTANT
}

enum class PersonOnlineStatus {  OPEN, BUSY, OFFLINE }

data class TextConversationParticipant (
    val id: String,
    val name: String,
    val role: PersonRole,
    val isPreferred: Boolean,
    val onlineStatus: PersonOnlineStatus
)