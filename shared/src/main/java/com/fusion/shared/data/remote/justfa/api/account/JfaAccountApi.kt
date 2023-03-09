package com.fusion.shared.data.remote.justfa.api.account

import com.fusion.shared.data.remote.justfa.models.JfaAccountDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Namespace prefix "Jfa" (JustFA) to distinguish from other remote APIs
 */
class JfaAccountApi : KoinComponent {

    private val httpClient: HttpClient by inject()

    suspend fun getAccount(): JfaAccountDto {
        return  httpClient.get("me").body()
    }
}
