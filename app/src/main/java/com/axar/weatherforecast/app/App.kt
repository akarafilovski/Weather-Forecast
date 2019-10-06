package com.axar.weatherforecast.app

import android.app.Application
import com.axar.weatherforecast.di.appModule
import com.axar.weatherforecast.di.forecastModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule,forecastModule))
        }
    }
}