package com.rxkotlindaggerdemo

import android.app.Application
import timber.log.Timber

/**
 * AppApplication
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}