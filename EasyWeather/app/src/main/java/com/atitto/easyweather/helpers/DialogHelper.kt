package com.atitto.easyweather.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R
import android.widget.Toast
import com.atitto.domain.cities.CitiesUseCase
import com.atitto.easyweather.presentation.main.widget.addcity.AddCityView

object DialogHelper {

    fun showErrorAlert(context: Context, error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    fun showDeleteAlert(context: Context, onRemove: () -> Unit) {
        AlertDialog
            .Builder(context)
            .setTitle(R.string.delete_city_title)
            .setNegativeButton(android.R.string.cancel) {_,_ -> }
            .setPositiveButton(android.R.string.ok) { _, _ -> onRemove.invoke() }
            .create()
            .show()
    }

    fun showAddCityDialog(context: Context, layoutInflater: LayoutInflater, cities: List<City>, useCase: CitiesUseCase, listener: (String) -> Unit ) {

        val view = layoutInflater.inflate(R.layout.dialog_add_city, null) as? AddCityView

        view?.attachUseCase(useCase)

        val etCity = view?.findViewById(R.id.etCity) as? EditText

        val dialog = AlertDialog
            .Builder(context)
            .setView(view)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setOnDismissListener { view?.dispose() }
            .create()

        dialog.setOnShowListener {
            view?.onCityClicked = {
                when {
                    (cities.firstOrNull { cities -> cities.name == it }) != null -> etCity?.error = "This city already presents"
                    it.isEmpty() -> etCity?.error = "Incorrect"
                    else -> {
                        listener.invoke(it.capitalize())
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()

    }

}