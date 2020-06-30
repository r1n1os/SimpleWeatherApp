package com.example.simpleweatherapp.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.utils.GetUserLocationClass
import com.example.simpleweatherapp.utils.PermissionsHelperClass
import com.example.simpleweatherapp.view_model.MainPageViewModel

class MainPageFragment : BaseFragment<MainPageViewModel>(), PermissionsHelperClass.OnPermissionListener,
    GetUserLocationClass.OnGetUserCurrentLocationCommonClass {

    companion object {
        //permission
        const val LOCATION_PERMISSIONS = Manifest.permission.ACCESS_COARSE_LOCATION
        const val LOCATION_PERMISSIONS_REQUEST_CODE = 31
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData()
    }

    private fun initViewAndData() {
        requestForLocationPermissions()
        observeWeather()
    }

    private fun observeWeather() {
        viewModel.weatherSearchHistory.observe(this.requireActivity(), Observer { weatherHistory ->
            weatherHistory?.let {
                Log.d("WeatherREsult", it.size.toString())
            }
        })
    }

    private fun requestForLocationPermissions() {
        val permissionsHelperClass = PermissionsHelperClass()
        permissionsHelperClass.checkPermissions(this.requireContext(), LOCATION_PERMISSIONS, this)
    }

    private fun handleRequestPermissions(grantResults: IntArray) {
        viewModel.checkIfPermissionsAreGranted(grantResults)
    }

    private fun getUserCurrentLocation() {
        val userCurrentLocationClass = GetUserLocationClass(this.requireActivity())
        userCurrentLocationClass.getUserCurrentLocation()
        userCurrentLocationClass.initListener(this)
    }

    /**
     * PermissionsHelperClass Listeners
     * */
    override fun onPermissionGranted() {
        getUserCurrentLocation()
    }

    override fun onPermissionRequestNeeded() {
    }

    override fun onPermissionDeniedWithDontShowAgain() {
    }

    /**
     * GetUserLocationClass Listener
     * */
    override fun onCurrentUserLocationReceived(latitude: Double, longitude: Double) {
        viewModel.getWeatherDetailsForCurrentCity(getString(R.string.weather_api_key),latitude, longitude)
    }
    override fun initViewModel() = MainPageViewModel::class.java

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> handleRequestPermissions(grantResults)
        }
    }
}