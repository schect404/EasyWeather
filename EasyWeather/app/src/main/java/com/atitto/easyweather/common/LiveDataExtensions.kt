package com.atitto.easyweather.common

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LifecycleOwner.bindDataTo(liveData: LiveData<T>, listener: (T) -> Unit) {
    liveData.observe(this, Observer { value ->
        value?.let{ listener.invoke(it) }
    })
}