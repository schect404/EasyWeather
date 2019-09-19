package com.atitto.data.cities

import android.location.Location
import com.atitto.data.cities.api.WeatherApi
import com.atitto.data.cities.api.model.toWeatherDetails
import com.atitto.data.cities.api.model.updateCity
import com.atitto.data.cities.db.CitiesDAO
import com.atitto.data.cities.db.toCity
import com.atitto.data.cities.db.toDBCity
import com.atitto.data.cities.default.DefaultCitiesProvider
import com.atitto.data.cities.search.SearchCitiesApi
import com.atitto.data.cities.search.model.toSearchCities
import com.atitto.data.location.LocationProvider
import com.atitto.domain.cities.CitiesRepository
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.SearchCity
import com.atitto.domain.cities.model.WeatherDetails
import io.reactivex.Completable
import io.reactivex.Single

class CitiesRepositoryImpl(private val defaultProvider: DefaultCitiesProvider,
                           private val dao: CitiesDAO,
                           private val weatherApi: WeatherApi,
                           private val citiesApi: SearchCitiesApi,
                           private val locationProvider: LocationProvider): CitiesRepository {

    override fun getWeatherDetails(city: City): Single<List<WeatherDetails>> = weatherApi.getWeatherDetails(city.name).map { it.toWeatherDetails() }

    override fun getLocation(location: Location): Single<String?> = locationProvider.getCurrentCityLocation(location)

    override fun getWeather(city: City): Single<City> = weatherApi.getCityWeather(city.name).map { it.updateCity(city) }

    override fun getDefaultCities(): List<City> = defaultProvider.getCities().map { City(name = it) }

    override fun updateDBCity(city: City): Completable {
        return try {
            dao.updateCity(city.toDBCity())
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    override fun requestLocation(callback: (Location?) -> Unit) = locationProvider.requestLocation(callback)

    override fun getDBCities(): Single<List<City>> = dao.getAllCities().map { it.map { it.toCity() } }

    override fun insertCitiesToDB(cities: List<City>): Completable {
        return try {
            dao.insertAll(cities.map { it.toDBCity() })
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    override fun getCities(prefix: String?): Single<List<SearchCity>> = citiesApi.getCities(prefix).map { it.toSearchCities() }
}