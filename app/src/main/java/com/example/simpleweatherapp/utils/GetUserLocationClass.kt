package com.example.simpleweatherapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.*

class GetUserLocationClass(var acitvity: Activity) {

    private lateinit var onGetUserCurrentLocationCommonClass: OnGetUserCurrentLocationCommonClass

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var locationManager: LocationManager? = null
    private var gotUserLocation = false

    init {
        initLocation()
    }

    interface OnGetUserCurrentLocationCommonClass {
        fun onCurrentUserLocationReceived(latitude: Double, longitude: Double)
    }

    fun initListener(onGetUserCurrentLocationCommonClass: OnGetUserCurrentLocationCommonClass) {
        this.onGetUserCurrentLocationCommonClass = onGetUserCurrentLocationCommonClass
    }

    fun isGPSEnabled() = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(acitvity)
        locationManager = acitvity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun getUserCurrentLocation() {
        //locationCallBackListener()
        locationRequestBuilder()
        when {
            isGPSEnabled() -> mFusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun removeLocationUpdate() {
        locationCallback?.let { mFusedLocationClient?.removeLocationUpdates(it) }
    }

    private fun locationRequestBuilder() {
        locationRequest = LocationRequest.create()
            .setInterval(1000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                if (!gotUserLocation) {
                    gotUserLocation = true
                    val latitude: Double? = p0?.locations?.get(0)?.latitude!!
                    val longitude: Double? = p0?.locations?.get(0)?.longitude!!
                    if (latitude != null && longitude != null){
                        onGetUserCurrentLocationCommonClass.onCurrentUserLocationReceived(latitude, longitude)
                    }
                    //Log.d("currentLocation", p0?.locations?.get(0)?.latitude.toString() + ", " + p0?.locations?.get(0)?.longitude)
                    removeLocationUpdate()
                }
            }
        }
    }
}