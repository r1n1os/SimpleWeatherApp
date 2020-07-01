package com.example.simpleweatherapp.database_models

import androidx.room.*

@Dao
interface GeneralWeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneralWeatherData(vararg generalWeatherData: GeneralWeatherData)

    @Query("SELECT * FROM GeneralWeatherData")
    fun getAllGeneralWeatherData(): MutableList<GeneralWeatherData>

    @Query("SELECT DISTINCT city_name FROM GeneralWeatherData")
    fun getAllAvailableCityNames(): MutableList<String>

    @Transaction
    @Query("SELECT * FROM GeneralWeatherData")
    fun getWeatherData(): MutableList<GeneralAndSpecificWeatherData>?

}
