package com.fusion.shared

import com.fusion.shared.data.remote.justfa.repositories.JfaAccountService
import com.fusion.shared.di.sharedModule
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import kotlin.test.Test

class JfaAccountServiceTests: KoinTest {

    @Test
    fun `test Logging In`() {
        startKoin {
            modules(sharedModule(true))
        }

        val accountService = JfaAccountService()

        runBlocking {
            val account = accountService.login(
                "kent.black@test.com",
                "aA123456789"
            ).last()
            println("### Account: $account")
            assert(account?.id?.isNotEmpty()?:false)
        }
    }

}