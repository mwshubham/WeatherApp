package com.weatherapp.api

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json
import com.weatherapp.vo.WeatherUi


@JsonClass(generateAdapter = true)
data class WeatherNow(
    @Json(name = "base")
    val base: String,
    @Json(name = "clouds")
    val currentClouds: CurrentClouds,
    @Json(name = "cod")
    val cod: Int,
    @Json(name = "coord")
    val currentCoord: CurrentCoord,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "main")
    val currentMain: CurrentMain,
    @Json(name = "name")
    val name: String,
    @Json(name = "sys")
    val currentSys: CurrentSys,
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "weather")
    val currentWeather: List<CurrentWeather>,
    @Json(name = "wind")
    val currentWind: CurrentWind
)

@JsonClass(generateAdapter = true)
data class CurrentClouds(
    @Json(name = "all")
    val all: Int
)

@JsonClass(generateAdapter = true)
data class CurrentCoord(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class CurrentMain(
    @Json(name = "feels_like")
    val feelsLike: Double,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "temp")
    val temp: Double,
    @Json(name = "temp_max")
    val tempMax: Double,
    @Json(name = "temp_min")
    val tempMin: Double
)

@JsonClass(generateAdapter = true)
data class CurrentSys(
    @Json(name = "country")
    val country: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
    @Json(name = "type")
    val type: Int
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "description")
    val description: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "main")
    val main: String
)

@JsonClass(generateAdapter = true)
data class CurrentWind(
    @Json(name = "deg")
    val deg: Int,
    @Json(name = "speed")
    val speed: Double
)

fun WeatherNow.toWeatherUi() = WeatherUi(
    currentTemp = currentMain.temp.toString(),
    currentWeather = currentWeather[0].main
)