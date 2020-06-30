package com.example.simpleweatherapp.database_models

import androidx.room.*

@Dao
interface GeneralWeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeneralWeatherData(vararg generalWeatherData: GeneralWeatherData)

    @Transaction
    @Query("SELECT * FROM GeneralWeatherData")
    fun getWeatherData(): List<GeneralAndSpecificWeatherData>

}
