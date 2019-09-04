package com.atitto.easyweather.di

import com.atitto.easyweather.presentation.main.MainViewModel
import com.atitto.easyweather.presentation.main.MainViewModelImpl
import org.koin.dsl.module.module

val appModule = module {
    factory<MainViewModel> { MainViewModelImpl(get()) }
}