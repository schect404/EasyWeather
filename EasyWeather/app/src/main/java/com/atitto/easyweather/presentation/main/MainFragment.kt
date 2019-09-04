package com.atitto.easyweather.presentation.main

import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.BR
import com.atitto.easyweather.R
import com.atitto.easyweather.common.*
import com.atitto.easyweather.helpers.DialogHelper
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.get
import java.util.concurrent.atomic.AtomicReference
import android.location.*
import com.atitto.easyweather.MainActivity
import com.atitto.easyweather.databinding.ItemCityBinding


class MainFragment : Fragment() {

    private val items: ObservableList<City> = ObservableArrayList()

    private val adapter = LastAdapter(items, BR.item).map<City, ItemCityBinding>(R.layout.item_city) { onClick {
        it.binding.item?.let { city -> city.coords?.let { (activity as? MainActivity)?.goToMap(city, items) } }
    } }

    private val state = AtomicReference<List<City>>()

    private val viewModel: MainViewModel = get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        knowLocation(view.context)
        setupList()
        setupFAB()
        checkState()
        bindViewModel()
    }

    private fun checkState() {
        state.get()?.let { items.loaded(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.set(items)
    }

    private fun setupList() {
        rvCities.decorate()
        rvCities.attachAdapter(adapter)
    }

    private fun bindViewModel() {
        bindDataTo(viewModel.cities, items::loaded)
        bindDataTo(viewModel.errorLiveData) { error -> context?.let { DialogHelper.showErrorAlert(it, error) } }
        bindDataTo(viewModel.updatedCity, items::update)
    }

    private fun setupFAB() {
        fab.setOnClickListener {
            DialogHelper.showCreateCategoryDialog(it.context, layoutInflater, items) {
                viewModel.addCity(it, items)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.let {
            if (Build.VERSION.SDK_INT >= 23 ||
                ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                val provider = locationManager?.getBestProvider(Criteria(), false)
                val location = provider?.let { locationManager.getLastKnownLocation(it) }
                viewModel.initCities(location)
            } else {
                viewModel.initCities(null)
            }
        }
    }

    private fun knowLocation(context: Context) {
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        } else {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val provider = locationManager?.getBestProvider(Criteria(), false)
            val location = provider?.let { locationManager.getLastKnownLocation(it) }
            viewModel.initCities(location)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
