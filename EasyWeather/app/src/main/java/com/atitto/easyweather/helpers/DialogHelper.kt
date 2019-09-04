package com.atitto.easyweather.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.atitto.domain.cities.model.City
import com.atitto.easyweather.R

object DialogHelper {

    fun showErrorAlert(context: Context, error: String) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    fun showCreateCategoryDialog(context: Context, layoutInflater: LayoutInflater, cities: List<City>, listener: (String) -> Unit ) {

        val view = layoutInflater.inflate(R.layout.layout_new_city, null)
        val etCity = view.findViewById(R.id.etCity) as EditText

        val dialog = AlertDialog
            .Builder(context)
            .setTitle("New City")
            .setView(view)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()

        dialog.setOnShowListener {
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val newCity = etCity.text.toString()

                when {
                    (cities.firstOrNull { it.name.toLowerCase() == newCity }) != null -> etCity.error = "This city already presents"
                    newCity.isEmpty() -> etCity.error = "Incorrect"
                    else -> {
                        listener.invoke(newCity.capitalize())
                        dialog.dismiss()
                    }
                }

            }
        }

        dialog.show()

    }

}