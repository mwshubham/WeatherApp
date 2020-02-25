package com.weatherapp

import com.weatherapp.api.*
import com.weatherapp.vo.ForecastUi
import com.weatherapp.vo.Resource
import com.weatherapp.vo.WeatherUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val openWeatherService: OpenWeatherService) {

    suspend fun fetchCurrentWeatherData(city: String = "Bangalore", units: String = "metric")
            : Flow<Resource<WeatherUi>> = flow {
        emit(Resource.loading(null))

        emit(
            processResponse<WeatherNow, WeatherUi>(
                safeApiCall(
                    call = {
                        ApiResponse.create(openWeatherService.getCurrentWeather(city, units))
                    },
                    errorMessage = "Error fetching weather data"
                )
            )
        )
    }

    suspend fun fetchWeatherForecastData(city: String = "Bangalore"): Flow<Resource<List<ForecastUi>>> = flow {
        emit(Resource.loading(null))

        emit(
            processResponse<Forecast, List<ForecastUi>>(
                safeApiCall(
                    call = {
                        ApiResponse.create(openWeatherService.getWeatherForecast(city))
                    },
                    errorMessage = "Error fetching forecast data"
                )
            )
        )
    }

    private inline fun <reified T, V> processResponse(apiResponse: ApiResponse<T>): Resource<V> {
        return when (apiResponse) {
            is ApiEmptyResponse -> {
                Resource.error("Empty response", null)
            }
            is ApiSuccessResponse -> {
                when (T::class) {
                    WeatherNow::class -> Resource.success((apiResponse.data as WeatherNow).toWeatherUi()) as Resource<V>
                    Forecast::class -> Resource.success((apiResponse.data as Forecast).toForecastUi()) as Resource<V>
                    else -> {
                        Timber.e("Unknown response class")
                        Resource.error("Unknown response class", null)
                    }
                }
            }
            is ApiErrorResponse -> {
                Resource.error(apiResponse.errorMessage, null)
            }
        }
    }
}