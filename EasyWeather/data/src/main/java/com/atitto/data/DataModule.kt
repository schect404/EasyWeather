package com.atitto.data

import androidx.room.Room
import com.atitto.data.cities.CitiesRepositoryImpl
import com.atitto.data.cities.api.WeatherApi
import com.atitto.data.cities.db.WeatherDB
import com.atitto.data.cities.default.DefaultCitiesProvider
import com.atitto.data.cities.default.DefaultCitiesProviderImpl
import com.atitto.data.cities.search.SearchCitiesApi
import com.atitto.data.location.LocationProvider
import com.atitto.data.location.LocationProviderImpl
import com.atitto.data.retrofit.RetrofitFactory
import com.atitto.data.retrofit.RetrofitFactoryImpl
import com.atitto.data.retrofit.RetrofitVariants
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
    single("weather") { get<RetrofitFactory>().createRetrofit(get(), get(), RetrofitVariants.WEATHER) }
    single("cities") { get<RetrofitFactory>().createRetrofit(get(), get(), RetrofitVariants.CITIES) }
    single<DefaultCitiesProvider> { DefaultCitiesProviderImpl(get(), get()) }
    single { Room.databaseBuilder(androidApplication(), WeatherDB::class.java, "weather-db").build() }
    single { get<Retrofit>("weather").create(WeatherApi::class.java) }
    single { get<Retrofit>("cities").create(SearchCitiesApi::class.java)}
    single<LocationProvider> { LocationProviderImpl(get()) }
    single<CitiesRepository> { CitiesRepositoryImpl(get(), get(), get(), get(), get()) }
    single { get<WeatherDB>().citiesDao() }
}