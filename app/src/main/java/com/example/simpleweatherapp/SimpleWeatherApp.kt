package com.example.simpleweatherapp

import android.app.Application

class SimpleWeatherApp: Application() {

    companion object{
        private var mInstance: SimpleWeatherApp? = null

        fun getSimpleWeatherAppInstance(): SimpleWeatherApp? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}