package com.example.simpleweatherapp.database_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GeneralWeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city_name: String,
    val lat: Double,
    val lon: Double,
    val temp: Double,
    val weather: WeatherModel,
    @SerializedName("datetime")
    val dateTime: String
)
