package com.atitto.domain.cities

import android.location.Location
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.SearchCity
import com.atitto.domain.cities.model.WeatherDailyDetails
import com.atitto.domain.cities.model.WeatherDetails
import io.reactivex.Completable
import io.reactivex.Single

interface CitiesUseCase {

    fun getDefaultCities(myLocation: String?): Single<List<City>>
    fun getDBCities(myLocation: String?, ifCurrentIsNewCallback: (City) -> Unit): Single<List<City>>
    fun insertCitiesToDB(cities: List<City>): Completable
    fun updateCity(city: City): Completable
    fun getWeather(city: City): Single<City>
    fun getWeatherDetails(city: City): Single<List<WeatherDailyDetails>>
    fun getLocation(location: Location): Single<String?>
    fun requestLocation(callback: (Location?) -> Unit)
    fun searchCity(prefix: String?): Single<List<SearchCity>>
    fun deleteCity(city: City): Completable
}

class CitiesUseCaseImpl(private val citiesRepository: CitiesRepository): CitiesUseCase {

    override fun getDefaultCities(myLocation: String?): Single<List<City>> = Single.just(handleMyCities(myLocation, ArrayList(citiesRepository.getDefaultCities())) {})

    override fun getDBCities(myLocation: String?, ifCurrentIsNewCallback: (City) -> Unit): Single<List<City>> = citiesRepository.getDBCities().map { handleMyCities(myLocation, ArrayList(it), ifCurrentIsNewCallback) }

    override fun insertCitiesToDB(cities: List<City>): Completable = citiesRepository.insertCitiesToDB(cities)

    override fun updateCity(city: City): Completable = citiesRepository.updateDBCity(city)

    override fun getWeather(city: City): Single<City> = citiesRepository.getWeather(city)

    override fun getWeatherDetails(city: City): Single<List<WeatherDailyDetails>> = citiesRepository.getWeatherDetails(city)

    override fun getLocation(location: Location) = citiesRepository.getLocation(location)

    override fun searchCity(prefix: String?): Single<List<SearchCity>> = citiesRepository.getCities(prefix)

    override fun requestLocation(callback: (Location?) -> Unit) = citiesRepository.requestLocation(callback)

    override fun deleteCity(city: City): Completable = citiesRepository.deleteCity(city)

    private fun handleMyCities(location: String?, cities: ArrayList<City>, ifCurrentIsNewCallback: (City) -> Unit): List<City>  {
        location?.let {
            if(cities.count() != 0) {
                val inside = cities.firstOrNull { it.name == location }
                val presentInSource = cities.indexOfFirst { it.name == location }
                if(presentInSource != -1) { cities.removeAt(presentInSource) }
                else ifCurrentIsNewCallback.invoke(inside?.copy(isMy = true) ?: City(name = location, isMy = true))
                cities.add(0, inside?.copy(isMy = true) ?: City(name = location, isMy = true))
            }
        }
        return cities
    }

}