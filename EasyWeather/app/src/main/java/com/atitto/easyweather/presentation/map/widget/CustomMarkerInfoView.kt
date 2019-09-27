package com.atitto.easyweather.presentation.map.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.marker_info_window.view.*

class CustomMarkerInfoView(private val context: Context): GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View? = null

    override fun getInfoWindow(marker: Marker?): View {
        val layout = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null)
        val city = marker?.tag as? City ?: return layout
        layout.tvCity.text = city.name
        layout.tvTemperature.text = city.temperature
        return layout
    }

}