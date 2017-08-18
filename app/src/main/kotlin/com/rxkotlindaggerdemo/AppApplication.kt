package com.rxkotlindaggerdemo

import android.app.Application
import com.rxkotlindaggerdemo.injections.components.DaggerNetworkComponent
import com.rxkotlindaggerdemo.injections.components.NetworkComponent
import com.rxkotlindaggerdemo.injections.modules.RetrofitModule
import timber.log.Timber

/**
 * AppApplication
 */
class AppApplication : Application() {
    lateinit var component: NetworkComponent

    companion object {
        lateinit var instance: AppApplication;

        fun getAppContext(): AppApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
        component = DaggerNetworkComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
        Timber.plant(Timber.DebugTree())
    }
}