package com.example.simpleweatherapp.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.SimpleWeatherApp
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.*
import com.example.simpleweatherapp.services.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPageViewModel(application: Application) : BaseViewModel(application) {

    private val db by lazy { AppDatabase.getDatabase(getApplication()) }

    private var weatherService = WeatherService().getWeatherService()
    val weatherSearchHistory = MutableLiveData<MutableList<GeneralAndSpecificWeatherData>>()
    val cityNames = MutableLiveData<MutableList<String>>()
    val requestFailed = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    /*init {
        DaggerApiComponent.create().inject(this)
    }*/

    fun getWeatherDetailsForCurrentCity(apiKey: String, latitude: Double, longitude: Double) {
        launch {
            flow<BaseWeatherModel> {
                withContext(Dispatchers.IO) {
                    val weatherResponse = weatherService.getWeatherDetailsBasedOnUserLocation(apiKey, latitude, longitude)
                    withContext(Dispatchers.Main) {
                        if (weatherResponse.isSuccessful && weatherResponse.code() == 200) {
                            emit(weatherResponse.body()!!)
                        } else {
                            errorMessage.value = weatherResponse.message()
                        }
                    }
                }
            }.catch { error ->
                when (error.localizedMessage) {
                    SimpleWeatherApp.getSimpleWeatherAppInstance()?.getString(R.string.localize_error_message_for_no_internet_connection) -> errorMessage.value = SimpleWeatherApp.getSimpleWeatherAppInstance()?.getString(R.string.no_internet_connection)
                    else -> errorMessage.value = error.localizedMessage
                }
            }.collect { baseWeatherModel ->
                saveWeatherDataIntoLocalDatabase(baseWeatherModel)
            }
        }
    }

    fun handleCitySelection(selectedCity: String) {
        launch {
            flow {
                var weatherHistoryForSelectedCity = mutableListOf<GeneralAndSpecificWeatherData>()
                withContext(Dispatchers.IO) {
                    weatherHistoryForSelectedCity = if (selectedCity == SimpleWeatherApp.getSimpleWeatherAppInstance()?.getString(R.string.all_cities)) {
                        db.generalWeatherDataDao().getWeatherData()!!
                    } else {
                        db.generalWeatherDataDao().getWeatherDataByCityName(selectedCity)
                    }
                }
                emit(weatherHistoryForSelectedCity)
            }.catch { error ->
                errorMessage.value = error.localizedMessage
            }.collect {
                weatherSearchHistory.value = it
            }
        }
    }

    private fun saveWeatherDataIntoLocalDatabase(baseWeatherModel: BaseWeatherModel) {
        launch {
            flow {
                var list: MutableList<GeneralAndSpecificWeatherData>? = null
                withContext(Dispatchers.IO) {
                    var generalWeatherData: GeneralWeatherData? = null
                    baseWeatherModel.generalWeatherModel.forEach { tempGeneralWeatherModel ->
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
                getAllCityNamesFromWeatherHistory()
                emit(list)
            }.collect { generalAndSpecificWeatherData ->
                weatherSearchHistory.value = generalAndSpecificWeatherData
            }
        }
    }

    fun getAllCityNamesFromWeatherHistory() {
        launch {
            flow {
                var cityNamesList = mutableListOf<String>()
                withContext(Dispatchers.IO) {
                    cityNamesList = db.generalWeatherDataDao().getAllAvailableCityNames()
                }
                emit(cityNamesList)
            }.collect { cityNameList ->
                val tempListWithCityNames = cityNameList
                tempListWithCityNames.add(0, SimpleWeatherApp.getSimpleWeatherAppInstance()?.getString(R.string.all_cities)!!)
                cityNames.value = tempListWithCityNames
            }
        }
    }
}

