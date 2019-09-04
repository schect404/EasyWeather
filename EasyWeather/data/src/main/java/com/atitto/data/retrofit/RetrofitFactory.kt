package com.atitto.data.retrofit

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface RetrofitFactory {

    fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit

}