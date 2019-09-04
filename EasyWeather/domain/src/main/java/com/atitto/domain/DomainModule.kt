package com.atitto.domain

import com.atitto.domain.cities.CitiesUseCase
import com.atitto.domain.cities.CitiesUseCaseImpl
import org.koin.dsl.module.module

val domainModule = module {
    single<CitiesUseCase> { CitiesUseCaseImpl(get()) }
}