package com.example.simpleweatherapp.database_models

import com.google.gson.annotations.SerializedName

/*


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class BaseWeatherModel(
   @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("count")
    val count: Int
)*/
data class BaseWeatherModel(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val weatherModel: List<WeatherModel>
)
