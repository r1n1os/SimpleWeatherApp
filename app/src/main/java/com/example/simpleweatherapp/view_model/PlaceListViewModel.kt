package com.example.simpleweatherapp.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.AppDatabase
import com.example.simpleweatherapp.database_models.BaseWeatherModel
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import com.example.simpleweatherapp.database_models.GeneralWeatherData
import com.example.simpleweatherapp.services.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceListViewModel(application: Application) : BaseViewModel(application) {

    private val db by lazy { AppDatabase.getDatabase(getApplication()) }
    private var weatherService = WeatherService().getWeatherService()
    val savedWeatherPlacesList = MutableLiveData<MutableList<GeneralAndSpecificWeatherData>>()
    val isWeatherUpdatedForSelectedPlace = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()


    fun loadAllSavedWeatherPlacesFromLocalDatabase() {
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

    fun executeRequestToGetWeatherDataForSelectedCity(apiKey: String, selectedWeatherResult: GeneralAndSpecificWeatherData){
        launch {
            flow {
                withContext(Dispatchers.IO){
                    var weatherResponse =  weatherService.getWeatherDetailsBasedOnUserLocation(apiKey, selectedWeatherResult.generalWeatherData.lat, selectedWeatherResult.generalWeatherData.lon)
                    withContext(Dispatchers.Main) {
                        if (weatherResponse.isSuccessful) {
                            emit(weatherResponse.body())
                        } else {
                            isWeatherUpdatedForSelectedPlace.value = false
                            errorMessage.value = weatherResponse.message()
                        }
                    }
                }
            }.collect { baseWeatherModel ->
                saveNewWeatherDataToLocalDatabase(baseWeatherModel)
            }
        }
    }

    fun executeRequestToGetWeatherDataByCityName(apiKey: String, cityName: String){
        launch {
            flow {
                withContext(Dispatchers.IO) {
                    var searchQueryWeatherResponse = weatherService.getWeatherDetailsBasedOnCity(apiKey, cityName)
                    withContext(Dispatchers.Main) {
                        if (searchQueryWeatherResponse.isSuccessful && searchQueryWeatherResponse.code() == 200) {
                            emit(searchQueryWeatherResponse.body())
                        } else {
                            isWeatherUpdatedForSelectedPlace.value = false
                            errorMessage.value = searchQueryWeatherResponse.message()
                        }
                    }
                }
            }.collect {baseWeatherModel ->
                saveNewWeatherDataToLocalDatabase(baseWeatherModel)
            }
        }
    }

    private fun saveNewWeatherDataToLocalDatabase(baseWeatherModel: BaseWeatherModel?) {
        launch {
            flow {
                var list: MutableList<GeneralAndSpecificWeatherData>? = null
                withContext(Dispatchers.IO) {
                    var generalWeatherData: GeneralWeatherData? = null
                    baseWeatherModel?.generalWeatherModel?.forEach { tempGeneralWeatherModel ->
                        generalWeatherData = tempGeneralWeatherModel
                    }
                    generalWeatherData.let {
                        val generalWeatherId = db.generalWeatherDataDao().insertGeneralWeatherData(it!!)
                        val weatherData = it.weather
                        weatherData.generalWeatherDataId = generalWeatherId.toInt()

                        db.weatherDao().insertWeatherData(it.weather)
                    }
                    list = db.generalWeatherDataDao().getWeatherData()
                }
                emit(list)
            }.collect { generalAndSpecificWeatherData ->
                isWeatherUpdatedForSelectedPlace.value = true
            }
        }
    }
}