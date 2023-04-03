package com.fusion.shared.data.remote.justfa.api.conversation

import com.fusion.shared.data.remote.justfa.models.JfaTextConversationHistoryDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JfaConversationApi: KoinComponent {

    private val httpClient: HttpClient by inject()

    suspend fun getTextConversationHistory(

        // account.person.id
        personId: String,
        // requested page number, numbering starts from 0
        page: Int? = 0,
        // requested page size
        size: Int? = 20,
        // sort order, given as list of properties names
        sort: List<String>? = listOf("timeStamp", "desc")

    ): JfaTextConversationHistoryDto {

        return httpClient.get {
            url {
                appendPathSegments("chat", "client", personId, "history")
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                parameters.append("sort", sort!!.joinToString(","))
            }
        }.body()
    }
}