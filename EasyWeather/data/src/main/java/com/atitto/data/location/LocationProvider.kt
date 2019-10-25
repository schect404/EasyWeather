package com.atitto.data.location

import android.content.Context
import android.location.*
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Single
import java.util.*

interface LocationProvider {
    fun requestLocation(callback:(Location?) -> Unit)
    fun getCurrentCityLocation(location: Location): Single<String?>
}

class LocationProviderImpl(private val context: Context): LocationProvider {

    override fun requestLocation(callback: (Location?) -> Unit) {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)
            .setFastestInterval(1000)
            .setNumUpdates(1)

        val locationCallback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                val firebase = FirebaseAnalytics.getInstance(context)
                val bundle = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_NAME, result?.locations?.first().toString())
                }
                firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                callback.invoke(result?.locations?.first())
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                if(p0?.isLocationAvailable == false) callback.invoke(null)
            }
        }

        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun getCurrentCityLocation(location: Location): Single<String?> {
        val coder = Geocoder(context, Locale.ENGLISH)
        val results: List<Address>?
        return try {
            val firebase = FirebaseAnalytics.getInstance(context)

            results = coder.getFromLocation(location.latitude, location.longitude, 1)

            if (results.isNullOrEmpty().not()) {
                results.forEach {
                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.ITEM_NAME, it.locality)
                        putString(FirebaseAnalytics.Param.CONTENT_TYPE, it.subLocality)
                    }
                    firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                    if (it.locality.isNullOrEmpty().not()) {
                        return Single.just(it.locality.replace("'",""))
                    }
                }
            }
            Single.error(java.lang.Exception("Could not find city"))
        } catch (e: Exception) {
            Single.error(java.lang.Exception("Could not find city"))
        }
    }

}