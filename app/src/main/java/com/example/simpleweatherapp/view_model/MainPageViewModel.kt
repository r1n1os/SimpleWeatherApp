package com.example.simpleweatherapp.view_model

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.SimpleWeatherApp
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.*
import com.example.simpleweatherapp.services.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPageViewModel(application: Application) : BaseViewModel(application) {

     private val db by lazy { AppDatabase.getDatabase(getApplication()) }

    private var weatherService = WeatherService().getWeatherService()
     val weatherSearchHistory = MutableLiveData<List<GeneralWeatherData>>()

/*    init {
        DaggerApiComponent.create().inject(this)
    }*/

    fun getWeatherDetailsForCurrentCity(apiKey: String, latitude: Double, longitude: Double) {
        launch {
            flow<BaseWeatherModel> {
                withContext(Dispatchers.IO) {
                    val weatherResponse = weatherService.getWeatherDetailsBasedOnUserLocation(apiKey, latitude, longitude)
                    withContext(Dispatchers.Main) {
                        if (weatherResponse.isSuccessful) {
                            emit(weatherResponse.body()!!)
                        } else {
                            Log.d("sdasdas", "fail")
                        }
                    }
                }
            }.collect { baseWeatherModel ->
                saveWeatherDataIntoLocalDatabase(baseWeatherModel)
            }
        }
    }

    /**
     * The reason I am using for loop to get the object of weatherModel is because in order to parse data from the response jsonObject,
     * I have to parse the JsonArray that's why I am using list for the BaseWeatherModel. So I am using for instead of get index of 0 to avoid any case that might produce crash
     * */
    private fun saveWeatherDataIntoLocalDatabase(baseWeatherModel: BaseWeatherModel) {
         launch {
             flow {
                 var list = listOf<GeneralAndSpecificWeatherData>()
                 withContext(Dispatchers.IO) {
                     var generalWeatherData:  GeneralWeatherData? = null
                     baseWeatherModel.generalWeatherModel.forEach { tempGeneralWeatherModel ->
                         generalWeatherData = tempGeneralWeatherModel
                     }
                     generalWeatherData.let {
                         db.generalWeatherDataDao().insertGeneralWeatherData(it!!)
                         it.weather.let { weatherModel ->
                             db.weatherDao().insertWeatherData(weatherModel)
                         }
                     }
                     list = db.generalWeatherDataDao().getWeatherData()
                 }
                 emit(list)
             }.collect{generalAndSpecificWeatherData ->
                 generalAndSpecificWeatherData.forEach {tempGeneralAndSpecificWeatherData ->
                     Log.d("mTest", tempGeneralAndSpecificWeatherData.generalWeatherData.city_name)
                     tempGeneralAndSpecificWeatherData.weatherData.forEach { weather ->
                         Log.d("mTest", weather.description)
                     }
                 }
             }
         }
    }
}
