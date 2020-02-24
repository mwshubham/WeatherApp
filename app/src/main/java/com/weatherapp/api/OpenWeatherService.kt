package com.weatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(@Query("q") query: String = "Bangalore")
            : Response<Forecast>
}