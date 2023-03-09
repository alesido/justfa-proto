package com.fusion.shared

import com.fusion.shared.di.sharedModule
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import kotlin.test.Test

class KoinConfigurationTest: KoinTest {

    @Test
    fun verifyKoinApplication() {
        koinApplication {
            modules(sharedModule(true))
            checkModules()
        }
    }
}