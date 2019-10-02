package com.atitto.data.cities.db

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface CitiesDAO {

    @Query("SELECT * from cities")
    fun getAllCities(): Single<List<CityDB>>

    @Insert
    fun insertAll(cities: List<CityDB>)

    @Update
    fun updateCity(city: CityDB)

    @Delete
    fun deleteCity(city: CityDB)

}