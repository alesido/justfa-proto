package com.fusion.android

import android.app.Application

import com.fusion.shared.di.sharedModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class JustFaApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // di
        startKoin {
            androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@JustFaApplication)
            modules(sharedModule(true))
        }

        // kmm logging
        if (BuildConfig.DEBUG) Napier.base(DebugAntilog())
    }
}