package com.weatherapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestJUnitRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, WeatherApp::class.java.canonicalName, context)
    }
}