package com.weatherapp

import android.app.Application
import com.weatherapp.di.AppComponent
import com.weatherapp.di.DaggerAppComponent
import timber.log.Timber

class WeatherApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.factory().create(this)
    }

}