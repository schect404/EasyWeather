package com.atitto.easyweather.presentation.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import com.atitto.easyweather.presentation.map.widget.MapManager
import com.google.android.gms.maps.SupportMapFragment
import org.koin.android.ext.android.get

private const val CITIES_ARGS = "CITIES"
private const val CURRENT_CITY_ARGS = "CURRENT_CITY"

class MapFragment : Fragment() {

    private val cities: ArrayList<City> = ArrayList()
    private var currentCity: City? = null

    private val mapManager: MapManager = get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cities.addAll(arguments?.getParcelableArrayList(CITIES_ARGS) ?: listOf())
        currentCity = arguments?.getParcelable(CURRENT_CITY_ARGS)

        val map = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapManager.apply {
            map?.let {
                initMap(it) {
                    currentCity?.let { goToCurrentPosition(it) }
                    initPins(cities, currentCity)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mapManager.releaseMap()
    }

    companion object {

        fun newInstance(currentCity: City?, cities: List<City>) =
            MapFragment().apply {
                Bundle().apply {
                    putParcelableArrayList(CITIES_ARGS, ArrayList(cities))
                    putParcelable(CURRENT_CITY_ARGS, currentCity)
                }.also { arguments = it }
            }
    }


}