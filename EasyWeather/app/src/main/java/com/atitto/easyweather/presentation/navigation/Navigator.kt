package com.atitto.easyweather.presentation.navigation

import android.support.v4.app.FragmentManager
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import com.atitto.easyweather.presentation.map.MapFragment

interface Navigator {
    fun goToMap(currentCity: City?, cities: List<City>, fragmentManager: FragmentManager)
}

class NavigatorImpl: Navigator {

    override fun goToMap(currentCity: City?, cities: List<City>, fragmentManager: FragmentManager) {
        val fragment = MapFragment.newInstance(currentCity, cities)
        fragmentManager.beginTransaction()
            .replace(R.id.vParent, fragment)
            .addToBackStack(null)
            .commit()
    }

}