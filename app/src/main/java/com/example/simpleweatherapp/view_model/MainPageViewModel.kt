package com.example.simpleweatherapp.view_model

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simpleweatherapp.base_classes.BaseViewModel
import com.example.simpleweatherapp.database_models.AppDatabase
import com.example.simpleweatherapp.database_models.BaseWeatherModel
import com.example.simpleweatherapp.database_models.TestDB
import com.example.simpleweatherapp.database_models.WeatherModel
import com.example.simpleweatherapp.services.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPageViewModel(application: Application) : BaseViewModel(application) {

    private var weatherService = WeatherService().getWeatherService()
    val weatherSearchHistory = MutableLiveData<List<WeatherModel>>()

   /* private val db by lazy { WeatherDatabase(getApplication()).weatherDao() }
*/

/*    init {
        DaggerApiComponent.create().inject(this)
    }*/

    fun checkIfPermissionsAreGranted(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //getWeatherDetailsForCurrentCity()
        } else {
            //jobDetailsView.onPermissionsDenied()
        }
    }

    fun getWeatherDetailsForCurrentCity(apiKey: String, latitude: Double, longitude: Double) {
        launch {
            flow<BaseWeatherModel> {
                withContext(Dispatchers.IO) {
                    var db = AppDatabase.getDatabase(this@MainPageViewModel.getApplication())
                        .baseWeatherDao()
                    db.insertAll(TestDB(1, "EVag", "FRag"))
                    Log.d("dfdfdf", db.getAll().size.toString())
                }
                    val weatherResponse =
                        weatherService.getWeatherDetailsBasedOnUserLocation(
                            apiKey,
                            latitude,
                            longitude
                        )
                    Log.d("sdasdas", weatherResponse.headers().toString())
                    if (weatherResponse.isSuccessful) {
                        emit(weatherResponse.body()!!)
                    } else {
                        Log.d("sdasdas", "fail")
                    }
                }.collect {
                    Log.d("sdasdas", it.weatherModel.toString())

                    val list = mutableListOf<WeatherModel>()
                    list.add(it.weatherModel[0])
                    weatherSearchHistory.value = list
                }
            }
        }
    }
