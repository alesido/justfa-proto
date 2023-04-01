package com.fusion.shared.data.remote.justfa.wsapi

import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 *  Multipurpose persistent channel implemented with Ktor WebSockets to support
 *  text conversation, main API extensions and streaming control messaging.
 *
 *  TODO Add the exceptions classification.
 *  TODO Consider adding Resource type to return instead of rethrowing exception.
 */
class JfaWebSocketChannel: KoinComponent {

    private val httpClient: HttpClient by inject()
    private val bearerTokenStorage: BearerTokenStorage by inject()

    private var session: WebSocketSession? = null

    val incomingTextMessagesFlow get() = _incomingTextMessagesFlow.asStateFlow()
    private var _incomingTextMessagesFlow = MutableStateFlow(Result.success(""))

    suspend fun openSession(): Result<Boolean> {
        if (session?.isActive == true)
            return Result.success(true)

        val accessToken = bearerTokenStorage.last()?.accessToken
            ?: return Result.failure(Throwable("WS access is not authorised."))

        session = try { httpClient.webSocketSession {
            url {
                host = JfaApiConfiguration.WS_HOST
                port = JfaApiConfiguration.WS_PORT
                encodedPath =
                    JfaApiConfiguration.wsPathPrefix(accessToken)
            }
        }}
        catch (e: CancellationException) { throw e }
        catch (e: Exception) { return Result.failure(e) }

        if (session?.isActive != true)
            return Result.failure(Throwable("Error connecting WS server."))

        // launch coroutine to collect incoming text messages
        CoroutineScope(Dispatchers.IO).launch {
            observeTextMessages()
        }

        return Result.success(true)
    }

    fun isActive() = session?.isActive == true

    suspend fun closeSession() {
        session?.close()
    }

    /**
     * It's supposed that only text messages supported.
     */
    suspend fun sendTextMessage(text: String) {
        session?.let {
            it.send(Frame.Text(text))
            Napier.d(tag = this.javaClass.simpleName, message = "### Outgoing message \"$text\"")
        }
    }

    /**
     * Redirect incoming text messages to the published flow to share among consumers.
     * Take text messages only, ignore binary and control messages.
     * This method avoid issues due to
     */
    private suspend fun observeTextMessages() {
        session?.incoming?.consumeEach {
            try {
                val text = (it as? Frame.Text)?.readText() ?: ""
                if (text.isNotEmpty()) {
                    Napier.d(tag = this.javaClass.simpleName, message = "### Incoming message \"$text\"")
                    _incomingTextMessagesFlow.value = Result.success(text)
                }
            }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                _incomingTextMessagesFlow.value = Result.failure(e)
            }
        }
        /* Should use this method due to Channels API deprecates soon?
        session?.incoming?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map {(it as? Frame.Text)?.readText() ?: ""}
            ?.collect {
                if (it.isNotEmpty())
                    _incomingTextMessagesFlow.value = it
            }
        */
    }
}