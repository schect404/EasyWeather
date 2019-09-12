package com.atitto.data.cities.search.model

import com.atitto.domain.cities.model.SearchCity
import com.google.gson.annotations.SerializedName

data class ApiSearchCities(
    @SerializedName("data")
    val cities: List<ApiSearchCity>?
)

data class ApiSearchCity(
    @SerializedName("city")
    val city: String
)

fun ApiSearchCities.toSearchCities() =
        cities?.map { SearchCity(it.city) }