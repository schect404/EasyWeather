package com.atitto.easyweather.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
import android.view.*
import com.atitto.easyweather.databinding.ItemCityBinding
import android.view.MenuInflater
import com.atitto.easyweather.MainActivity

class MainFragment : Fragment() {

    private val items: AsyncObservableList<City> = AsyncObservableList()

    private val adapter = LastAdapter(items, BR.item).map<City, ItemCityBinding>(R.layout.item_city) { onClick {
        it.binding.item?.let { city -> viewModel.showDetails(city) }
    } }

    private val state = AtomicReference<List<City>>()

    private val viewModel: MainViewModel = get()

    private val permissionManager: PermissionManager = get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupList()
        checkState()
        bindViewModel()
        if(state.get() == null) knowLocation()
    }

    private fun checkState() {
        state.get()?.let { items.loaded(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.set(items)
    }

    private fun setupList() {
        rvCities.attachAdapter(adapter)
        rvCities.scheduleLayoutAnimation()
    }

    private fun bindViewModel() {
        bindDataTo(viewModel.cities) { items.update(it) }
        bindDataTo(viewModel.errorLiveData) { error -> context?.let { DialogHelper.showErrorAlert(it, error) } }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when(item?.itemId) {
            R.id.action_add -> context?.let { DialogHelper.showAddCityDialog(it, layoutInflater, items, get()) { viewModel.addCity(it, items) } }
            R.id.action_map -> {
                val myCity = items.firstOrNull { it.isMy }
                (activity as? MainActivity)?.goToMap(myCity, items)
            }
            else -> {}
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(permissionManager.isLocationPermissionGranted()) {
            initCities()
        } else {
            viewModel.initCities(null)
        }
    }

    private fun knowLocation() =
        if (permissionManager.isLocationPermissionGranted()) permissionManager.requestPermission(this) else initCities()

    @SuppressLint("MissingPermission")
    private fun initCities() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val provider = locationManager?.getBestProvider(Criteria(), false)
        val location = provider?.let { locationManager.getLastKnownLocation(it) }
        viewModel.initCities(location)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
