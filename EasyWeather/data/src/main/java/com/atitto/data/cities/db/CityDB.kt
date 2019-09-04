package com.atitto.data.cities.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "cities")
data class CityDB (
    @PrimaryKey val name: String,
    val temperature: String?
)