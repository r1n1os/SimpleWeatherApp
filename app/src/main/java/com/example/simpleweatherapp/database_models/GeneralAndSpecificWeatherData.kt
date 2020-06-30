
package com.example.simpleweatherapp.database_models

import androidx.room.Embedded
import androidx.room.Relation

data class GeneralAndSpecificWeatherData(
    @Embedded
    val generalWeatherData: GeneralWeatherData,
    @Relation(
        parentColumn = "id",
        entityColumn = "generalWeatherDataId"
    )
    val weatherData: List<WeatherModel>
)
