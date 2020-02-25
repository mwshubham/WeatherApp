package com.weatherapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.AppRepository
import com.weatherapp.MainViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherViewModelFactory @Inject constructor(
    private val appRepository: AppRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(appRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}