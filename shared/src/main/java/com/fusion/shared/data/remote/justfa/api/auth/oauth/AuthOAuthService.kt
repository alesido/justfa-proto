package com.fusion.shared.data.remote.justfa.api.auth.oauth

import com.fusion.shared.data.remote.framework.oauth.OAuthTokenInfo
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.API_CLIENT_ID
import com.fusion.shared.data.remote.justfa.api.JfaApiConfiguration.AUTH_TOKEN_REQUEST_URL
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class AuthOAuthService(val httpClient: HttpClient, val bearerTokenStorage: BearerTokenStorage) {

    //suspend fun authorize(userName: String, password: String): String {
    suspend fun authorize(authorizationCode: String, secret: String) {

        val tokenInfo: OAuthTokenInfo = httpClient.submitForm(
            url = AUTH_TOKEN_REQUEST_URL,
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("code", authorizationCode)
                append("client_id", API_CLIENT_ID)
                append("client_secret", secret)
            }
        ).body()

        bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))
    }
}