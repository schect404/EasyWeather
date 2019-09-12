package com.atitto.data.retrofit

import com.atitto.data.BuildConfig
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactoryImpl : RetrofitFactory {

    override fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson, variants: RetrofitVariants): Retrofit {
        val okHttpBuilder =
            okHttpClient.newBuilder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(loggingInterceptor)

        val baseUrl = if(variants == RetrofitVariants.CITIES) BuildConfig.BASE_URL_CITIES else BuildConfig.BASE_URL_WEATHER

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        return builder.build()
    }

}

