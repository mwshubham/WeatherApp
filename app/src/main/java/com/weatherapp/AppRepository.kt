package com.weatherapp

import com.weatherapp.api.*
import com.weatherapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val openWeatherService: OpenWeatherService) {

    suspend fun fetchCurrentWeatherData(city: String = "Bangalore", units: String = "metric")
            : Flow<Resource<WeatherNow>> = flow {
        emit(Resource.loading(null))

        emit(
            processResponse(
                safeApiCall(
                    call = {
                        ApiResponse.create(openWeatherService.getCurrentWeather(city, units))
                    },
                    errorMessage = "Error fetching weather data"
                )
            )
        )
    }

    suspend fun fetchWeatherForecastData(city: String = "Bangalore"): Flow<Resource<Forecast>> = flow {
        emit(Resource.loading(null))

        emit(
            processResponse(
                safeApiCall(
                    call = {
                        ApiResponse.create(openWeatherService.getWeatherForecast(city))
                    },
                    errorMessage = "Error fetching forecast data"
                )
            )
        )
    }

    private fun <T> processResponse(apiResponse: ApiResponse<T>): Resource<T> {
        return when (apiResponse) {
            is ApiEmptyResponse -> {
                Resource.error("Empty response", null)
            }
            is ApiSuccessResponse -> {
                Resource.success(apiResponse.data)
            }
            is ApiErrorResponse -> {
                Resource.error(apiResponse.errorMessage, null)
            }
        }
    }
}