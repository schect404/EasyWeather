package com.atitto.easyweather.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.BR
import com.atitto.easyweather.R
import com.atitto.easyweather.common.*
import com.atitto.easyweather.helpers.DialogHelper
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.get
import java.util.concurrent.atomic.AtomicReference
import android.view.*
import android.view.MenuInflater
import com.atitto.easyweather.databinding.ItemCityBinding
import com.atitto.easyweather.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.item_city.view.*

class MainFragment : Fragment() {

    private val items: AsyncObservableList<City> = AsyncObservableList()

    private val adapter = LastAdapter(items, BR.item).map<City, ItemCityBinding>(R.layout.item_city) {
        onClick { it.binding.item?.let { city -> viewModel.showDetails(city) } }
        onBind { holder ->
            holder.binding.item?.let { city ->
                holder.itemView.vWeatherDailyDetails.onClick = { viewModel.onDailyClicked(city, it) }
                holder.itemView.ivRemove.setOnClickListener { context?.let { DialogHelper.showDeleteAlert(it) { viewModel.onCityDeleted(city) } }  }
            }
        }
    }

    private val state = AtomicReference<List<City>>()

    private val viewModel: MainViewModel = get()

    private val permissionManager: PermissionManager = get()

    private val navigator: Navigator = get()

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
        state.get()?.let { items.update(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.set(items)
    }

    private fun setupList() {
        rvCities.attachAdapter(adapter)
        rvCities.scheduleLayoutAnimation()
//        rvCities.isNestedScrollingEnabled = true
//        val icon = context?.getDrawable(R.drawable.ic_delete)
//        icon?.let {
//            ItemTouchHelper(SwipeToDeleteCallback({ viewModel.onCityDeleted(items[it]) }, it) ). .attachToRecyclerView(rvCities)
//        }
    }

    private fun bindViewModel() {
        bindDataTo(viewModel.cities) {
            pbCities.visibility = View.GONE
            items.update(it)
        }
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
                fragmentManager?.let { navigator.goToMap(myCity, items, it) }
            }
            else -> {}
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(permissionManager.isLocationPermissionGranted()) viewModel.init() else viewModel.initCities(null)
    }

    private fun knowLocation() =
        if (permissionManager.isLocationPermissionGranted()) permissionManager.requestPermission(this) else viewModel.init()


    companion object {
        fun newInstance() = MainFragment()
    }

}
