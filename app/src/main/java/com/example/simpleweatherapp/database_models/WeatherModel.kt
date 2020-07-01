package com.example.simpleweatherapp.database_models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
data class WeatherModel(
        @PrimaryKey(autoGenerate = true)
        var weatherModelId: Int,
        var icon: String,
        var code: String,
        var description: String,
        var generalWeatherDataId: Int
)

