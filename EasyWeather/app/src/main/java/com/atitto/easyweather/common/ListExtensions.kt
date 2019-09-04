package com.atitto.easyweather.common

import android.databinding.ObservableList
import com.atitto.domain.cities.model.City

fun <T> ObservableList<T>.loaded(list: List<T>) {
    clear()
    addAll(list)
}

fun ObservableList<City>.update(item: City) {
    val newList = toMutableList().map { if(it.name == item.name) item else it }
    loaded(newList)
}