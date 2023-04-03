package com.fusion.shared.domain.repositories

import com.fusion.shared.domain.models.TextConversationHistoryPage
import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant
import com.fusion.shared.domain.models.TextConversationParticipantsList
import kotlinx.coroutines.flow.StateFlow

interface TextConversationService {

    /**
     * It is flow to deliver response on a request of the list of conversation participants,
     * representing the service company, i.e. advisers, assistants, support.
     */
    val listOfParticipantsFlow: StateFlow<Result<TextConversationParticipantsList>>

    /**
     * Flow of participant enter or leave conversation events (connect or disconnect).
     */
    val participantConnectionFlow: StateFlow<Result<TextConversationParticipant>>

    /**
     * Flow of text conversation messages, which are plain text messages or text messages
     *  with binary attachments. It does not support control messages, or request messages.
     */
    val incomingMessagesFlow: StateFlow<Result<TextConversationMessage>>

    suspend fun notifySessionActive()

    /**
     * Get complete list of contacts user can communicate. As the user is a Client, the list
     * will contain contacts of Advisors and Assistants with their online status and indication
     * whether the contact is preferable.
     */
    suspend fun requestAllContacts()
    suspend fun loadMessagingHistoryPage(page: Int, pageSize: Int)
    : Result<TextConversationHistoryPage>
    suspend fun sendMessage(message: TextConversationMessage)
}