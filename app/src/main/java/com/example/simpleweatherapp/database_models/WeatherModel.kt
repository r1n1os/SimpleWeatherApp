package com.example.simpleweatherapp.database_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val icon: String,
    val code: String,
    val description: String,
    val generalWeatherDataId: Int
)

