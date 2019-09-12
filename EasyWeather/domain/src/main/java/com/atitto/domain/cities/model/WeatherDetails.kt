package com.atitto.domain.cities.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDetails(
    val iconUrl: String,
    val time: String,
    val temperature: String,
    val windSpeed: String,
    val pressure: String
): Parcelable