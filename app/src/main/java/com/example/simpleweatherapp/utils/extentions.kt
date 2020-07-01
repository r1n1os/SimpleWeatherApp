package com.example.simpleweatherapp.utils

import android.view.View
import com.example.simpleweatherapp.utils.Constants.GONE
import com.example.simpleweatherapp.utils.Constants.INVISIBLE
import com.example.simpleweatherapp.utils.Constants.VISIBLE

fun View.changeVisibilityOfView(visibilityType: Int){
    when(visibilityType){
        VISIBLE -> this.visibility = View.VISIBLE
        INVISIBLE -> this.visibility = View.INVISIBLE
        GONE -> this.visibility = View.GONE
    }
}