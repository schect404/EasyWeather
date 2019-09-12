package com.atitto.domain.cities

import android.location.Location
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.SearchCity
import com.atitto.domain.cities.model.WeatherDetails
import io.reactivex.Completable
import io.reactivex.Single

interface CitiesUseCase {

    fun getDefaultCities(myLocation: String?): Single<List<City>>
    fun getDBCities(myLocation: String?): Single<List<City>>
    fun insertCitiesToDB(cities: List<City>): Completable
    fun updateCity(city: City): Completable
    fun getWeather(city: City): Single<City>
    fun getWeatherDetails(city: City): Single<List<WeatherDetails>>
    fun getLocation(location: Location): Single<String?>
    fun searchCity(prefix: String?): Single<List<SearchCity>>
}

class CitiesUseCaseImpl(private val citiesRepository: CitiesRepository): CitiesUseCase {

    override fun getDefaultCities(myLocation: String?): Single<List<City>> = Single.just(handleMyCities(myLocation, ArrayList(citiesRepository.getDefaultCities())))

    override fun getDBCities(myLocation: String?): Single<List<City>> = citiesRepository.getDBCities().map { handleMyCities(myLocation, ArrayList(it)) }

    override fun insertCitiesToDB(cities: List<City>): Completable = citiesRepository.insertCitiesToDB(cities)

    override fun updateCity(city: City): Completable = citiesRepository.updateDBCity(city)

    override fun getWeather(city: City): Single<City> = citiesRepository.getWeather(city)

    override fun getWeatherDetails(city: City): Single<List<WeatherDetails>> = citiesRepository.getWeatherDetails(city)

    override fun getLocation(location: Location) = citiesRepository.getLocation(location)

    override fun searchCity(prefix: String?): Single<List<SearchCity>> = citiesRepository.getCities(prefix)

    private fun handleMyCities(location: String?, cities: ArrayList<City>): List<City>  {
        location?.let {
            if(cities.count() != 0) {
                val presentInSource = cities.firstOrNull { it.name == location }
                presentInSource?.let {
                    cities.remove(presentInSource)
                    cities.add(0, presentInSource.copy(isMy = true))
                }
            }
        }
        return cities
    }

}