package com.example.simpleweatherapp.database_models.type_converters

import androidx.room.TypeConverter
import com.example.simpleweatherapp.database_models.WeatherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherModelTypeConverter {

    @TypeConverter
    fun fromWeatherDataModel(weatherModel: WeatherModel?): String? {
        return if (weatherModel == null){
            null
        } else {
            val gson = Gson()
            val type = object : TypeToken<WeatherModel>(){}.type
            val json = gson.toJson(weatherModel, type)
            json
        }
    }

    @TypeConverter
    fun toWeatherDataModel(weatherModelString: String?): WeatherModel? {
        return if (weatherModelString == null){
            null
        } else {
            val gson = Gson()
            val type = object : TypeToken<WeatherModel>(){}.type
            val json = gson.fromJson<WeatherModel>(weatherModelString, type)
            json
        }
    }
}