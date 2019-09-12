package com.atitto.data.cities.api.model

import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.Coords
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

private const val ICON_URL_PATTERN = "http://openweathermap.org/img/w/%s.png"

fun ApiWeather.updateCity(city: City) =
    city.copy(temperature = "${list.first().main.temperature.roundToInt()}°C",
        coords = Coords(lat = list.first().coords.latitude, long = list.first().coords.longtitude),
        iconUrl = ICON_URL_PATTERN.format(list.first().weather.first().icon))

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
