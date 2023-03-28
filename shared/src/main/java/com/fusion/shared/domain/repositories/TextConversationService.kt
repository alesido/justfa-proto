package com.fusion.shared.domain.repositories

import com.fusion.shared.domain.models.TextConversationMessage
import com.fusion.shared.domain.models.TextConversationParticipant
import kotlinx.coroutines.flow.StateFlow

interface TextConversationService {

    /**
     * History of messages, i.e. list of messages exchanged before current session is started.
     */
    val initialMessagesFlow: StateFlow<Result<List<TextConversationMessage>>>

    /**
     * Flow of conversation participation change delivering notifications on a participant
     *  enter or leave conversation.
     */
    val listOfParticipantsFlow: StateFlow<Result<List<TextConversationParticipant>>>

    /**
     * Flow of text conversation messages, which are plain text messages or text messages
     *  with binary attachments. It does not support control messages, or request messages.
     */
    val incomingMessagesFlow: StateFlow<Result<TextConversationMessage>>

    /**
     * Get complete list of contacts user can communicate. As the user is a Client, the list
     * will contain contacts of Advisors and Assistants with their online status and indication
     * whether the contact is preferrable.
     */
    suspend fun requestAllContacts()
    suspend fun requestOnlineContacts()
    suspend fun requestMessagingHistory()
    suspend fun sendMessage(message: TextConversationMessage)
}