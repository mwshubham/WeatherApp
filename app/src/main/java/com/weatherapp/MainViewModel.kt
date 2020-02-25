package com.weatherapp

import androidx.lifecycle.*
import com.weatherapp.util.Event
import com.weatherapp.vo.ForecastUi
import com.weatherapp.vo.Resource
import com.weatherapp.vo.WeatherUi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {

    private val _city = MutableLiveData<String>()

    val forecasts: LiveData<Event<Resource<List<ForecastUi>>>> = Transformations.switchMap(_city) {
        liveData {
            appRepository.fetchWeatherForecastData(it).collect {
                emit(Event(it))
            }
        }
    }

    val currentWeather: LiveData<Event<Resource<WeatherUi>>> = Transformations.switchMap(_city) {
        liveData {
            appRepository.fetchCurrentWeatherData(it).collect {
                emit(Event(it))
            }
        }
    }

    fun setCity(city: String) {
        if (_city.value != city)
            _city.value = city
    }

    fun retry() {
        _city.value?.let {
            _city.value = it
        }
    }
}