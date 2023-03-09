@file:Suppress("SAFE_CALL_WILL_CHANGE_NULLABILITY")

package com.fusion.android.conversation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import okio.ByteString
import com.fusion.android.conversation.DestinationScreen.*
import com.fusion.android.conversation.WsConnectionStateKind.*
import com.fusion.android.data.HawkStoreKey
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*


class ConversationViewModel(application: Application) : AndroidViewModel(application) {

    private var ws: WebSocket? = null
    private val client by lazy { OkHttpClient() }

    private val _conversationFlow = MutableStateFlow(WsConversationState.initial())
    val conversationFlow = _conversationFlow.asStateFlow()

    private var wsIpAddress: String? = null

    var timeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")

    init {
        Hawk.init(application.applicationContext).build()
        wsIpAddress = Hawk.get<String>(HawkStoreKey.WS_SERVER_IP_AND_PORT, null)
        wsIpAddress?.let {
            startConversation()
        }
    }

    fun onServerAddressProvided(serverIp: String, serverPort: String) {
        val address = "ws://$serverIp:$serverPort"
        Hawk.put(HawkStoreKey.WS_SERVER_IP_AND_PORT, address)
        wsIpAddress = address
        startConversation()
    }

    fun omMessageSubmitted(message: WsMessage) {
        val messageWithTimeFormatted = message.copy(
            timeFormatted = timeFormatter.print(message.timestamp))
        _conversationFlow.value.addMessage(messageWithTimeFormatted)
        ws?.send(message.content)
    }

    fun closeConversation() {
        ws?.close(1000, "Goodbye !")
    }

    fun shutdown() {
        client.dispatcher.executorService.shutdown()
    }

    fun configure() {
        _conversationFlow.value = _conversationFlow.value.copy(
            screen = CONVERSATION_SETUP_SCREEN
        )
    }

    fun retry() {
        startConversation()
    }

    private fun startConversation() {
        wsIpAddress ?: return
        val request: Request = Request.Builder().url(wsIpAddress!!).build()
        val listener = object: WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                _conversationFlow.value = _conversationFlow.value.copy(
                    channelName = "Test Channel",
                    connection = WsConnectionState(CONNECTED),
                    screen = CONVERSATION_SCREEN
                )
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val currentTime = System.currentTimeMillis()
                _conversationFlow.value.addMessage(
                    WsMessage("Web", text, currentTime, timeFormatter.print(currentTime)))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                onMessage(webSocket, bytes.toString())
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _conversationFlow.value = _conversationFlow.value.copy(
                    connection = WsConnectionState(CLOSED),
                    screen = CONVERSATION_SCREEN
                )
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _conversationFlow.value = _conversationFlow.value.copy(
                    connection = WsConnectionState(CLOSING, "Closing conversation ..."),
                    screen = PROGRESS_SCREEN)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _conversationFlow.value = _conversationFlow.value.copy(
                    connection = WsConnectionState(FAILED, "Server connection error! ${t.message}"),
                    screen = FAILURE_SCREEN)
            }
        }

        ws = client.newWebSocket(request, listener)

        _conversationFlow.value = _conversationFlow.value.copy(
            connection = WsConnectionState(CONNECTING, "Connecting chat server ..."),
            screen = PROGRESS_SCREEN)
    }
}