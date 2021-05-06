package com.pburdelak.randomcityapp.screen.list

import androidx.lifecycle.*
import com.pburdelak.randomcityapp.hilt.DispatchersDefault
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.utils.livedata.Event
import com.pburdelak.randomcityapp.utils.livedata.setEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @DispatchersDefault private val dispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        private val cities = listOf("Gdańsk", "Warszawa", "Poznań", "Białystok", "Wrocław", "Katowice", "Kraków")
        private val colors = listOf("Yellow", "Green", "Blue", "Red", "Black", "White")
    }

    val list: LiveData<List<CityColorCombination>> get() = _list.map { it }
    val detailsEvent: LiveData<Event<CityColorCombination>> get() = _detailsEvent
    private val _list = MutableLiveData<MutableList<CityColorCombination>>(mutableListOf())
    private val _detailsEvent = MutableLiveData<Event<CityColorCombination>>()
    private var job: Job? = null

    fun startGenerator() {
        job = viewModelScope.launch(dispatcher) {
            while (true) {
                generateNext()
                delay(5000)
            }
        }
    }

    private fun generateNext() {
        val city = cities.random()
        val color = colors.random()
        val newElement = CityColorCombination(city, color, Date())
        val list = _list.value ?: mutableListOf()
        val index = list.indexOfLast { element ->
            element.city <= newElement.city
        }
        list.add(index + 1, newElement)
        _list.postValue(list)
    }

    fun stopGenerator() {
        job?.run {
            cancel()
            job = null
        }
    }

    fun selectItem(position: Int) {
        val item = _list.value?.getOrNull(position) ?: return
        _detailsEvent.setEvent(item)
    }
}