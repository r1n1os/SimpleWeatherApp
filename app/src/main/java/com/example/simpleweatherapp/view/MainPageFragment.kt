package com.example.simpleweatherapp.view

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.adapters.WeatherRecyclerViewAdapter
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.utils.GetUserLocationClass
import com.example.simpleweatherapp.utils.PermissionsHelperClass
import com.example.simpleweatherapp.view_model.MainPageViewModel
import kotlinx.android.synthetic.main.fragment_main_page.*

class MainPageFragment : BaseFragment<MainPageViewModel>(), PermissionsHelperClass.OnPermissionListener,
        GetUserLocationClass.OnGetUserCurrentLocationCommonClass, AdapterView.OnItemSelectedListener, View.OnClickListener, WeatherRecyclerViewAdapter.OnWeatherAdapterListener {

    companion object {
        //permission
        const val LOCATION_PERMISSIONS = Manifest.permission.ACCESS_FINE_LOCATION
        const val LOCATION_PERMISSIONS_REQUEST_CODE = 31
    }

    private lateinit var navController: NavController
    private var adapter: WeatherRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initViewAndData()
    }

    private fun initViewAndData() {
        requestForLocationPermissions()
        initAdapter()
        initListeners()
        observeWeather()
        observeListOfAvailableCityNames()
    }

    private fun initListeners() {
        mainPageSpinner.onItemSelectedListener = this
        savedPLacesImageView.setOnClickListener(this)
    }

    private fun initAdapter() {
        adapter = WeatherRecyclerViewAdapter(this)
        weatherHistoryRecyclerView.adapter = adapter
    }

    private fun observeWeather() {
        viewModel.weatherSearchHistory.observe(this.requireActivity(), Observer { weatherHistory ->
            weatherHistory?.let {
                adapter?.loadData(it.sortedBy { it.generalWeatherData.id }.toMutableList())
                hideProgressDialog()
            }
        })
    }

    private fun observeListOfAvailableCityNames() {
        viewModel.cityNames.observe(this.requireActivity(), Observer { listOfCityNames ->
            setCityNamesToSpinner(listOfCityNames)
        })
    }

    private fun setCityNamesToSpinner(listOfCityNames: MutableList<String>?) {
        val spinnerAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, listOfCityNames as List<String>)
        mainPageSpinner.setSelection(0, false)
        mainPageSpinner.adapter = spinnerAdapter
    }

    private fun requestForLocationPermissions() {
        val permissionsHelperClass = PermissionsHelperClass()
        permissionsHelperClass.checkPermissions(this.requireContext(), LOCATION_PERMISSIONS, this)
    }

    private fun requestLocationPermissions() {
        requestPermissions(arrayOf(LOCATION_PERMISSIONS), LOCATION_PERMISSIONS_REQUEST_CODE)
    }

    private fun handleRequestPermissions(grantResults: IntArray) {
        when (grantResults[0]) {
            PackageManager.PERMISSION_GRANTED -> getUserCurrentLocation()
            else -> showAlertDialogToExplainReasonOfPermissionRequired()
        }
    }

    private fun getUserCurrentLocation() {
        val userCurrentLocationClass = GetUserLocationClass(this.requireActivity())
        userCurrentLocationClass.getUserCurrentLocation()
        userCurrentLocationClass.initListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            savedPLacesImageView -> navController.navigate(R.id.action_mainPageFragment_to_placeListFragment)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.handleCitySelection(parent?.getItemAtPosition(position) as String)
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    /**
     * PermissionsHelperClass Listeners
     * */
    override fun onPermissionGranted() {
        showProgressDialog()
        getUserCurrentLocation()
    }

    override fun onPermissionRequestNeeded() {
        requestLocationPermissions()
    }

    override fun onPermissionDenied() {
        showAlertDialogToExplainReasonOfPermissionRequired()
    }

    private fun showAlertDialogToExplainReasonOfPermissionRequired() {
        showAlertDialog("", getString(R.string.reason_app_needs_location_permissions), DialogInterface.OnClickListener { dialog, which ->

        })
    }

    /**
     * GetUserLocationClass Listener
     * */
    override fun onCurrentUserLocationReceived(latitude: Double, longitude: Double) {
        viewModel.getWeatherDetailsForCurrentCity(getString(R.string.weather_api_key), latitude, longitude)
    }

    override fun initViewModel() = MainPageViewModel::class.java

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> handleRequestPermissions(grantResults)
        }
    }
}