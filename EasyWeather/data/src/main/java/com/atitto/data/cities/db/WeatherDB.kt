package com.atitto.data.cities.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityDB::class], version = 1)
abstract class WeatherDB : RoomDatabase() {
    abstract fun citiesDao(): CitiesDAO
}