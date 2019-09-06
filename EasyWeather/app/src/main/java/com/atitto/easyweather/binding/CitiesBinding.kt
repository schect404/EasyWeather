package com.atitto.easyweather.binding

import android.databinding.BindingAdapter
import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout
import com.atitto.easyweather.R
import com.bumptech.glide.Glide

object CitiesBinding {

    @JvmStatic
    @BindingAdapter("app:isMy")
    fun formatDate(layout: RelativeLayout, isMy: Boolean) {
        val color = if(isMy) layout.context.getColor(R.color.colorSecondary) else Color.WHITE
        layout.setBackgroundColor(color)
    }

    @JvmStatic
    @BindingAdapter("app:icon")
    fun loadIcon(view: ImageView, icon: String?) {
        Glide.with(view)
            .load(icon)
            .fitCenter()
            .into(view)
    }

}