package com.example.simpleweatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.base_classes.BaseFragment
import com.example.simpleweatherapp.view_model.PlaceListViewModel


class PlaceListFragment : BaseFragment<PlaceListViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_list, container, false)
    }

    override fun initViewModel() = PlaceListViewModel::class.java
}