package com.example.mockads

import com.example.mockads.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import qtjambiii.ads.AdsApplication

class MyApp : AdsApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}