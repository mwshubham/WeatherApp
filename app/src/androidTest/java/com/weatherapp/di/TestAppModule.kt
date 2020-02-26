package com.weatherapp.di

import com.weatherapp.api.OpenWeatherService
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
object TestAppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherService() : OpenWeatherService = mockk<OpenWeatherService>()
}