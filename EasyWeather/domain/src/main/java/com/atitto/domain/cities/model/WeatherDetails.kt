package com.atitto.domain.cities.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class WeatherDetails(
    val iconUrl: String,
    val time: String,
    val temperature: String,
    val windSpeed: String,
    val pressure: String
): Parcelable

@Parcelize
data class WeatherDailyDetails(
    val date: String,
    val averageTemperature: String,
    val iconUrl: String,
    val details: List<WeatherDetails>,
    val isExpanded: Boolean = false
): Parcelable