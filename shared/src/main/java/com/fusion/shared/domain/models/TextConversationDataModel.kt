package com.fusion.shared.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

data class TextConversationMessage (
    val messageId: String = UUID.randomUUID().toString().substring(2),
    val timeStamp: Instant = Clock.System.now(),

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