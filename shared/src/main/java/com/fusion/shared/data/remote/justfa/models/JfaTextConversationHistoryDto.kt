package com.fusion.shared.data.remote.justfa.models

import com.fusion.shared.domain.models.TextConversationHistoryPage
import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationMessageStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class JfaTextConversationHistoryDto (

    val content: List<JfaHistoryItem>,

    val totalElements: Int,     // total messages in the history
    val numberOfElements: Int,  // the same as total messages in the history?

    val size: Int,          // the page size
    val totalPages: Int,    // total pages of given size in the history

    val number: Int,        // this page number, numbering starts from 0

    val first: Boolean,     // true, if this is the first page
    val last: Boolean,      // true, if this is the last page
    val empty: Boolean,     // true, if this page is empty

    val sort: JfaHistorySortDescriptor
) {
    fun toDomain() = TextConversationHistoryPage(
        pageMessages = content.map {
            with (it.messageData) {
                TextConversationMessage(
                    messageId = messageId,
                    timeStamp = Instant.parse(timeStamp),
                    senderId = senderId,
                    senderName = senderName,
                    content = content,
                    status = messageStatus?.let { messageStatus.toDomain() }
                        ?: TextConversationMessageStatus.UNREAD
                )
            }
        },
        pageNumber = number,
        totalItems = totalElements
    )
}

@Serializable
data class JfaHistoryItem (
    val type: JfaWsEventType = JfaWsEventType.CHAT_USER_MESSAGE,
    val messageData: JfaHistoryMessageData
)

@Serializable
data class JfaHistoryMessageData (
    val type: JfaWsPayloadDataType = JfaWsPayloadDataType.TEXT_DATA,
    val messageId: String,
    val timeStamp: String,
    val senderId: String,
    val senderName: String,
    val senderAccountType: JfaWsAccountType,
    val messageStatus: JfaWsTextMessageStatus?,
    val content: String
)

@Serializable
data class JfaHistorySortDescriptor (
    val empty: Boolean,     // the sort order parameter was not empty in the request?
    val sorted: Boolean,    // true, if the history messages sorted in the response (?)
    val unsorted: Boolean   // // true, if the history messages aren't sorted in the response (?)
)
