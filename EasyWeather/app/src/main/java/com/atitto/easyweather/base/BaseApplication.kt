package com.atitto.easyweather.base

import android.app.Application
import com.atitto.data.dataModule
import com.atitto.domain.domainModule
import com.atitto.easyweather.di.appModule
import org.koin.android.ext.android.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(dataModule, domainModule, appModule))
    }

}