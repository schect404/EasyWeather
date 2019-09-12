package com.atitto.easyweather.presentation.map


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

private const val CITIES_ARGS = "CITIES"
private const val CURRENT_CITY_ARGS = "CURRENT_CITY"

class MapFragment : Fragment() {

    private val cities: ArrayList<City> = ArrayList()
    private var currentCity: City? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cities.addAll(arguments?.getParcelableArrayList(CITIES_ARGS) ?: listOf())
        currentCity = arguments?.getParcelable(CURRENT_CITY_ARGS)
        val map = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        map?.getMapAsync { myMap ->

            myMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            myMap.clear()

            currentCity?.coords?.let {
                val googlePlex = CameraPosition.builder()
                    .target(LatLng(it.lat, it.long))
                    .zoom(10f)
                    .bearing(0f)
                    .tilt(45f)
                    .build()

                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10, null)
            }

            makeMarkers(myMap)

        }
    }

    private fun makeMarkers(myMap: GoogleMap) {

        cities.forEach { city ->
            city.coords?.let {
                val options = MarkerOptions()
                    .position(LatLng(it.lat, it.long))
                    .title("${city.name} -- ${city.temperature}")

                val marker = myMap.addMarker(options)

                Glide.with(this)
                    .asBitmap()
                    .load(city.iconUrl)
                    .fitCenter()
                    .into(object : CustomTarget<Bitmap>(ICON_SIZE, ICON_SIZE) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(resource))
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })

                if (city.name == currentCity?.name) marker.showInfoWindow()
            }
        }

    }

    companion object {
        private const val ICON_SIZE = 100
        fun newInstance() = MapFragment()
    }

}
