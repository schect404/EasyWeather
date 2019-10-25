package com.atitto.easyweather.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.bindDataTo(liveData: LiveData<T>, listener: (T) -> Unit) {
    liveData.observe(this, Observer { value ->
        value?.let{ listener.invoke(it) }
    })
}