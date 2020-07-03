package com.example.simpleweatherapp

import android.app.Application
import com.example.simpleweatherapp.database_models.BaseWeatherModel
import com.example.simpleweatherapp.database_models.GeneralWeatherData
import com.example.simpleweatherapp.database_models.WeatherModel
import com.example.simpleweatherapp.services.WeatherApi
import com.example.simpleweatherapp.services.WeatherService
import com.example.simpleweatherapp.utils.Urls
import com.example.simpleweatherapp.view_model.MainPageViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class MainPageViewModelTest {

    private val application = RuntimeEnvironment.application
    private lateinit var weatherApi: WeatherApi
    @Mock
    lateinit var weatherService: WeatherService

    @Mock
    var mainPageViewModel = MainPageViewModel(application)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        weatherApi = Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Test
    fun getWeatherDetailsForCurrentCitySuccess() = runBlocking {
        val weatherModel = WeatherModel(2, "sunny.png", "c34t", "clear sky", 1)
        val generalWeatherData = GeneralWeatherData(1, "Limassol", "CY", 34.7071, 33.0226, 32.0, weatherModel, "2020-07-2 19:00:00")
        val weather = BaseWeatherModel(1, mutableListOf(generalWeatherData))

       // `when`(weatherApi.getWeatherDetailsBasedOnUserLocation("156b4168909644728132f265a8626cb3", 34.7071, 33.0226)).thenReturn(Response.success(weather))
        var t = mainPageViewModel.getWeatherDetailsForCurrentCity("156b4168909644728132f265a8626cb3", 34.7071, 33.0226)
        Assert.assertEquals(weather, mainPageViewModel.weatherSearchHistory.value)
    }
}
