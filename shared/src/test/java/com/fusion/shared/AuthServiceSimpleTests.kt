package com.fusion.shared

import com.fusion.shared.data.remote.justfa.api.auth.simple.JfaAuthServiceSimple
import com.fusion.shared.data.remote.framework.oauth.BearerTokenStorage
import com.fusion.shared.di.sharedModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import kotlin.test.Test
import org.koin.test.KoinTest
import org.koin.test.get

class AuthServiceSimpleTests : KoinTest {

    @Test
    fun testGettingToken() {
        startKoin {
            modules(sharedModule(true))
        }
        val authService = get<JfaAuthServiceSimple>()
        val bearerTokenStorage = get<BearerTokenStorage>()

        runBlocking {
            authService.authorize("kent.black@test.com", "aA123456789")
        }

        println("### OAuth Token received: ${bearerTokenStorage.last()?.accessToken}")

        assert(bearerTokenStorage.last()?.accessToken?.isNotEmpty()?:false)
    }
}