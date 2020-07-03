package com.example.simpleweatherapp

import android.app.Application
import com.example.simpleweatherapp.database_models.*
import com.example.simpleweatherapp.view_model.MainPageViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class AppDatabaseTest {

    private var generalWeatherDataDao: GeneralWeatherDataDao? = null
    private var weatherDao: WeatherDao? = null
    private val application = RuntimeEnvironment.application
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true
        db = AppDatabase.getDatabase(application)
        generalWeatherDataDao = db.generalWeatherDataDao()
        weatherDao = db.weatherDao()
    }

    @Test
    fun insertWeatherData() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)

        val weatherTestResult = weatherDao?.insertWeatherData(weatherModel)

        Assert.assertEquals(2L, weatherTestResult)
    }

    @Test
    fun insertGeneralWeatherDataSuccess() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")

        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData)

        val generalWeatherDataTest = generalWeatherDataDao?.getAllGeneralWeatherData()
        Assert.assertEquals(generalWeatherData.id, generalWeatherDataTest?.get(0)?.id)
    }

    @Test
    fun getAllGeneralWeatherDataSuccess() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val weatherModel2 = WeatherModel(2, "sunny.png", "c35t", "Raining", 2)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")
        val generalWeatherData2 = GeneralWeatherData(2, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel2, "2020-07-2 19:00:00")

        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData)
        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData2)

        val generalWeatherDataTest = generalWeatherDataDao?.getAllGeneralWeatherData()
        Assert.assertEquals(2, generalWeatherDataTest?.size)
    }

    @Test
    fun getAllAvailableCityNamesSuccess() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")
        val weatherModel2 = WeatherModel(2, "sunny.png", "c35t", "Raining", 2)
        val generalWeatherData2 = GeneralWeatherData(2, "Nicosia", "CY", 34.7071, 33.0226, 32.0, weatherModel2, "2020-07-2 19:00:00")

        val expectedResultList = mutableListOf<String>("Limassol", "Nicosia")
        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData)
        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData2)

        val generalWeatherDataTest = generalWeatherDataDao?.getAllAvailableCityNames()

        Assert.assertEquals(generalWeatherDataTest, expectedResultList)
    }

    @Test
    fun getWeatherDataByCityNameSuccess() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")
        val generalWeatherWithWeatherModel = mutableListOf<GeneralAndSpecificWeatherData>(GeneralAndSpecificWeatherData(generalWeatherData, weatherModel))

        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData)
        weatherDao?.insertWeatherData(weatherModel)

        val generalWeatherDataTest = generalWeatherDataDao?.getWeatherDataByCityName("Limassol")

        Assert.assertEquals(generalWeatherWithWeatherModel, generalWeatherDataTest)
    }

    @Test
    fun testGeneralAndSpecificWeatherDataRelationShip() {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")

        generalWeatherDataDao?.insertGeneralWeatherData(generalWeatherData)
        weatherDao?.insertWeatherData(weatherModel)

        val testResultData = generalWeatherDataDao?.getWeatherDataByCityName("Limassol")

        Assert.assertEquals(1, testResultData?.get(0)?.generalWeatherData?.id)
        Assert.assertEquals(2, testResultData?.get(0)?.weatherData?.weatherModelId)
    }
}