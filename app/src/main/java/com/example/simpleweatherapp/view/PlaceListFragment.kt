package com.example.simpleweatherapp.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.adapters.WeatherRecyclerViewAdapter
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import com.example.simpleweatherapp.utils.Constants.GONE
import com.example.simpleweatherapp.utils.Constants.INVISIBLE
import com.example.simpleweatherapp.utils.Constants.VISIBLE
import com.example.simpleweatherapp.utils.changeVisibilityOfView
import com.example.simpleweatherapp.view_model.PlaceListViewModel
import kotlinx.android.synthetic.main.fragment_place_list.*


class PlaceListFragment : BaseFragment<PlaceListViewModel>(), View.OnClickListener, WeatherRecyclerViewAdapter.OnWeatherAdapterListener, SearchView.OnQueryTextListener {

    private var adapter: WeatherRecyclerViewAdapter? = null
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initViewAndData()
    }

    private fun initViewAndData() {
        initAdapter()
        initListeners()
        executeLocalRequestForSavedWeatherPlaces()
        observedSavePlaces()
        observedWeatherUpdateForSelectedPlace()
        observedErrorMessages()
    }

    private fun initAdapter() {
        adapter = WeatherRecyclerViewAdapter(this)
        savedPlacesRecyclerView.adapter = adapter
    }

    private fun executeLocalRequestForSavedWeatherPlaces() {
        showProgressDialog()
        viewModel.loadAllSavedWeatherPlacesFromLocalDatabase()
    }

    private fun observedSavePlaces() {
        hideProgressDialog()
        viewModel.savedWeatherPlacesList.observe(this.requireActivity(), Observer { savedWeatherPlacesList ->
           adapter?.loadData(savedWeatherPlacesList)
        })
    }

    private fun observedWeatherUpdateForSelectedPlace() {
        viewModel.isWeatherUpdatedForSelectedPlace.observe(this.requireActivity(), Observer { isSuccess ->
            hideProgressDialog()
            if (isSuccess){
                navController.navigate(R.id.action_placeListFragment_to_mainPageFragment)
            } else {
                showAlertDialog("", getString(R.string.something_went_wrong), DialogInterface.OnClickListener { dialog, which ->  })
            }
        })
    }

    private fun observedErrorMessages() {
        viewModel.errorMessage.observe(this.requireActivity(), Observer { errorMessage ->
            hideProgressDialog()
            showAlertDialog("", errorMessage, DialogInterface.OnClickListener { dialog, which ->

            })
        })
    }

    private fun initListeners() {
        searchIcon.setOnClickListener(this)
        cancelSearchViewArrow.setOnClickListener(this)
        citySearchView.setOnQueryTextListener(this)
        //backArrowIcon.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            searchIcon -> enableSearch()
            cancelSearchViewArrow -> disableSearch()
            //backArrowIcon -> navController.popBackStack()
        }
    }

    private fun enableSearch() {
        citySearchView.changeVisibilityOfView(VISIBLE)
        cancelSearchViewArrow.changeVisibilityOfView(VISIBLE)
        searchIcon.changeVisibilityOfView(INVISIBLE)
        mainPageTitle.changeVisibilityOfView(GONE)
        //backArrowIcon.changeVisibilityOfView(GONE)
    }

    private fun disableSearch(){
        citySearchView.changeVisibilityOfView(INVISIBLE)
        cancelSearchViewArrow.changeVisibilityOfView(INVISIBLE)
        searchIcon.changeVisibilityOfView(VISIBLE)
        mainPageTitle.changeVisibilityOfView(VISIBLE)
        //backArrowIcon.changeVisibilityOfView(VISIBLE)
    }

    /**
     * SearchView listeners
     * */
    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.executeRequestToGetWeatherDataByCityName(getString(R.string.weather_api_key), query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean { return true }
    /**
     * WeatherRecyclerViewAdapter Listeners
     * */
    override fun onWeatherResultClicked(selectedWeatherResult: GeneralAndSpecificWeatherData) {
        showProgressDialog()
        viewModel.executeRequestToGetWeatherDataForSelectedCity(getString(R.string.weather_api_key), selectedWeatherResult)
    }

    override fun initViewModel() = PlaceListViewModel::class.java
}