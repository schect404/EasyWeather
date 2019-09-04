package com.atitto.easyweather.binding

import android.databinding.BindingAdapter
import android.graphics.Color
import android.widget.RelativeLayout
import com.atitto.easyweather.R

object CitiesBinding {

    @JvmStatic
    @BindingAdapter("app:isMy")
    fun formatDate(layout: RelativeLayout, isMy: Boolean) {
        val color = if(isMy) layout.context.getColor(R.color.colorSecondary) else Color.WHITE
        layout.setBackgroundColor(color)
    }

}