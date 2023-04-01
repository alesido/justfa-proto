package com.fusion.shared.di

import com.fusion.shared.data.remote.justfa.api.auth.simple.JfaAuthServiceSimple
import com.fusion.shared.data.remote.justfa.client.buildKtorClient
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.repositories.JfaAccountService
import com.fusion.shared.data.remote.justfa.repositories.JfaTextConversationService
import com.fusion.shared.data.remote.justfa.wsapi.JfaCommunicationCenterService
import com.fusion.shared.data.remote.justfa.wsapi.JfaWebSocketChannel
import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.domain.repositories.CommunicationCenterService
import com.fusion.shared.domain.repositories.TextConversationService
import com.fusion.shared.presenters.conversation.text.TextConversationPresenter
import com.fusion.shared.presenters.user.session.UserSessionPresenter
import org.koin.dsl.module

/**
 *  @see "https://insert-koin.io/docs/reference/koin-core/modules/" on how to hide private modules
 */
@Suppress("USELESS_CAST")
fun sharedModule(enableNetworkLogs: Boolean) = module {

    // remote data source (api) module, to be separated to
    single {
        buildKtorClient(bearerTokenStorage = get(), enableNetworkLogs)
    }

    single { JfaAuthServiceSimple(httpClient = get(), bearerTokenStorage = get()) }
    single { BearerTokenStorage() }

    single {
        JfaWebSocketChannel()
    }

    // data (repository) module, to be separated to
    single {
        JfaAccountService() as AccountService
    }

    single {
        JfaCommunicationCenterService() as CommunicationCenterService
    }

    single {
        JfaTextConversationService() as TextConversationService
    }

    // presentation module, to be separated to and exported to UI
    single { UserSessionPresenter(accountService = get()) }
    single { TextConversationPresenter() }
}

