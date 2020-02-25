package com.weatherapp.di

import android.app.Application
import com.weatherapp.R
import com.weatherapp.api.OpenWeatherService
import com.weatherapp.api.ServiceInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideLabService(client: OkHttpClient): OpenWeatherService {
        return Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenWeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(context: Application): OkHttpClient {
        val apiKey = context.getString(R.string.open_weather_key)

        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ServiceInterceptor(apiKey))
            .build()
    }
}