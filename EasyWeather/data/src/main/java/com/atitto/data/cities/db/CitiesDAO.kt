package com.atitto.data.cities.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CitiesDAO {

    @Query("SELECT * from cities")
    fun getAllCities(): Single<List<CityDB>>

    @Insert
    fun insertAll(cities: List<CityDB>): Completable

    @Update
    fun updateCity(city: CityDB): Completable

    @Delete
    fun deleteCity(city: CityDB): Completable

}