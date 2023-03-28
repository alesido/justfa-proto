package com.fusion.shared.data.remote.justfa.wsapi

import com.fusion.shared.domain.models.PersonOnlineStatus
import com.fusion.shared.domain.models.PersonRole
import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant
import kotlinx.serialization.Serializable

enum class JfaWsEventType {

    CHAT_USER_MESSAGE,

    USER_CONNECTION,

    USERS_LIST,
    USERS_LIST_REQUEST,
    USER_ACTIVATE,

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
    ASSISTANT
}

enum class JfaWsParticipantConnectionStatus {
    OPEN, CLOSE, BUSY
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
        fun fromDomain(message: TextConversationMessage): String {
            // TODO Convert domain level text message to the API model and serialize to JSON string
            return "Implement Me"
        }
    }
}

@Serializable
data class JfaWsTextMessageData(
    val type: JfaWsPayloadDataType = JfaWsPayloadDataType.TEXT_DATA,
    val messageId: String,
    val timeStamp: String,
    val senderId: String,
    val senderName: String,
    val content: String,
) {
    fun toDomain(): TextConversationMessage {
        // TODO Convert this message DTO to its domain counterpart object
        return TextConversationMessage.empty()
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
        // TODO Map JfaWsUserListItem to TextConversationParticipant
        val roleTypeDomain = try { PersonRole.valueOf(roleType.name) }
            catch (_: Throwable) { PersonRole.ADVISER }
        val onlineStatusDomain = when(connectionStatus) {
            JfaWsParticipantConnectionStatus.OPEN -> PersonOnlineStatus.OPEN
            JfaWsParticipantConnectionStatus.BUSY -> PersonOnlineStatus.BUSY
            JfaWsParticipantConnectionStatus.CLOSE -> PersonOnlineStatus.OFFLINE
        }
        return TextConversationParticipant(
            id = userId,
            name = userName,
            role = roleTypeDomain,
            isPreferred = isPreferred,
            onlineStatus = onlineStatusDomain
        )
    }
}

// endregion
// region Error Message

@Serializable
data class JfaWsErrorMessageData(
    val type: String,
    val timeStamp: String,
    val errorMessage: String
)

// endregion

