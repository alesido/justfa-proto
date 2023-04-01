package com.fusion.shared.presenters.conversation.text

import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.domain.repositories.CommunicationCenterService
import com.fusion.shared.domain.repositories.TextConversationService
import com.fusion.shared.domain.repositories.TextMessagesRepository
import com.fusion.shared.framework.resultOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TextConversationPresenter: KoinComponent {

    private val accountService: AccountService by inject()
    private val communicationCenterService: CommunicationCenterService by inject()
    private val textConversationService: TextConversationService by inject()
    private val textMessagesRepository: TextMessagesRepository by inject()

    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    val conversationFlow get() = _conversationFlow.asStateFlow()
    private val _conversationFlow = MutableStateFlow(TextConversationState.initial())

    private var isHistoryLoaded = false

    init {
        startSession()
    }

    private fun startSession() {
        viewModelScope.launch {

            // prepare to track participants list
            textConversationService.listOfParticipantsFlow
                .onEach {
                    it.onSuccess { t->
                        _conversationFlow.value = _conversationFlow.value.copy(
                            stage = if (isHistoryLoaded) TextConversationStage.READY
                            else TextConversationStage.STARTING,
                            participants = t)
                    }.onFailure { e -> onError(e) }
                }
                .catch { onError(it) }
                .collect()

            // prepare to accept loaded messaging history
            textConversationService.initialMessagesFlow
                .onEach {
                    it.onSuccess { t ->
                        _conversationFlow.value.addMessages(t)
                        isHistoryLoaded = true
                        val isParticipantsLoaded = _conversationFlow.value.participants != null
                        _conversationFlow.value = _conversationFlow.value.copy(
                            stage = if (isParticipantsLoaded) TextConversationStage.READY
                            else TextConversationStage.STARTING
                        )
                    }.onFailure { e -> onError(e) }
                }
                .catch { onError(it) }
                .collect()

            // prepare to collect incoming messages
            textConversationService.incomingMessagesFlow
                .onEach {
                    it.onSuccess { t ->
                        _conversationFlow.value.addMessage(t)
                    }.onFailure { e -> onError(e) }
                }
                .catch { onError(it) }
                .collect()

            // start conversation session
            _conversationFlow.value = _conversationFlow.value.copy(
                stage = TextConversationStage.STARTING,
            )
            communicationCenterService.openSession()
                .onSuccess {
                    prepareConversation()
                }
                .onFailure {
                    _conversationFlow.value = _conversationFlow.value.copy(
                        stage = TextConversationStage.FAILURE,
                        error = it.message
                    )
                }
        }
    }

    private suspend fun prepareConversation() = resultOf {
        textConversationService.requestMessagingHistory()
        textConversationService.requestAllContacts()
        // responses to these calls expected to come later, with incoming messages
        // TODO Set timeouts on the WS API requests
    }.onFailure {
        _conversationFlow.value = _conversationFlow.value.copy(
            stage = TextConversationStage.FAILURE,
            error = it.message
        )
    }

    fun onMessageSubmitted(message: TextConversationMessage) {
        viewModelScope.launch {
            resultOf {
                accountService.currentAccount()
            }.onSuccess { currentAccount ->
                currentAccount?.let { senderAccount ->
                    sendMessage(TextConversationMessage(
                        senderId = senderAccount.id,
                        senderName = senderAccount.person.name,
                        content = messageText
                    ))
                }?: {
                    onError("Your are not authorized to send messages at the moment!")
                }
            }.onFailure { onError(it) }
        }
    }

    private suspend fun sendMessage(message: TextConversationMessage) = resultOf {
        textConversationService.sendMessage(message)
    }.onSuccess {
        _conversationFlow.value = _conversationFlow.value.withInsertedMessage(message)
    }.onFailure { onError(it) }

    private fun onError(e: Throwable) {
        _conversationFlow.value = _conversationFlow.value.copy(
            stage = TextConversationStage.FAILURE,
            error = e.message.toString()
        )
    }

    private fun onError(messageText: String) {
        Napier.d(tag = this.javaClass.simpleName, message = "### Failure: $messageText")
        _conversationFlow.value = _conversationFlow.value.copy(
            stage = FAILURE,
            error = messageText
        )
    }
}