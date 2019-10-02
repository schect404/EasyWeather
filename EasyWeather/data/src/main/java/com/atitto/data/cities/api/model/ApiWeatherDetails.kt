package com.atitto.data.cities.api.model

import com.google.gson.annotations.SerializedName

data class ApiWeatherDetails(
    @SerializedName("list")
    val list: List<ApiWeatherDetailsList>
)

data class ApiWeatherDetailsList(
    @SerializedName("dt")
    val time: Long,
    @SerializedName("main")
    val main: ApiWeatherDetailsExt,
    @SerializedName("wind")
    val wind: ApiWeatherDetailsWind,
    @SerializedName("weather")
    val weather: List<ApiWeatherDetailsWeather>
)

data class ApiWeatherDetailsExt(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("pressure")
    val pressure: Double
)

data class ApiWeatherDetailsWind(
    @SerializedName("speed")
    val speed: Double
)

data class ApiWeatherDetailsWeather(
    @SerializedName("icon")
    val icon: String
)