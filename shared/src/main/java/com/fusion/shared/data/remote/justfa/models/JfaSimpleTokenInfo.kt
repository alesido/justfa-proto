package com.fusion.shared.data.remote.justfa.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  OAuth token as it defined in simplified OAuth flow used in the project (JustFA, Fusion)
 **/
@Serializable
data class OAuthSimpleTokenInfo(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("expires_in") val expiresIn: Int,
    val scope: String,
    @SerialName("account_type") val accountType: String,
    @SerialName("account_id") val accountId: String,
)