package com.fusion.shared.data.remote.justfa.models

import com.fusion.shared.domain.models.*
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

enum class JfaWsEventType {

    CHAT_USER_MESSAGE,

    USER_CONNECTION,
    USER_ACTIVATE,

    USERS_LIST,
    USERS_LIST_REQUEST,

    SCREEN_SHARE_RESPONSE,

    START_CALL_REQUEST,
    START_CALL_REJECT,
    CALL_SESSION_RESPONSE,
    CALL_SESSION_STREAM_DESTROYED,
    CALL_SESSION_STATUS,
    JOIN_CALL_SESSION_REQUEST,

    JOIN_CO_BROWSING_SESSION_REQUEST,

    ERROR
}

enum class JfaWsPayloadDataType {
    TEXT_DATA,

    USERS_LIST_REQUEST_DATA,
    USERS_LIST_DATA,

    USER_CONNECTION_DATA,
    USER_ACTIVATE_DATA,

    DOCUMENT_DATA,
    SCREEN_SHARE_REQUEST_DATA,

    START_CALL_REQUEST_DATA,
    START_CALL_REJECT_DATA,
    CALL_SESSION_REQUEST_DATA,
    CALL_SESSION_RESPONSE_DATA,
    JOIN_CALL_SESSION_REQUEST_DATA,
    JOIN_CHAT_REQUEST_DATA,

    JOIN_CO_BROWSING_SESSION_REQUEST_DATA
}

enum class JfaWsAccountType { User, Client }

enum class JfaWsRoleType {
    SYSTEM_ADMINISTRATOR,
    COMPANY_ADMINISTRATOR,
    ADVISER,
    SUPPORT,
    ASSISTANT;

    fun toDomain() = try { PersonRole.valueOf(this.name) }
        catch (_: Throwable) { PersonRole.ADVISER }
}

enum class JfaWsParticipantConnectionStatus {
    OPEN, CLOSE, BUSY;
    fun toDomain() = when(this) {
        OPEN -> PersonOnlineStatus.OPEN
        BUSY -> PersonOnlineStatus.BUSY
        CLOSE -> PersonOnlineStatus.OFFLINE
    }
}

enum class JfaWsTextMessageStatus {
    UNREAD, READ;
    fun toDomain() = if (this == READ)
        TextConversationMessageStatus.READ else TextConversationMessageStatus.UNREAD
}

@Serializable
open class JfaWsMessage(val event: JfaWsEventType, val data: JfaWsPayloadData)

@Serializable
open class JfaWsPayloadData(val type: JfaWsPayloadDataType)

// region Text Message

@Serializable
data class JfaWsTextMessage(val event: JfaWsEventType, val data: JfaWsTextMessageData) {
    companion object {
        /**
         * Returns JSON string containing serialized DTO of the message
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun fromDomain(message: TextConversationMessage): String {
            val jfaTextMessage = JfaWsTextMessage(
                event = JfaWsEventType.CHAT_USER_MESSAGE,
                data = JfaWsTextMessageData (
                    type = JfaWsPayloadDataType.TEXT_DATA,
                    messageId = message.messageId,
                    timeStamp = message.timeStamp.toString(),
                    senderId = message.senderId,
                    senderName = message.senderName,
                    senderRole = null,
                    senderAccountType = null,
                    recipientId = null,
                    recipientName = null,
                    clientId = null,
                    messageStatus = null,
                    content = message.content
                )
            )
            val format = Json { explicitNulls = false }
            return format.encodeToString(serializer(), jfaTextMessage)
        }
    }
}

@Serializable
data class JfaWsTextMessageData(
    val type: JfaWsPayloadDataType,
    val messageId: String,
    val timeStamp: String,
    val senderId: String,
    val senderName: String,
    val senderRole: JfaWsRoleType?,
    val senderAccountType: JfaWsAccountType?,
    val recipientId: String?,
    val recipientName: String?,
    val clientId: String?,
    val messageStatus: JfaWsTextMessageStatus?,
    val content: String
) {
    fun toDomain(): TextConversationMessage {
        return TextConversationMessage(
            messageId = messageId,
            timeStamp = Instant.parse(timeStamp),
            senderId = senderId,
            senderName = senderName,
            content = content,
            status = messageStatus?.let { messageStatus.toDomain() }
                ?: TextConversationMessageStatus.UNREAD
        )
    }
}

// endregion
// region User List

@Serializable
class JfaWsUserListRequest: JfaWsMessage(
    JfaWsEventType.USERS_LIST_REQUEST,
    JfaWsPayloadData(JfaWsPayloadDataType.USERS_LIST_REQUEST_DATA)
)

@Serializable
data class JfaWsUserListResponse(
    val event: JfaWsEventType = JfaWsEventType.USERS_LIST,
    val data: JfaWsUserListResponseData
)

@Serializable
data class JfaWsUserListResponseData (
    val timeStamp: String,
    val users: List<JfaWsUserListItem>
): JfaWsPayloadData(JfaWsPayloadDataType.USERS_LIST_DATA)

@Serializable
data class JfaWsUserListItem(
    val userId: String,
    val accountType: JfaWsAccountType,
    val roleType: JfaWsRoleType,
    val userName: String,
    val isPreferred: Boolean,
    val connectionStatus: JfaWsParticipantConnectionStatus
) {
    fun toDomain(): TextConversationParticipant {
        return TextConversationParticipant(
            id = userId,
            name = userName,
            role = roleType.toDomain(),
            isPreferred = isPreferred,
            onlineStatus = connectionStatus.toDomain()
        )
    }
}

@Serializable
data class JfaWsUserConnectionEventData(
    val timeStamp: String,
    val userId: String,
    val accountType: JfaWsAccountType,
    val roleType: JfaWsRoleType,
    val userName: String,
    val isPreferred: Boolean,
    val status: JfaWsParticipantConnectionStatus
): JfaWsPayloadData(JfaWsPayloadDataType.USER_CONNECTION_DATA) {
    fun toDomain() = TextConversationParticipant(
        id = userId,
        role = roleType.toDomain(),
        name = userName,
        isPreferred = isPreferred,
        onlineStatus = status.toDomain()
    )
}
@Serializable
data class JfaWsUserActivationEventData(
    val timeStamp: String,
    val recipientId: String?,
    val senderId: String?,
): JfaWsPayloadData(JfaWsPayloadDataType.USER_ACTIVATE_DATA)

// endregion
// region Error Message

@Serializable
data class JfaWsErrorMessageData(
    val type: String,
    val timeStamp: String,
    val errorMessage: String
)

// endregion

