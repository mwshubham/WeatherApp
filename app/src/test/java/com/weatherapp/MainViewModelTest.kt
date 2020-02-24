package com.weatherapp

import com.google.common.truth.Truth
import com.weatherapp.AppRepository
import com.weatherapp.InstantExecutorExtension
import com.weatherapp.MainViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class)
class MainViewModelTest {
    private val appRepository = mockk<AppRepository>()
    private val forecastViewModel = MainViewModel(appRepository)

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init no observer`() = runBlockingTest {
        Truth.assertThat(forecastViewModel.forecasts).isNotNull()
        coVerify { appRepository.fetchWeatherForecastData(any()) wasNot Called }
        forecastViewModel.setCity("B")
        coVerify { appRepository.fetchWeatherForecastData(any()) wasNot Called}
    }

    @Test
    fun `basic call`() = runBlockingTest {
        forecastViewModel.forecasts.observeForever(mockk())
        coEvery { appRepository.fetchWeatherForecastData(any()) } returns flowOf()

        forecastViewModel.setCity("Bangalore")
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }

        forecastViewModel.setCity("London")
        coVerify { appRepository.fetchWeatherForecastData("London") }
    }

    @Test
    fun `retry`() = runBlockingTest {
        forecastViewModel.forecasts.observeForever(mockk())
        coEvery { appRepository.fetchWeatherForecastData("Bangalore") } returns flowOf()

        forecastViewModel.setCity("Bangalore")
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }

        forecastViewModel.retry()
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }
    }
}