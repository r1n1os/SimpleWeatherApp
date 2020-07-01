package com.example.simpleweatherapp.view

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.adapters.MainPageRecyclerViewAdapter
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.utils.Constants.GONE
import com.example.simpleweatherapp.utils.Constants.INVISIBLE
import com.example.simpleweatherapp.utils.Constants.VISIBLE
import com.example.simpleweatherapp.utils.changeVisibilityOfView
import com.example.simpleweatherapp.view_model.PlaceListViewModel
import kotlinx.android.synthetic.main.fragment_place_list.*


class PlaceListFragment : BaseFragment<PlaceListViewModel>(), View.OnClickListener {

    private var adapter: MainPageRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData()
    }

    private fun initViewAndData() {
        initAdapter()
        initListeners()
        executeLocalRequestForSavedWeatherPlaces()
        observedSavePlaces()
    }

    private fun initAdapter() {
        adapter = MainPageRecyclerViewAdapter()
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

    private fun initListeners() {
        searchIcon.setOnClickListener(this)
        cancelSearchViewArrow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            searchIcon -> enableSearch()
            cancelSearchViewArrow -> disableSearch()
        }
    }

    private fun enableSearch() {
        citySearchView.changeVisibilityOfView(VISIBLE)
        cancelSearchViewArrow.changeVisibilityOfView(VISIBLE)
        searchIcon.changeVisibilityOfView(INVISIBLE)
        mainPageTitle.changeVisibilityOfView(GONE)
    }

    private fun disableSearch(){
        citySearchView.changeVisibilityOfView(INVISIBLE)
        cancelSearchViewArrow.changeVisibilityOfView(INVISIBLE)
        searchIcon.changeVisibilityOfView(VISIBLE)
        mainPageTitle.changeVisibilityOfView(VISIBLE)
    }

    override fun initViewModel() = PlaceListViewModel::class.java
}