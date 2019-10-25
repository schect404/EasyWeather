package com.atitto.data.cities.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.Coords

@Entity(tableName = "cities")
data class CityDB (
    @PrimaryKey var name: String = "",
    var temperature: String? = null,
    var iconLogo: String? = null,
    var lat: Double? = null,
    var long: Double? = null
)

fun CityDB.toCity() =
        City(
            name = name,
            temperature = temperature,
            iconUrl = iconLogo,
            coords = Coords(
                lat = lat ?: 0.0,
                long = long ?: 0.0))

fun City.toDBCity() =
        CityDB(name = name,
            temperature = temperature,
            iconLogo = iconUrl,
            lat = coords?.lat,
            long = coords?.long)
