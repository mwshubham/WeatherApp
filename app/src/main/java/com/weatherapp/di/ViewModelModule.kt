package com.weatherapp.di

import androidx.lifecycle.ViewModelProvider
import com.weatherapp.util.WeatherViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: WeatherViewModelFactory): ViewModelProvider.Factory
}