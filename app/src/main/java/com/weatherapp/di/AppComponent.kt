package com.weatherapp.di

import android.app.Application
import com.weatherapp.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Application): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}