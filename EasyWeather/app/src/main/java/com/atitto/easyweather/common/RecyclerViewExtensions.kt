package com.atitto.easyweather.common

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.atitto.easyweather.R

fun RecyclerView.decorate() {
    val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    decoration.setDrawable(resources.getDrawable(R.drawable.divider))
    addItemDecoration(decoration)
}

fun RecyclerView.attachAdapter(listAdapter: RecyclerView.Adapter<*>) {
    adapter ?: run { adapter = listAdapter }
}