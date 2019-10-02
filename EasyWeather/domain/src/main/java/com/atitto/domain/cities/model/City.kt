package com.atitto.domain.cities.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City (
   val name: String,
   val temperature: String? = null,
   val isMy: Boolean = false,
   val coords: Coords? = null,
   val iconUrl: String? = null,
   val dailyDetails: List<WeatherDailyDetails>? = null,
   val dailyExpanded: Boolean = false,
   val details: List<WeatherDetails>? = null,
   val expandedDetails: Boolean = false
): Parcelable {

   fun isCoordsEnabled() = coords != null

   fun isChanged(another: City) =
      (temperature != another.temperature)
      .or(coords != another.coords)
      .or(iconUrl != another.iconUrl)
      .or(isMy != another.isMy)
      .or(dailyExpanded != another.dailyExpanded)
      .or( dailyDetails != another.dailyDetails)

}

@Parcelize
data class Coords(
   val lat: Double,
   val long: Double
):Parcelable