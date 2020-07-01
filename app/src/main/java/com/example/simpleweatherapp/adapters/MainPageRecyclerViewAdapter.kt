package com.example.simpleweatherapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import com.example.simpleweatherapp.utils.Urls.BASE_ICON_URL
import kotlinx.android.synthetic.main.weather_data_item_layout.view.*

class MainPageRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var generalAndSpecificWeatherDataList = mutableListOf<GeneralAndSpecificWeatherData>()

    fun loadData(list: MutableList<GeneralAndSpecificWeatherData>) {
        generalAndSpecificWeatherDataList.clear()
        generalAndSpecificWeatherDataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WeatherDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_data_item_layout, parent, false))

    override fun getItemCount() = generalAndSpecificWeatherDataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WeatherDataViewHolder -> holder.onBindData(generalAndSpecificWeatherDataList[position])
        }
    }

    inner class WeatherDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun onBindData(generalAndSpecificWeatherData: GeneralAndSpecificWeatherData) {
            itemView.cityNameTextView.text = "${generalAndSpecificWeatherData.generalWeatherData.city_name}, ${generalAndSpecificWeatherData.generalWeatherData.country_code}"
            itemView.weatherTemperatureTextView.text = generalAndSpecificWeatherData.generalWeatherData.temp.toString()
            itemView.weatherDescriptionTextView.text = generalAndSpecificWeatherData.generalWeatherData.weather.description
            var url = "$BASE_ICON_URL${generalAndSpecificWeatherData.generalWeatherData.weather.icon}.png"
            Log.d("dfdsf", url)
            Glide.with(itemView)
                .load(url)
                .into(itemView.weatherStatusIcon)
        }
    }
}