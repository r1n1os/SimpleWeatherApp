package com.example.simpleweatherapp.database_models
import com.google.gson.annotations.SerializedName

data class BaseWeatherModel(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val generalWeatherModel: MutableList<GeneralWeatherData>
)
