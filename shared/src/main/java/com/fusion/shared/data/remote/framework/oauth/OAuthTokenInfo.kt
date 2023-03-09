package com.fusion.shared.data.remote.framework.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * OAuth token structure taken from Ktor's OAuth sample/snippet client-auth-oauth-google
 */
@Serializable
data class OAuthTokenInfo(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String? = null,
    val scope: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("id_token") val idToken: String,
)