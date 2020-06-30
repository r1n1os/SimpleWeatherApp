package com.example.simpleweatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.database_models.GeneralAndSpecificWeatherData
import kotlinx.android.synthetic.main.weather_data_item_layout.view.*

class MainPageRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var generalAndSpecificWeatherDataList = mutableListOf<GeneralAndSpecificWeatherData>()

    fun loadData(list: MutableList<GeneralAndSpecificWeatherData>){
        generalAndSpecificWeatherDataList.clear()
        generalAndSpecificWeatherDataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WeatherDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_data_item_layout, parent, false))

    override fun getItemCount() = generalAndSpecificWeatherDataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is WeatherDataViewHolder -> holder.onBindData(generalAndSpecificWeatherDataList[position])
        }
    }

    inner class WeatherDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun onBindData(generalAndSpecificWeatherData: GeneralAndSpecificWeatherData) {
            itemView.cityNameTextView.text = generalAndSpecificWeatherData.generalWeatherData.city_name
            itemView.weatherTemperatureTextView.text = generalAndSpecificWeatherData.generalWeatherData.temp.toString()
        }
    }
}