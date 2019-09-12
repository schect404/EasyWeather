package com.atitto.easyweather.presentation.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.os.Parcelable
import com.atitto.domain.cities.CitiesRepository
import com.atitto.domain.cities.CitiesUseCase
import com.atitto.domain.cities.model.City
import com.atitto.domain.cities.model.WeatherDetails
import com.atitto.easyweather.common.makeAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface MainViewModel {

    val cities: LiveData<List<City>>
    val errorLiveData: LiveData<String>

    fun initCities(location: Location?)
    fun getWeatherDetails(city: City)
    fun showDetails(city: City)
    fun update(city: City)
    fun addCity(city: String, currentCities: List<City>)
}

class MainViewModelImpl(private val citiesUseCase: CitiesUseCase): MainViewModel, ViewModel() {

    override val cities: MutableLiveData<List<City>> = MutableLiveData()

    override val errorLiveData: MutableLiveData<String> = MutableLiveData()

    private val weatherDetails: HashMap<String, List<WeatherDetails>> = HashMap()

    private val compositeDisposable = CompositeDisposable()

    override fun initCities(location: Location?) {
        location?.let {
            compositeDisposable.makeAction(citiesUseCase.getLocation(it), errorLiveData) { initData(it) }
        } ?: run { initData(null) }
    }

    private fun initData(defaultCity: String?) {
        compositeDisposable.makeAction(citiesUseCase.getDBCities(defaultCity), errorLiveData) { handleCitiesFromDB(it, defaultCity) }
    }

    private fun handleCitiesFromDB(citiesFromDB: List<City>, location: String?) {
        if(citiesFromDB.isEmpty()) {
            compositeDisposable.makeAction(citiesUseCase.getDefaultCities(location), errorLiveData) {
                citiesToDB(it)
                cities.postValue(it)
                getWeatherInCities(it)
            }
        } else {
            cities.postValue(citiesFromDB)
            getWeatherInCities(citiesFromDB)
        }
    }

    private fun citiesToDB(cities: List<City>) {
        compositeDisposable.makeAction(citiesUseCase.insertCitiesToDB(cities), errorLiveData) {}
    }

    private fun getWeatherInCities(citiesList: List<City>) {
        citiesList.forEach { city ->
            compositeDisposable.makeAction(citiesUseCase.getWeather(city), { errorLiveData.postValue("Could not load weather for ${city.name}") }) {
                val newCity = city.copy(temperature = it.temperature, coords = it.coords, iconUrl = it.iconUrl)
                update(newCity)
                updateCity(newCity)
                getWeatherDetails(newCity)
            }
        }
    }

    override fun addCity(city: String, currentCities: List<City>) {
        val newList = ArrayList(currentCities)
        val newCity = City(name = city)
        newList.add(newCity)
        cities.postValue(newList)
        citiesToDB(listOf(newCity))
        getWeatherInCities(listOf(newCity))
    }

    override fun showDetails(city: City) {
        val details = weatherDetails[city.name] ?: return
        update(city.copy(details = details, shouldDetailsBeSeen = !city.shouldDetailsBeSeen))
    }

    private fun updateCity(city: City) {
        compositeDisposable.makeAction(citiesUseCase.updateCity(city), errorLiveData) {}
    }

    override fun getWeatherDetails(city: City) {
        compositeDisposable.makeAction(citiesUseCase.getWeatherDetails(city), { errorLiveData.postValue("Could not load weather for ${city.name}") }) {
            val newCity = city.copy(details = it)
            newCity.details?.let { weatherDetails[city.name] = it }
        }
    }

    override fun update(city: City) {
        val data = cities.value ?: return
        cities.value = data.replace(city) { (it.name == city.name).and(it.isChanged(city)) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun <T: Parcelable> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
        return map { if (block(it)) newValue else it }
    }

}