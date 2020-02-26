package com.weatherapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.weatherapp.api.OpenWeatherService
import io.mockk.coEvery
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Inject
    lateinit var openWeatherService: OpenWeatherService

    @Before
    fun setup() {
        getApp().appComponent.inject(this)
    }

    private fun getApp(): WeatherApp {
        return InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as WeatherApp
    }

    @Test
    fun mainContentIsShown() {
        onView(withId(R.id.content_main)).check(matches(isDisplayed()))
        onView(withId(R.id.error_view)).check(doesNotExist())
    }

    @Test
    fun errorViewIsShown() {
        coEvery { openWeatherService.getCurrentWeather() } returns Response.error(
            401,
            ResponseBody.create(MediaType.parse("application/txt"), "Invalid request")
        )
        coEvery { openWeatherService.getWeatherForecast() } returns Response.error(
            401,
            ResponseBody.create(MediaType.parse("application/txt"), "Invalid request")
        )

        onView(withId(R.id.error_view)).check(matches(isDisplayed()))
        onView(withId(R.id.content_main)).check(doesNotExist())
    }
}
