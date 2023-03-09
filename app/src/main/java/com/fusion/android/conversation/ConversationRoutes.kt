package com.fusion.android.conversation

sealed class ConversationRoutes(val route: String) {
    object Conversation: ConversationRoutes("Conversation")
}