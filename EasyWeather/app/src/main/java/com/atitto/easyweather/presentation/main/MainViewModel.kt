package com.atitto.easyweather.presentation.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.atitto.domain.cities.CitiesRepository
import com.atitto.domain.cities.CitiesUseCase
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.common.makeAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface MainViewModel {

    val cities: LiveData<List<City>>
    val errorLiveData: LiveData<String>
    val updatedCity: LiveData<City>

    fun initCities(location: Location?)
    fun addCity(city: String, currentCities: List<City>)
}

class MainViewModelImpl(private val citiesUseCase: CitiesUseCase): MainViewModel, ViewModel() {

    override val cities: MutableLiveData<List<City>> = MutableLiveData()

    override val errorLiveData: MutableLiveData<String> = MutableLiveData()

    override val updatedCity: MutableLiveData<City> = MutableLiveData()

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
                val newCity = city.copy(temperature = it.temperature, coords = it.coords)
                updatedCity.value = newCity
                updateCity(newCity)
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

    private fun updateCity(city: City) {
        compositeDisposable.makeAction(citiesUseCase.updateCity(city), errorLiveData) {}
    }

}