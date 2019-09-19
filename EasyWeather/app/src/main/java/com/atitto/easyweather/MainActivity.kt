package com.atitto.easyweather

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.presentation.main.MainFragment
import com.atitto.easyweather.presentation.map.MapFragment

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
