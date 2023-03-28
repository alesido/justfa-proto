package com.fusion.shared

import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.repositories.JfaAccountService
import com.fusion.shared.data.remote.justfa.repositories.JfaTextConversationService
import com.fusion.shared.data.remote.justfa.wsapi.JfaCommunicationCenterService
import com.fusion.shared.di.sharedModule
import io.ktor.client.plugins.auth.providers.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test

class JfaTextConversationServiceTests: KoinTest {

    @Test
    fun `test Get Conversation Contacts`() {
        startKoin {
            modules(sharedModule(true))
        }

        val accountService = JfaAccountService()
        val communicationService = JfaCommunicationCenterService()
        val conversationService = JfaTextConversationService()

        val bearerTokenStorage: BearerTokenStorage by inject()
        bearerTokenStorage.add(BearerTokens(accessToken = "TestToken12345", refreshToken = ""))

        runBlocking {
            /* Uncomment to test with real WS server.
            val account = accountService.login(
                "kent.black@test.com",
                "aA123456789"
            ).last()
            println("### Account: $account")
            assert(account?.id?.isNotEmpty()?:false)
            */

            communicationService.openSession()
                .onSuccess {
                    conversationService.requestAllContacts()
                    conversationService.listOfParticipantsFlow
                        .collect {
                            it.onSuccess { t ->
                                if (t.isNotEmpty())
                                    println("### Participant 0: ${t[0]}")
                            }.onFailure { t ->
                                println("### Error getting participants list: ${t.message}")
                            }
                        }

                }
                .onFailure {
                    println("### Error opening WS session: ${it.message}")
                }
        }
    }
}