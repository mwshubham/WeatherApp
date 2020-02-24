package com.weatherapp

import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import com.weatherapp.api.Forecast
import com.weatherapp.api.OpenWeatherService
import com.weatherapp.api.WeatherNow
import com.weatherapp.vo.Status
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Okio
import org.junit.jupiter.api.*
import retrofit2.Response
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppRepositoryTest {
    private val openWeatherService = mockk<OpenWeatherService>()
    private val repository = AppRepository(openWeatherService)

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("Fetch Forecasts")
    inner class FetchForecasts {

        @Test
        fun `success`() = runBlockingTest {
            val successResponse = createResponse<Forecast>("forecast-response.json")
            coEvery { openWeatherService.getWeatherForecast() } returns Response.success(
                successResponse
            )

            val result = repository.fetchWeatherForecastData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.SUCCESS)
            Truth.assertThat(result[1].data).isEqualTo(successResponse)
        }

        @Test
        fun `failure - empty response`() = runBlockingTest {
            coEvery { openWeatherService.getWeatherForecast() } returns Response.success<Forecast>(null)

            val result = repository.fetchWeatherForecastData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Empty response")
        }

        @Test
        fun `failure - code 404`() = runBlockingTest {
            coEvery { openWeatherService.getWeatherForecast() } returns Response.error(
                401,
                ResponseBody.create(MediaType.parse("application/txt"), "Invalid request")
            )

            val result = repository.fetchWeatherForecastData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Invalid request")
        }

        @Test
        fun `failure - could not contact server`() = runBlockingTest {
            coEvery { openWeatherService.getWeatherForecast() } throws IOException()

            val result = repository.fetchWeatherForecastData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Error fetching forecast data")
        }
    }

    @Nested
    @DisplayName("Fetch Current Weather")
    inner class FetchCurrentWeather {

        @Test
        fun `success`() = runBlockingTest {
            val successResponse = createResponse<WeatherNow>("weather-response.json")
            coEvery { openWeatherService.getCurrentWeather() } returns Response.success(
                successResponse
            )

            val result = repository.fetchCurrentWeatherData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.SUCCESS)
            Truth.assertThat(result[1].data).isEqualTo(successResponse)
        }

        @Test
        fun `failure - empty response`() = runBlockingTest {
            coEvery { openWeatherService.getCurrentWeather() } returns Response.success<WeatherNow>(null)

            val result = repository.fetchCurrentWeatherData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Empty response")
        }

        @Test
        fun `failure - code 404`() = runBlockingTest {
            coEvery { openWeatherService.getCurrentWeather() } returns Response.error(
                401,
                ResponseBody.create(MediaType.parse("application/txt"), "Invalid request")
            )

            val result = repository.fetchCurrentWeatherData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Invalid request")
        }

        @Test
        fun `failure - could not contact server`() = runBlockingTest {
            coEvery { openWeatherService.getCurrentWeather() } throws IOException()

            val result = repository.fetchCurrentWeatherData().take(2).toList()

            Truth.assertThat(result[0].status).isEqualTo(Status.LOADING)
            Truth.assertThat(result[1].status).isEqualTo(Status.ERROR)
            Truth.assertThat(result[1].data).isEqualTo(null)
            Truth.assertThat(result[1].message).isEqualTo("Error fetching weather data")
        }
    }

    private inline fun <reified T> createResponse(fileName: String): T? {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream(fileName)
        val source = Okio.buffer(Okio.source(inputStream))

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(T::class.java)
        return adapter.fromJson(source)
    }
}