package com.weatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") query: String = "Bangalore",
        @Query("units") units: String = "metric"
    ): Response<WeatherNow>

    @GET("data/2.5/forecast?units=metric")
    suspend fun getWeatherForecast(@Query("q") query: String = "Bangalore")
            : Response<Forecast>
}