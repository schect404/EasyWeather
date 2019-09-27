package com.atitto.easyweather.presentation.main.widget.addcity

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.RelativeLayout
import com.atitto.domain.cities.CitiesUseCase
import com.atitto.domain.cities.model.SearchCity
import com.atitto.easyweather.BR
import com.atitto.easyweather.R
import com.atitto.easyweather.common.AsyncObservableList
import com.atitto.easyweather.common.attachAdapter
import com.atitto.easyweather.common.makeAction
import com.github.nitrico.lastadapter.LastAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AddCityView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val items: AsyncObservableList<SearchCity> = AsyncObservableList()

    private var useCase: CitiesUseCase? = null

    private val adapter = LastAdapter(items, BR.item).map<SearchCity, com.atitto.easyweather.databinding.ItemSearchCityBinding>(R.layout.item_search_city) {
        onClick {
            it.binding.item?.let{ onCityClicked.invoke(it.name) }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    private val searchData: PublishSubject<String?> = PublishSubject.create()

    var onCityClicked: (String) -> Unit = {  }

    fun attachUseCase(useCase: CitiesUseCase) {
        this.useCase = useCase
    }

    init {
        inflate(context, R.layout.layout_new_city, this)

        val list: RecyclerView = findViewById(R.id.rvNewCities)
        list.attachAdapter(adapter)

        bindSearch()
        subscribeSearch()

    }

    private fun bindSearch() {
        val searchView: EditText = findViewById(R.id.etCity)
        searchView.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchData.onNext(s.toString())
            }
        })
    }

    private fun subscribeSearch() = compositeDisposable.makeAction(searchData.debounce(500, TimeUnit.MILLISECONDS), {}) { search(it) }


    private fun search(prefix: String?) = useCase?.let { compositeDisposable.makeAction(it.searchCity(prefix), {}) { items.update(it) } }

    fun dispose() = compositeDisposable.dispose()


}