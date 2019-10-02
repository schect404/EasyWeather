package com.atitto.data.cities.api.model

import com.atitto.domain.cities.model.WeatherDailyDetails
import com.atitto.domain.cities.model.WeatherDetails
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

private const val ICON_URL_PATTERN = "http://openweathermap.org/img/w/%s.png"
private const val TEMPERATURE_PATTERN = "%s°C"
private const val WIND_PATTERN = "%s m/s"
private const val TIME_PATTERN = "HH:mm"
private const val DATE_FORMAT = "EEE \n dd MMM"

fun Long.toMillis() = TimeUnit.SECONDS.toMillis(this)

fun List<ApiWeatherDetailsList>.toWeatherDetails(): List<WeatherDetails> =
    map {
        WeatherDetails(
            iconUrl = ICON_URL_PATTERN.format(it.weather.first().icon),
            time = SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).format(Date(it.time.toMillis())),
            temperature = TEMPERATURE_PATTERN.format(it.main.temperature),
            windSpeed = WIND_PATTERN.format(it.wind.speed),
            pressure = it.main.pressure.toString()
        )
    }

fun List<ApiWeatherDetailsList>.getAverageTemperature(): String {
    var temperature = 0.0
    forEach { temperature = temperature.plus(it.main.temperature) }
    return TEMPERATURE_PATTERN.format(temperature.div(size).roundToInt())
}

fun List<ApiWeatherDetailsList>.getAverageIconUrl(): String {
    val index = if((size.div(2)-1)!=-1) (size.div(2)-1) else 0
    return ICON_URL_PATTERN.format(this[index].weather.first().icon)
}

fun List<ApiWeatherDetailsList>.toWeatherDailyDetails(): Map<String, List<ApiWeatherDetailsList>>
        = groupBy { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(it.time*1000)) }

fun Map<String, List<ApiWeatherDetailsList>>.toWeatherDailyDetails(): List<WeatherDailyDetails> {
    val list = ArrayList<WeatherDailyDetails>()
    keys.forEach {
        list.add(
            WeatherDailyDetails(
                date = it,
                details = this[it]?.toWeatherDetails() ?: listOf(),
                averageTemperature = this[it]?.getAverageTemperature() ?: "",
                iconUrl = this[it]?.getAverageIconUrl() ?: ""
            )
        )
    }
    return list
}
