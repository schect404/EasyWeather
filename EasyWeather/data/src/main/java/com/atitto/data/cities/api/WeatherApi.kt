package com.atitto.data.cities.api

import com.atitto.data.BuildConfig
import com.atitto.data.cities.api.model.ApiWeather
import com.atitto.data.cities.api.model.ApiWeatherDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/find")
    fun getCityWeather(@Query("q") city: String,
                       @Query("units") units: String = "metric",
                       @Query("appid") appid: String = BuildConfig.API_KEY): Single<ApiWeather>

    @GET("data/2.5/forecast")
    fun getWeatherDetails(@Query("q") city: String,
                          @Query("units") units: String = "metric",
                          @Query("appid") appid: String = BuildConfig.API_KEY): Single<ApiWeatherDetails>


}