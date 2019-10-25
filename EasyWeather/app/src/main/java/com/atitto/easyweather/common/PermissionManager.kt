package com.atitto.easyweather.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat

interface PermissionManager {

    fun isLocationPermissionGranted(): Boolean
    fun requestPermission(fragment: Fragment)

}

class PermissionManagerImpl(private val context: Context): PermissionManager {

    override fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    override fun isLocationPermissionGranted()=
        (Build.VERSION.SDK_INT >= 23)
            .or((ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                .and((ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)))


}