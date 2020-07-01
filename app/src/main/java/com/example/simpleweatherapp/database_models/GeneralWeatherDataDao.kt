package com.example.simpleweatherapp.database_models

import androidx.room.*

@Dao
interface GeneralWeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneralWeatherData(generalWeatherData: GeneralWeatherData): Long

    @Query("SELECT * FROM GeneralWeatherData")
    fun getAllGeneralWeatherData(): MutableList<GeneralWeatherData>

    @Query("SELECT DISTINCT city_name FROM GeneralWeatherData")
    fun getAllAvailableCityNames(): MutableList<String>

    @Transaction
    @Query("SELECT * FROM GeneralWeatherData")
    fun getWeatherData(): MutableList<GeneralAndSpecificWeatherData>?

    @Transaction
    @Query("SELECT * FROM GeneralWeatherData WHERE city_name = :selectedCityName")
    fun getWeatherDataByCityName(selectedCityName: String): MutableList<GeneralAndSpecificWeatherData>

}
