package com.atitto.easyweather.binding

import android.databinding.BindingAdapter
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.atitto.domain.cities.model.WeatherDetails
import com.atitto.easyweather.R
import com.atitto.easyweather.presentation.main.widget.WeatherDetailsView
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
    @BindingAdapter("app:details")
    fun loadDetails(view: WeatherDetailsView, details: List<WeatherDetails>?) {
        details?.let { view.updateItems(details) }
    }

    @JvmStatic
    @BindingAdapter("app:visible")
    fun handleVisibility(view: WeatherDetailsView, isVisible: Boolean) {
        view.visibility = if(isVisible) View.VISIBLE else View.GONE
        val list: RecyclerView = view.findViewById(R.id.rvWeather)
        list.scheduleLayoutAnimation()
    }

}