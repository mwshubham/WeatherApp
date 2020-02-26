package com.weatherapp

import android.app.Application
import com.weatherapp.di.DaggerTestAppComponent
import com.weatherapp.di.TestAppComponent
import timber.log.Timber

class WeatherApp : Application() {
    lateinit var appComponent: TestAppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        appComponent = DaggerTestAppComponent.factory().create(this)
    }
}