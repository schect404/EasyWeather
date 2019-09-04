package com.atitto.data.cities.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [CityDB::class], version = 1)
abstract class WeatherDB : RoomDatabase() {
    abstract fun citiesDao(): CitiesDAO
}