package com.atitto.data.cities

import android.location.Location
import com.atitto.data.cities.api.CitiesApi
import com.atitto.data.cities.db.CitiesDAO
import com.atitto.data.cities.db.CityDB
import com.atitto.data.cities.default.DefaultCitiesProvider
import com.atitto.data.location.LocationProvider
import com.atitto.domain.cities.CitiesRepository
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.Coords
import io.reactivex.Completable
import io.reactivex.Single
import kotlin.math.roundToInt

class CitiesRepositoryImpl(private val defaultProvider: DefaultCitiesProvider,
                           private val dao: CitiesDAO,
                           private val citiesApi: CitiesApi,
                           private val locationProvider: LocationProvider): CitiesRepository {

    override fun getLocation(location: Location): Single<String?> = locationProvider.getCurrentCityLocation(location)

    override fun getWeather(city: City): Single<City> = citiesApi.getCityWeather(city.name).map {
        city.copy(temperature = "${it.list.first().main.temperature.roundToInt()}°C",
            coords = Coords(lat = it.list.first().coords.latitude, long = it.list.first().coords.longtitude))
    }

    override fun getDefaultCities(): List<City> = defaultProvider.getCities().map { City(name = it) }

    override fun updateDBCity(city: City): Completable {
        return try {
            dao.updateCity(CityDB(name = city.name, temperature = city.temperature))
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    override fun getDBCities(): Single<List<City>> = dao.getAllCities().map { it.map { City(name = it.name, temperature = it.temperature) } }

    override fun insertCitiesToDB(cities: List<City>): Completable {
        return try {
            dao.insertAll(cities.map { CityDB(name = it.name, temperature = it.temperature) })
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

}