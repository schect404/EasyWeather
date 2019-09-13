package com.atitto.data.location

import android.content.Context
import android.location.*
import io.reactivex.Single
import java.util.*

interface LocationProvider {
    fun getCurrentCityLocation(location: Location): Single<String?>
}

class LocationProviderImpl(private val context: Context): LocationProvider {

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