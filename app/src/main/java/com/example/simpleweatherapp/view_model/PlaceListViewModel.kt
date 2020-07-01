package com.example.simpleweatherapp.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.AppDatabase
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import com.example.simpleweatherapp.services.WeatherService

class PlaceListViewModel(application: Application): BaseViewModel(application) {

    private val db by lazy { AppDatabase.getDatabase(getApplication()) }
    val savedPlacesWeather = MutableLiveData<MutableList<GeneralAndSpecificWeatherData>>()

}