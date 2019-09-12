package com.atitto.data.cities.search

import com.atitto.data.BuildConfig
import com.atitto.data.cities.search.model.ApiSearchCities
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchCitiesApi {

    @GET("/v1/geo/cities")
    fun getCities(@Query("namePrefix") prefix: String?,
        @Header("X-RapidAPI-Key") apiKey: String = BuildConfig.CITIES_API_KEY,
        @Query("limit") limit: Int = 5,
        @Query("offset") offset: Int = 0): Single<ApiSearchCities>

}