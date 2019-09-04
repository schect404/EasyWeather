package com.atitto.easyweather

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.presentation.main.MainFragment
import com.atitto.easyweather.presentation.map.MapFragment

private const val CITIES_ARGS = "CITIES"
private const val CURRENT_CITY_ARGS = "CURRENT_CITY"

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val tr = supportFragmentManager.beginTransaction()
            supportFragmentManager.addOnBackStackChangedListener(this)
            tr.replace(R.id.vParent, MainFragment.newInstance())
            tr.commit()
        }
        shouldDisplayHomeUp()
    }

    fun goToMap(currentCity: City, cities: List<City>) {
        val fragment = MapFragment.newInstance()
        val args = Bundle()
        args.putParcelableArrayList(CITIES_ARGS, ArrayList(cities))
        args.putParcelable(CURRENT_CITY_ARGS, currentCity)
        fragment.arguments = args
        val tr = supportFragmentManager.beginTransaction()
        tr.replace(R.id.vParent, fragment)
        tr.addToBackStack(null)
        tr.commit()
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(canGoBack)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }
}
