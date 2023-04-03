package com.fusion.shared.presenters.conversation.text

import com.fusion.shared.domain.models.PersonOnlineStatus
import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant
import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.domain.repositories.CommunicationCenterService
import com.fusion.shared.domain.repositories.TextConversationService
import com.fusion.shared.framework.resultOf
import com.fusion.shared.presenters.conversation.text.TextConversationStage.*
import io.github.aakira.napier.Napier
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

    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    val conversationFlow get() = _conversationFlow.asStateFlow()
    private val _conversationFlow = MutableStateFlow(TextConversationState.initial())

    // TODO Set history loaded flag to false when the feature gets implemented
    private var isHistoryLoaded = false

    init {
        startSession()
    }

    private fun startSession() {
        viewModelScope.launch {

            initConversationFlow()

            // prepare to track the events
            awaitParticipantsList()
            setToTrackParticipantsConnectivity()
            setToAcceptIncomingMessages()

            // start conversation session
            communicationCenterService.openSession()
                .onSuccess { prepareConversation() }
                .onFailure { onError(it) }
        }
    }

    /**
     * It is supposed, that current account data is available
     * at the moment Conversation Activity started
     */
    private suspend fun initConversationFlow() {
        _conversationFlow.value = _conversationFlow.value.copy(
            author = accountService.currentAccount()?.person?.name,
            stage = STARTING
        )
    }

    private suspend fun prepareConversation() = resultOf {
        textConversationService.notifySessionActive()
        textConversationService.requestAllContacts()
        setupConversationHistoryPaging()
        // responses to "request" calls expected to come later, with incoming messages
        // TODO Set timeouts on the WS API requests
    }.onFailure { onError(it) }

    private fun awaitParticipantsList() {
        // prepare to track participants list
        textConversationService.listOfParticipantsFlow
            .onEach {
                it.onSuccess { participantsList ->
                    if (participantsList.isLoaded) {
                        _conversationFlow.value = _conversationFlow.value.copy(
                            stage = if (isHistoryLoaded) READY else STARTING,
                            title = getConversationTitle(participantsList.participants),
                            participants = participantsList.participants
                        )
                    }
                }.onFailure { e -> onError(e) }
            }
            .catch { onError(it) }
            .launchIn(viewModelScope)
    }

    private fun getConversationTitle(participants: List<TextConversationParticipant>?): String {
        val defaultTitle = "JustFA Support"
        participants?: return defaultTitle
        return participants.firstOrNull { it.isPreferred }?.name
            ?: participants.firstOrNull { it.onlineStatus == PersonOnlineStatus.OPEN }?.name
            ?: defaultTitle
    }

    private fun setToTrackParticipantsConnectivity() {
        textConversationService.participantConnectionFlow
            .onEach {
                it.onSuccess { connected ->
                    val participants = _conversationFlow.value.participants
                    when (connected.onlineStatus) {
                        PersonOnlineStatus.OPEN -> {
                            // enlist newly connected participant or just update their online status
                            if (null == participants) {
                                val newParticipants = listOf(connected)
                                _conversationFlow.value = _conversationFlow.value.copy(
                                    stage = if (isHistoryLoaded) READY else STARTING,
                                    title = getConversationTitle(newParticipants),
                                    participants = newParticipants)
                            }
                            else {
                                val newParticipants = if (participants.contains(connected)) {
                                    participants.map { item ->
                                        if (item.id != connected.id)
                                            item
                                        else
                                            item.copy(onlineStatus = PersonOnlineStatus.OPEN)
                                    }
                                }
                                else {
                                    participants + listOf(connected)
                                }
                                _conversationFlow.value = _conversationFlow.value.copy(
                                    stage = if (isHistoryLoaded) READY else STARTING,
                                    title = getConversationTitle(newParticipants),
                                    participants = newParticipants
                                )
                            }
                        }
                        PersonOnlineStatus.OFFLINE -> {
                            // remove just disconnected participant from the list
                            if (participants != null) {
                                val newParticipants =
                                    participants.filter { item -> item.id != connected.id }
                                    _conversationFlow.value = _conversationFlow.value.copy(
                                        title = getConversationTitle(newParticipants),
                                        participants = newParticipants
                                    )
                            }
                        }
                        PersonOnlineStatus.BUSY -> {
                            // doesn't change participants list, though can indicate it in the UI
                            if (participants != null) {
                                _conversationFlow.value = _conversationFlow.value.copy(
                                    participants = participants.map { item ->
                                        if (item.id != connected.id) item
                                        else item.copy(onlineStatus = PersonOnlineStatus.BUSY)
                                    }
                                )
                            }
                        }
                    }
                }.onFailure { e -> onError(e) }
            }
            .catch { onError(it)}
            .launchIn(viewModelScope)
    }

    private fun setupConversationHistoryPaging() {
        // load 1st pageful of messaging history
        viewModelScope.launch {
            resultOf {
                textConversationService.loadMessagingHistoryPage(0, 40)
                    .onSuccess {
                        _conversationFlow.value = _conversationFlow.value
                            .withInsertedMessages(it.pageMessages)
                        isHistoryLoaded = true
                        val isParticipantsLoaded = _conversationFlow.value.participants != null
                        _conversationFlow.value = _conversationFlow.value.copy(
                            stage = if (isParticipantsLoaded) READY
                            else STARTING
                        )
                    }
                    .onFailure { e -> onError(e) }
            }
        }
    }

    private fun setToAcceptIncomingMessages() {
        // prepare to collect incoming messages
        textConversationService.incomingMessagesFlow
            .onEach {
                it.onSuccess { m ->
                    if (m.isNotEmpty) {
                        _conversationFlow.value = _conversationFlow.value.withInsertedMessage(m)
                    }
                }.onFailure { e -> onError(e) }
            }
            .catch { onError(it) }
            .launchIn(viewModelScope)
    }

    fun onMessageSubmitted(messageText: String) {
        // prepare and send text message
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
        onError(e.message.toString() + e.cause?.message)
    }

    private fun onError(messageText: String) {
        Napier.d(tag = this.javaClass.simpleName, message = "### Failure: $messageText")
        _conversationFlow.value = _conversationFlow.value.copy(
            stage = FAILURE,
            error = messageText
        )
    }
}