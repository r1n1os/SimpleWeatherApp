package com.example.simpleweatherapp.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.AppDatabase
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import com.example.simpleweatherapp.services.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceListViewModel(application: Application): BaseViewModel(application) {

    private val db by lazy { AppDatabase.getDatabase(getApplication()) }
    val savedWeatherPlacesList = MutableLiveData<MutableList<GeneralAndSpecificWeatherData>>()

    fun loadAllSavedWeatherPlacesFromLocalDatabase(){
        launch {
            flow {
                var savedWeatherPlaces = mutableListOf<GeneralAndSpecificWeatherData>()
                withContext(Dispatchers.IO) {
                    savedWeatherPlaces = db.generalWeatherDataDao().getWeatherData()!!
                }
                emit(savedWeatherPlaces)
            }.collect {
                savedWeatherPlacesList.value = it
            }
        }
    }
}