package com.atitto.data

import android.arch.persistence.room.Room
import com.atitto.data.cities.CitiesRepositoryImpl
import com.atitto.data.cities.api.CitiesApi
import com.atitto.data.cities.db.WeatherDB
import com.atitto.data.cities.default.DefaultCitiesProvider
import com.atitto.data.cities.default.DefaultCitiesProviderImpl
import com.atitto.data.location.LocationProvider
import com.atitto.data.location.LocationProviderImpl
import com.atitto.data.retrofit.RetrofitFactory
import com.atitto.data.retrofit.RetrofitFactoryImpl
import com.atitto.domain.cities.CitiesRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit

val dataModule = module {
    single<Gson> { GsonBuilder().setLenient().create() }
    single { OkHttpClient() }
    single<RetrofitFactory> { RetrofitFactoryImpl() }
    single { get<RetrofitFactory>().createRetrofit(get(), get()) }
    single<DefaultCitiesProvider> { DefaultCitiesProviderImpl(get(), get()) }
    single { Room.databaseBuilder(androidApplication(), WeatherDB::class.java, "weather-db").allowMainThreadQueries().build() }
    single { get<Retrofit>().create(CitiesApi::class.java) }
    single<LocationProvider> { LocationProviderImpl(get()) }
    single<CitiesRepository> { CitiesRepositoryImpl(get(), get(), get(), get()) }
    single { get<WeatherDB>().citiesDao() }
}