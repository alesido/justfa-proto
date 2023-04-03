package com.fusion.shared.domain.models

import io.ktor.util.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

data class TextConversationMessage (
    val messageId: String = UUID.randomUUID().toString().substring(2),
    val timeStamp: Instant = Clock.System.now(),

    val senderId: String,
    val senderName: String,

    val content: String,
    val status: TextConversationMessageStatus = TextConversationMessageStatus.UNREAD,

    val isNotEmpty: Boolean = true
) {
    val timeFormatted: String get() {
        val l = timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${l.hour}:${l.minute}"
    }

    private val localDateTime = timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())

    private val isToday = localDateTime.date == Clock.System.todayIn(TimeZone.currentSystemDefault())

    /**
     *  True, if this instant belongs to a day before the given test message
     */
    fun isADayBefore(test: TextConversationMessage) =
        localDateTime.date.toEpochDays() - test.localDateTime.date.toEpochDays() < 0

    /**
     * True, if this instant belongs to a day after given test instant
     */
    fun isADayAfter(test: TextConversationMessage) =
        localDateTime.date.toEpochDays() - test.localDateTime.date.toEpochDays() > 0

    fun dayDateShort(): String = if (isToday) "Today" else with(localDateTime) {
        "${calendarNameFormat(dayOfWeek.name)}, ${calendarNameFormat(month.name)} $dayOfMonth"
    }

    private fun calendarNameFormat(s: String) = s.substring(0,3).toLowerCasePreservingASCIIRules()
        .replaceFirstChar { c -> c.uppercase() }

    companion object {

        fun empty() = TextConversationMessage(
            "", Instant.DISTANT_PAST, "", "", "",
            isNotEmpty = false
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

enum class PersonOnlineStatus {
    OPEN, BUSY, OFFLINE
}

enum class TextConversationMessageStatus {
    UNREAD, READ
}

data class TextConversationParticipantsList (
    val participants: List<TextConversationParticipant>? = null,
    val isLoaded: Boolean = false
)

data class TextConversationParticipant (
    val id: String,
    val name: String,
    val role: PersonRole,
    val isPreferred: Boolean,
    val onlineStatus: PersonOnlineStatus
) {
    fun isNone() = id.isEmpty()
    companion object {
        fun none() = TextConversationParticipant(
            "", "", PersonRole.SUPPORT, false, PersonOnlineStatus.OFFLINE)
    }
}
