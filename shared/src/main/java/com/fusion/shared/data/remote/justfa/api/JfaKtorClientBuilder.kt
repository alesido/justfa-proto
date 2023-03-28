package com.fusion.shared.data.remote.justfa.client

import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_HOST
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_PATH
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_PROTOCOL
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Cbor
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Ktor Client for simplified temporary OAuth flow
 */
fun buildKtorClient(bearerTokenStorage: BearerTokenStorage, enableNetworkLogs: Boolean): HttpClient {
    return HttpClient(CIO) {

        // any request
        defaultRequest {
            //url(API_BASE_URL) - not working

            // workaround, see https://youtrack.jetbrains.com/issue/KTOR-730/Cant-set-a-base-url-that-includes-path-data
            url {
                protocol = API_PROTOCOL
                host = API_HOST
                encodedPath = API_PATH
            }

            bearerTokenStorage.last()?.accessToken?.let {
                header("Authorization", "Bearer $it")
            }
        }

        // TODO response validation/error management
        // see https://ktor.io/docs/response-validation.html
        expectSuccess = true
        HttpResponseValidator {
            // 2xx HTTP response code case
            validateResponse { response ->
//                val error: Error = response.body()
//                if (error.code != 0) {
//                    throw CustomResponseException(response, "Code: ${error.code}, message: ${error.message}")
//                }
            }
            // non 2xx HTTP response code case
            handleResponseExceptionWithRequest { exception, request ->
//                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
//                val exceptionResponse = clientException.response
//                if (exceptionResponse.status == HttpStatusCode.NotFound) {
//                    val exceptionResponseText = exceptionResponse.bodyAsText()
//                    throw MissingPageException(exceptionResponse, exceptionResponseText)
//                }
            }
        }

        // logging
        if (enableNetworkLogs) {
            install(Logging) {
                level = LogLevel.BODY
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(tag = "### Ktor ", message = message)
                    }
                }
            }
        }

        // serialization (Ktor 2.0 JsonFeature was deprecated in favor of ContentNegotiation)
        install(ContentNegotiation) {
            //json() - ignoreUnknownKeys = false by default, should change for development
            serialization(ContentType.Application.Json, Json {
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = false
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
            })
        }

        // web sockets support
        install(WebSockets)
    }
}

