package com.example.simpleweatherapp.view

import android.os.Bundle
import android.view.*
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.utils.Constants.GONE
import com.example.simpleweatherapp.utils.Constants.INVISIBLE
import com.example.simpleweatherapp.utils.Constants.VISIBLE
import com.example.simpleweatherapp.utils.changeVisibilityOfView
import com.example.simpleweatherapp.view_model.PlaceListViewModel
import kotlinx.android.synthetic.main.fragment_place_list.*


class PlaceListFragment : BaseFragment<PlaceListViewModel>(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndData()
    }

    private fun initViewAndData() {
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