package com.example.simpleweatherapp.database_models

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WeatherDao {

    @Insert
    fun insertWeatherData(weatherModel: WeatherModel)
}