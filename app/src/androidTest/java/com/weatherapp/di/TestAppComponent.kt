package com.weatherapp.di

import android.app.Application
import com.weatherapp.MainActivity
import com.weatherapp.MainActivityTest
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class, ViewModelModule::class])
interface TestAppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Application): TestAppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(mainActivityTest: MainActivityTest)
}