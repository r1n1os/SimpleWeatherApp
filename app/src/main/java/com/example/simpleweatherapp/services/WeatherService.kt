package com.example.simpleweatherapp.services

import com.example.simpleweatherapp.utils.Urls.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {

  /*  @Inject
    lateinit var weatherApi: WeatherApi

    init {
        DaggerApiComponent.create().inject(this)
    }
*/
    fun getWeatherService(): WeatherApi{
      return Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(WeatherApi::class.java)
  }
}