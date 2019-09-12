package com.atitto.easyweather.common

import android.databinding.ObservableList
import com.atitto.domain.cities.model.City

fun <T> ObservableList<T>.loaded(list: List<T>) {
    clear()
    addAll(list)
}