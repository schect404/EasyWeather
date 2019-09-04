package com.atitto.data.cities.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CitiesDAO {

    @Query("SELECT * from cities")
    fun getAllCities(): Single<List<CityDB>>

    @Insert
    fun insertAll(cities: List<CityDB>)

    @Update
    fun updateCity(city: CityDB)

}