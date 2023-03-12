package com.fusion.shared.di

import com.fusion.shared.data.remote.justfa.api.auth.simple.JfaAuthServiceSimple
import com.fusion.shared.data.remote.justfa.client.buildKtorClient
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.data.remote.justfa.repositories.JfaAccountService
import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.presenters.user.session.UserSessionPresenter
import org.koin.dsl.module

/**
 *  @see "https://insert-koin.io/docs/reference/koin-core/modules/" on how to hide private modules
 */
fun sharedModule(enableNetworkLogs: Boolean) = module {

    // remote data source (api) module, to be separated to
    single {
        buildKtorClient(bearerTokenStorage = get(), enableNetworkLogs)
    }

    single { JfaAuthServiceSimple(httpClient = get(), bearerTokenStorage = get()) }
    single { BearerTokenStorage() }

    // data (repository) module, to be separated to
    single {
        @Suppress("USELESS_CAST")
        JfaAccountService() as AccountService
    }

    // domain module, to be separated to

    // presentation module, to be separated to and exported to UI
    single { UserSessionPresenter(accountService = get()) }

}

