package com.atitto.domain.cities.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City (
   val name: String,
   val temperature: String? = null,
   val isMy: Boolean = false,
   val coords: Coords? = null,
   val iconUrl: String? = null
): Parcelable

@Parcelize
data class Coords(
   val lat: Double,
   val long: Double
):Parcelable