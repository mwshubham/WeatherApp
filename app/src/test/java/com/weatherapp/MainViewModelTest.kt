package com.weatherapp

import com.google.common.truth.Truth
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
    private val mainViewModel = MainViewModel(appRepository)

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init no observer`() = runBlockingTest {
        Truth.assertThat(mainViewModel.forecasts).isNotNull()
        Truth.assertThat(mainViewModel.currentWeather).isNotNull()
        coVerify { appRepository.fetchWeatherForecastData(any()) wasNot Called }
        coVerify { appRepository.fetchCurrentWeatherData(any()) wasNot Called }

        mainViewModel.setCity("B")
        coVerify { appRepository.fetchWeatherForecastData(any()) wasNot Called}
        coVerify { appRepository.fetchCurrentWeatherData(any()) wasNot Called}
    }

    @Test
    fun `basic call`() = runBlockingTest {
        mainViewModel.forecasts.observeForever(mockk())
        mainViewModel.currentWeather.observeForever(mockk())
        coEvery { appRepository.fetchWeatherForecastData(any()) } returns flowOf()
        coEvery { appRepository.fetchCurrentWeatherData(any()) } returns flowOf()

        mainViewModel.setCity("Bangalore")
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }
        coVerify { appRepository.fetchCurrentWeatherData("Bangalore") }

        mainViewModel.setCity("London")
        coVerify { appRepository.fetchWeatherForecastData("London") }
        coVerify { appRepository.fetchCurrentWeatherData("London") }
    }

    @Test
    fun `retry`() = runBlockingTest {
        mainViewModel.forecasts.observeForever(mockk())
        mainViewModel.currentWeather.observeForever(mockk())
        coEvery { appRepository.fetchWeatherForecastData("Bangalore") } returns flowOf()
        coEvery { appRepository.fetchCurrentWeatherData("Bangalore") } returns flowOf()

        mainViewModel.setCity("Bangalore")
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }
        coVerify { appRepository.fetchCurrentWeatherData("Bangalore") }

        mainViewModel.retry()
        coVerify { appRepository.fetchWeatherForecastData("Bangalore") }
        coVerify { appRepository.fetchCurrentWeatherData("Bangalore") }
    }
}