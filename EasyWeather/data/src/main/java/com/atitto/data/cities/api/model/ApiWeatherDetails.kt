package com.atitto.data.cities.api.model

import com.atitto.domain.cities.model.WeatherDetails
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

private const val ICON_URL_PATTERN = "http://openweathermap.org/img/w/%s.png"

fun ApiWeatherDetails.toWeatherDetails(): List<WeatherDetails> =
    list.map {
        WeatherDetails(
            iconUrl = ICON_URL_PATTERN.format(it.weather.first().icon),
            time = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(it.time*1000)),
            temperature = "${it.main.temperature}°C",
            windSpeed = "${it.wind.speed} m/s",
            pressure = it.main.pressure.toString()
        )
    }

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