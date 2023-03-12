package com.fusion.shared.data.remote.justfa.api.auth.simple

import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_CLIENT_ID
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.AUTH_TOKEN_REQUEST_URL
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.models.OAuthSimpleTokenInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class JfaAuthServiceSimple(val httpClient: HttpClient, val bearerTokenStorage: BearerTokenStorage) {

    suspend fun authorize(userName: String, password: String): String {

        val tokenInfo: OAuthSimpleTokenInfo = httpClient.submitForm(
            url = AUTH_TOKEN_REQUEST_URL,
            formParameters = Parameters.build {
                append("username", userName)
                append("password", password)
                append("grant_type", "password")
                append("scope", "global")
                append("account_type", "Client")
                append("client_id", API_CLIENT_ID)
            }
        ).body()

        bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))
        return tokenInfo.accessToken
    }
}