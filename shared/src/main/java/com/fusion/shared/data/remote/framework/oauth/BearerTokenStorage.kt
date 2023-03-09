package com.fusion.shared.data.remote.framework.oauth

import io.ktor.client.plugins.auth.providers.*

class BearerTokenStorage  {

    private val storage = mutableListOf<BearerTokens>()

    fun add(bearerTokens: BearerTokens) = storage.add(bearerTokens)
    fun last() = if (storage.isNotEmpty()) storage.last() else null
}