package com.atitto.data.cities.default

import android.content.Context
import androidx.annotation.RawRes
import com.atitto.data.R
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

interface DefaultCitiesProvider {
    fun getCities(): List<String>
}

class DefaultCitiesProviderImpl(private val context: Context, private val gson: Gson): DefaultCitiesProvider {

    override fun getCities(): List<String>  = getFromResources(R.raw.default_cities, ResourceCities::class.java).cities

    private fun<T> getFromResources(@RawRes targetResourceId: Int, outClass: Class<T> ): T {
        val jsonString = context.resources.openRawResource(targetResourceId).bufferedReader().use { it.readText() }
        return gson.fromJson(jsonString, outClass)
    }

}

data class ResourceCities(
    @SerializedName("cities")
    val cities: List<String>
)