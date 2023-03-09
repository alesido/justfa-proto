package com.fusion.shared.di

import com.fusion.shared.data.remote.justfa.api.auth.simple.AuthServiceSimple
import com.fusion.shared.data.remote.justfa.client.buildKtorClient
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import org.koin.dsl.module

fun sharedModule(enableNetworkLogs: Boolean) = module {

    single {
        buildKtorClient(bearerTokenStorage = get(), enableNetworkLogs)
    }

    single { AuthServiceSimple(httpClient = get(), bearerTokenStorage = get()) }

    single { BearerTokenStorage() }
}

