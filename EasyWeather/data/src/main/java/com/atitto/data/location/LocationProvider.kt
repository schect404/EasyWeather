package com.atitto.data.location

import android.content.Context
import android.location.*
import com.google.android.gms.location.*
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

        val locationCallback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
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
            results = coder.getFromLocation(location.latitude, location.longitude, 1)
            val city = results[0].locality.replace("'","")
            Single.just(city)
        } catch (e: Exception) {
            Single.just(null)
        }
    }

}