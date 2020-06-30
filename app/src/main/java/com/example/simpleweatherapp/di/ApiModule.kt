package com.example.simpleweatherapp.di

import com.example.simpleweatherapp.services.WeatherApi
import com.example.simpleweatherapp.services.WeatherService
import com.example.simpleweatherapp.utils.Urls.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/*
@Module
class ApiModule {

    @Provides
    fun provideWeatherApi(): WeatherApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    fun provideWeatherService(): WeatherService{
        return WeatherService()
    }
}*/
