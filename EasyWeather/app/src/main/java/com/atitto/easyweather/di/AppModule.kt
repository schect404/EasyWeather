package com.atitto.easyweather.di

import com.atitto.easyweather.common.PermissionManager
import com.atitto.easyweather.common.PermissionManagerImpl
import com.atitto.easyweather.presentation.main.MainViewModel
import com.atitto.easyweather.presentation.main.MainViewModelImpl
import com.atitto.easyweather.presentation.map.widget.MapManager
import com.atitto.easyweather.presentation.map.widget.MapManagerImpl
import com.atitto.easyweather.presentation.navigation.Navigator
import com.atitto.easyweather.presentation.navigation.NavigatorImpl
import org.koin.dsl.module.module

val appModule = module {
    factory<MainViewModel> { MainViewModelImpl(get()) }
    single<PermissionManager> { PermissionManagerImpl(get()) }
    factory<MapManager> { MapManagerImpl(get()) }
    factory<Navigator> { NavigatorImpl() }
}