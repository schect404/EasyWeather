package com.atitto.easyweather.presentation.map.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.concurrent.atomic.AtomicReference

interface MapManager {

    fun initMap(mapFragment: SupportMapFragment, callback: () -> Unit)
    fun goToCurrentPosition(currentCity: City)
    fun initPins(cities: List<City>, currentCity: City?)
    fun releaseMap()

}

class MapManagerImpl(private val context: Context): MapManager {

    private var map = AtomicReference<GoogleMap>(null)

    private fun attachMap(map: GoogleMap) = this.map.set(map)

    override fun releaseMap() = map.set(null)

    override fun initMap(mapFragment: SupportMapFragment, callback: () -> Unit) =
        mapFragment.getMapAsync {
            it.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map))
                clear()
            }.also { map ->
                attachMap(map)
                callback.invoke()
                mapFragment.context?.let {
                    val markerWindowAdapter = CustomMarkerInfoView(it)
                    map.setInfoWindowAdapter(markerWindowAdapter)
                }
            }
        }

    override fun goToCurrentPosition(currentCity: City) {
        currentCity.coords?.let {
            val googlePlex = CameraPosition.builder()
                .target(LatLng(it.lat, it.long))
                .zoom(ZOOM)
                .bearing(BEARING)
                .tilt(TILT)
                .build()

            map.get()?.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), ZOOM_SPEED, null)
        }
    }

    override fun initPins(cities: List<City>, currentCity: City?) {
        cities.forEach { city ->
            city.coords?.let {
                val options = MarkerOptions()
                    .position(LatLng(it.lat, it.long))
                    .title("${city.name} -- ${city.temperature}")

                val marker = map.get()?.addMarker(options)

                marker?.tag = city

                Glide.with(context)
                    .asBitmap()
                    .load(city.iconUrl)
                    .fitCenter()
                    .into(object : CustomTarget<Bitmap>(
                        ICON_SIZE,
                        ICON_SIZE
                    ) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            marker?.setIcon(BitmapDescriptorFactory.fromBitmap(resource))
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        }
    }

    companion object {
        private const val ZOOM = 10f
        private const val BEARING = 0f
        private const val TILT = 45f
        private const val ZOOM_SPEED = 10
        private const val ICON_SIZE = 100
    }

}