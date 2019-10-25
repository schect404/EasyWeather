package com.atitto.easyweather.binding

import androidx.databinding.BindingAdapter
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.atitto.domain.cities.model.WeatherDailyDetails
import com.atitto.domain.cities.model.WeatherDetails
import com.atitto.easyweather.R
import com.atitto.easyweather.presentation.main.widget.WeatherDetailsView
import com.atitto.easyweather.presentation.main.widget.daily.WeatherDailyDetailsView
import com.bumptech.glide.Glide

object CitiesBinding {

    @JvmStatic
    @BindingAdapter("app:isMy")
    fun formatDate(layout: RelativeLayout, isMy: Boolean) {
        val color = if(isMy) layout.context.getColor(R.color.colorSecondary) else Color.WHITE
        layout.setBackgroundColor(color)
    }

    @JvmStatic
    @BindingAdapter("app:icon")
    fun loadIcon(view: ImageView, icon: String?) {
        Glide.with(view)
            .load(icon)
            .fitCenter()
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("app:dailyDetails")
    fun loadDailyDetails(view: WeatherDailyDetailsView, details: List<WeatherDailyDetails>?) {
        details?.let { view.updateItems(details) }
    }

    @JvmStatic
    @BindingAdapter("app:details")
    fun loadDetails(view: WeatherDetailsView, details: List<WeatherDetails>?) {
        details?.let { view.updateItems(details) }
    }

    @JvmStatic
    @BindingAdapter("app:dailyVisible")
    fun handleDailyVisibility(view: WeatherDailyDetailsView, isVisible: Boolean) {
        view.visibility = if(isVisible) View.VISIBLE else View.GONE
        val list: RecyclerView = view.findViewById(R.id.rvWeather)

    }

    @JvmStatic
    @BindingAdapter("app:visible")
    fun handleVisibility(view: WeatherDetailsView, isVisible: Boolean) {
        view.visibility = if(isVisible) View.VISIBLE else View.GONE
        val list: RecyclerView = view.findViewById(R.id.rvWeatherDetails)
        list.scheduleLayoutAnimation()
    }

    @JvmStatic
    @BindingAdapter("app:visible")
    fun handleVisibility(view: ImageView, isVisible: Boolean) {
        view.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:background")
    fun loadIcon(view: RelativeLayout, isActive: Boolean) {
        if(isActive) view.setBackgroundColor(view.context.getColor(R.color.colorSecondary)) else view.setBackgroundColor(Color.WHITE)
    }

}