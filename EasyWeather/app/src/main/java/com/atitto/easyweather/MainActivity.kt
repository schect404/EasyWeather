package com.atitto.easyweather

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import com.atitto.easyweather.presentation.main.MainFragment
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) { initPrimary() }
        shouldDisplayHomeUp()
        initAds()
    }

    private fun initPrimary() {
        val tr = supportFragmentManager.beginTransaction()
        supportFragmentManager.addOnBackStackChangedListener(this)
        tr.replace(R.id.vParent, MainFragment.newInstance())
        tr.commit()
    }

    private fun initAds() {
        MobileAds.initialize(this, BuildConfig.AD_MOB_KEY)
        adView.loadAd(AdRequest.Builder().addTestDevice("DF57416E00F012FC528E1B25C7F8F6F8").build())
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
