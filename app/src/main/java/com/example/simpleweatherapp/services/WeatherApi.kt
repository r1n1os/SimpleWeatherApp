package com.example.simpleweatherapp.services

import com.example.simpleweatherapp.database_models.BaseWeatherModel
import com.example.simpleweatherapp.utils.Constants.CITY
import com.example.simpleweatherapp.utils.Constants.LAT
import com.example.simpleweatherapp.utils.Constants.LON
import com.example.simpleweatherapp.utils.Urls.GET_CURRENT_WEATHER_FOR_SPECIFIC_CITY_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(GET_CURRENT_WEATHER_FOR_SPECIFIC_CITY_URL)
    suspend fun getWeatherDetailsBasedOnUserLocation(@Query("key")apiKey: String, @Query(LAT) lat: Double, @Query(LON) lon: Double): Response<BaseWeatherModel>

    @GET(GET_CURRENT_WEATHER_FOR_SPECIFIC_CITY_URL)
    suspend fun getWeatherDetailsBasedOnCity(@Query("key")apiKey: String, @Query(CITY) cityName: String): Response<BaseWeatherModel>
}