package com.atitto.easyweather.presentation.main.widget.daily

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.atitto.domain.cities.model.WeatherDailyDetails
import com.atitto.easyweather.BR
import com.atitto.easyweather.R
import com.atitto.easyweather.common.AsyncObservableList
import com.atitto.easyweather.common.attachAdapter
import com.atitto.easyweather.databinding.ItemWeatherDailyDetailsBinding
import com.github.nitrico.lastadapter.LastAdapter

class WeatherDailyDetailsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val items: AsyncObservableList<WeatherDailyDetails> = AsyncObservableList()

    private val adapter = LastAdapter(items, BR.item).map<WeatherDailyDetails, ItemWeatherDailyDetailsBinding>(R.layout.item_weather_daily_details) {
        onClick { it.binding.item?.let { onClick(it) } }
    }

    var onClick: (WeatherDailyDetails) -> Unit = {}

    fun updateItems(newItems: List<WeatherDailyDetails>) = items.update(newItems)

    init {
        inflate(context, R.layout.layout_weather_daily_details, this)

        val list: RecyclerView = findViewById(R.id.rvWeather)
        list.attachAdapter(adapter)
    }

}