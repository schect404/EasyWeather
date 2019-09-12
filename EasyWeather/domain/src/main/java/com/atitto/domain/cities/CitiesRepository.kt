package com.atitto.domain.cities

import android.location.Location
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.SearchCity
import com.atitto.domain.cities.model.WeatherDetails
import io.reactivex.Completable
import io.reactivex.Single

interface CitiesRepository {

    fun getDefaultCities(): List<City>
    fun getDBCities(): Single<List<City>>
    fun insertCitiesToDB(cities: List<City>): Completable
    fun updateDBCity(city: City): Completable
    fun getWeather(city: City): Single<City>
    fun getWeatherDetails(city: City): Single<List<WeatherDetails>>
    fun getLocation(location: Location): Single<String?>
    fun getCities(prefix: String?): Single<List<SearchCity>>
}