package com.fusion.shared.data.remote.justfa.repositories

import com.fusion.shared.data.remote.justfa.api.conversation.JfaConversationApi
import com.fusion.shared.data.remote.justfa.models.*
import com.fusion.shared.data.remote.justfa.wsapi.*
import com.fusion.shared.domain.models.TextConversationHistoryPage
import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant
import com.fusion.shared.domain.models.TextConversationParticipantsList
import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.domain.repositories.TextConversationService
import com.fusion.shared.framework.resultOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JfaTextConversationService : TextConversationService, KoinComponent {

    private val webSocketChannel: JfaWebSocketChannel by inject()
    private val conversationApi = JfaConversationApi()
    private val accountService: AccountService by inject()

    override val listOfParticipantsFlow get() = _listOfParticipantsFlow.asStateFlow()
    private val _listOfParticipantsFlow =
        MutableStateFlow(Result.success(TextConversationParticipantsList()))

    override val participantConnectionFlow get() = _participantConnectionFlow.asStateFlow()
    private val _participantConnectionFlow =
        MutableStateFlow(Result.success(TextConversationParticipant.none()))
    override val incomingMessagesFlow get() = _incomingMessagesFlow.asStateFlow()
    private val _incomingMessagesFlow =
        MutableStateFlow(Result.success(TextConversationMessage.empty()))

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        webSocketChannel.incomingTextMessagesFlow
            .onEach {it
                .onSuccess {
                        t -> processIncomingMessage(t) }
                .onFailure {
                        e -> _incomingMessagesFlow.value = Result.failure(e) }
            }
            .catch { _incomingMessagesFlow.value = Result.failure(it) }
            .launchIn(scope)
    }

    private fun processIncomingMessage(messageBodyText: String) {
        // skip empty message (empty message used to initialize the flow)
        if (messageBodyText.isEmpty())
            return
        // parse message body as an arbitrary JSON structure
        val json = try {
            Json.parseToJsonElement(messageBodyText)
        } catch (e: SerializationException) {
            _incomingMessagesFlow.value = Result.failure(e)
            return
        }
        // find type of message by value of event type property
        json.jsonObject["event"]?.let {event ->
            val eventTypeString = event.jsonPrimitive.content
            val eventType = try {
                JfaWsEventType.valueOf(eventTypeString)
            } catch(e: IllegalArgumentException) {
                _incomingMessagesFlow.value = Result.failure(e)
                return
            }
            // map DTO to domain and feed a state flow depending on type of message (event)
            val dataElement = json.jsonObject["data"]
            if (dataElement == null) {
                _incomingMessagesFlow.value = Result.failure(Throwable(
                    "Warning: No payload in incoming message: \"$messageBodyText\""))
                return
            }
            when (eventType) {
                JfaWsEventType.USERS_LIST -> processUserListResponseMessage(dataElement)
                JfaWsEventType.USER_ACTIVATE -> updateOnParticipantActivatedConversation(dataElement)
                JfaWsEventType.USER_CONNECTION -> updateOnParticipantConnected(dataElement)
                JfaWsEventType.CHAT_USER_MESSAGE -> processTextConversationMessage(dataElement)
                JfaWsEventType.ERROR -> handleApiLevelError(dataElement)
                else -> {
                    _incomingMessagesFlow.value = Result.failure(Throwable(
                        "Warning: Ignoring not supported event type: $eventTypeString"))
                }
            }
        }
    }

    private fun processUserListResponseMessage(dataElement: JsonElement) = resultOf {
        Json.decodeFromJsonElement(JfaWsUserListResponseData.serializer(), dataElement)
    }.onSuccess { userListResponseData ->
        _listOfParticipantsFlow.value = Result.success(
            TextConversationParticipantsList(
                participants = userListResponseData.users.map { it.toDomain() },
                isLoaded = true
            )
        )
    }.onFailure {
        _listOfParticipantsFlow.value = Result.failure(it)
    }

    private fun updateOnParticipantActivatedConversation(dataElement: JsonElement) {
        // This is to update visual "Read" status of the participant's messages, or to indicate
        // the participant opened chat at their side. Not supported yet in the UI.
    }

    /**
     * Update UI to show that a participant came online and can take part in the conversation
     */
    private fun updateOnParticipantConnected(dataElement: JsonElement) = resultOf {
        Json.decodeFromJsonElement(JfaWsUserConnectionEventData.serializer(), dataElement)
    }.onSuccess {
        _participantConnectionFlow.value = Result.success(it.toDomain())
    }.onFailure {
        _participantConnectionFlow.value = Result.failure(it)
    }

    private fun processTextConversationMessage(dataElement: JsonElement) = resultOf {
        Json.decodeFromJsonElement(JfaWsTextMessageData.serializer(), dataElement)
    }.onSuccess {
        _incomingMessagesFlow.value = Result.success(it.toDomain())
    }.onFailure {
        _incomingMessagesFlow.value = Result.failure(it)
    }

    private fun handleApiLevelError(dataElement: JsonElement) = resultOf {
        Json.decodeFromJsonElement(JfaWsErrorMessageData.serializer(), dataElement)
    }.onSuccess {
        _incomingMessagesFlow.value = Result.failure(
            Throwable(it.errorMessage))
    }.onFailure {
        _incomingMessagesFlow.value = Result.failure(Throwable(
            "Warning: Skipping broken format: $dataElement"))
    }

    /**
     * Notify other conversation parties we are online.
     */
    override suspend fun notifySessionActive() {
        webSocketChannel.sendTextMessage(Json.encodeToString(JfaWsMessage(
            event = JfaWsEventType.USER_ACTIVATE,
            data = JfaWsUserActivationEventData(
                timeStamp = Clock.System.now().toString(),
                recipientId = null,
                senderId = null
            )
        )))
    }

    /**
     *  Request list of contacts of the principal through web socket channel
     */
    override suspend fun requestAllContacts() {
        webSocketChannel.sendTextMessage(Json.encodeToString(JfaWsUserListRequest()))
    }

    override suspend fun loadMessagingHistoryPage(page: Int, pageSize: Int)
    : Result<TextConversationHistoryPage> {
        val currentAccount = accountService.currentAccount()
            ?: return Result.failure(Throwable("You have to be logged in to get your chat history!"))
        return resultOf {
            val dto = conversationApi.getTextConversationHistory(
                currentAccount.person.id, page, pageSize)
            dto.toDomain()
        }
    }

    override suspend fun sendMessage(message: TextConversationMessage) {
        webSocketChannel.sendTextMessage(JfaWsTextMessage.fromDomain(message))
    }
}