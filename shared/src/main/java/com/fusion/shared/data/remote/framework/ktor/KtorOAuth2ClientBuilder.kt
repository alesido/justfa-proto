package com.fusion.shared.data.remote.framework.ktor

import com.fusion.shared.data.remote.framework.oauth.OAuthTokenInfo
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_CLIENT_ID
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.AUTH_URL
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

fun buildKtorOAuth2Client(bearerTokenStorage: BearerTokenStorage, enableNetworkLogs: Boolean): HttpClient {
    return HttpClient(CIO) {

        // any request
        defaultRequest {
            url {
                host = JfaApiConfiguration.API_BASE_URL
                url { protocol = URLProtocol.HTTPS }
            }
        }

        // authorization
        install(Auth) {
            bearer {
                loadTokens {
                    bearerTokenStorage.last()
                }
                refreshTokens {
                    val refreshTokenInfo: OAuthTokenInfo = client.submitForm(
                        url = "https://accounts.google.com/o/oauth2/token",
                        formParameters = Parameters.build {
                            append("grant_type", "refresh_token")
                            append("client_id", API_CLIENT_ID)
                            append("refresh_token", oldTokens?.refreshToken ?: "")
                        }
                    ) { markAsRefreshTokenRequest() }.body()
                    bearerTokenStorage.add(BearerTokens(refreshTokenInfo.accessToken, oldTokens?.refreshToken!!))
                    bearerTokenStorage.last()
                }
                sendWithoutRequest { request ->
                    request.url.host == AUTH_URL
                }
            }
        }

        // logging
        if (enableNetworkLogs) {
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.e(tag = "Http Client", message = message)
                    }
                }
            }
        }

        // serialization
        install(ContentNegotiation) {
            json()
        }
    }
}