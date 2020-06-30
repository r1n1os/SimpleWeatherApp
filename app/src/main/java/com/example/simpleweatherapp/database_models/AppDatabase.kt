
package com.example.simpleweatherapp.database_models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpleweatherapp.database_models.type_converters.WeatherModelTypeConverter

@Database(entities = [GeneralWeatherData::class, WeatherModel::class], version = 1)
@TypeConverters(WeatherModelTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun generalWeatherDataDao(): GeneralWeatherDataDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Weather_database"
                ) .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}



