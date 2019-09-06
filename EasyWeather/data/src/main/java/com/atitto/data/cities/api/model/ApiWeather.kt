package com.atitto.data.cities.api.model

import com.google.gson.annotations.SerializedName

data class ApiWeather(
    @SerializedName("list")
    val list: List<ApiWeatherList>
)

data class ApiWeatherList(
    @SerializedName("main")
    val main: ApiWeatherMain,
    @SerializedName("coord")
    val coords: ApiCoords,
    @SerializedName("weather")
    val weather: List<ApiWeatherBlock>
)

data class ApiWeatherMain(
    @SerializedName("temp")
    val temperature: Double
)

data class ApiCoords(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longtitude: Double
)

data class ApiWeatherBlock(
    @SerializedName("icon")
    val icon: String
)

