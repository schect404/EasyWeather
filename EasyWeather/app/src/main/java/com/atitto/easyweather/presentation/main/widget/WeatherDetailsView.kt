package com.atitto.easyweather.presentation.main.widget

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.atitto.domain.cities.model.WeatherDetails
import com.atitto.easyweather.BR
import com.atitto.easyweather.R
import com.atitto.easyweather.common.attachAdapter
import com.atitto.easyweather.common.loaded
import com.github.nitrico.lastadapter.LastAdapter

class WeatherDetailsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val items: ObservableList<WeatherDetails> = ObservableArrayList()

    private val adapter = LastAdapter(items, BR.item).map<WeatherDetails>(R.layout.item_weather_details)

    fun updateItems(newItems: List<WeatherDetails>) = items.loaded(newItems)

    init {
        inflate(context, R.layout.layout_weather_details, this)

        val list: RecyclerView = findViewById(R.id.rvWeather)
        list.attachAdapter(adapter)
    }

}