package com.pburdelak.randomcityapp.screen.activity

import androidx.lifecycle.*
import com.pburdelak.randomcityapp.hilt.DispatchersDefault
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.model.Error
import com.pburdelak.randomcityapp.repository.ListRepository
import com.pburdelak.randomcityapp.utils.livedata.Event
import com.pburdelak.randomcityapp.utils.livedata.setEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CombinationProducerViewModel @Inject constructor(
    private val repository: ListRepository,
    @DispatchersDefault private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        const val generatorPeriod = 5000L
        private val cities = listOf("Gdańsk", "Warszawa", "Poznań", "Białystok", "Wrocław", "Katowice", "Kraków")
        private val colors = listOf("Yellow", "Green", "Blue", "Red", "Black", "White")
    }

    val list: LiveData<List<CityColorCombination>> get() = _list.map { it }
    val detailsEvent: LiveData<Event<CityColorCombination>> get() = _detailsEvent
    val errorMessageEvent: LiveData<Event<Error>> get() = _errorMessageEvent

    private val _list = MutableLiveData<MutableList<CityColorCombination>>(mutableListOf())
    private val _detailsEvent = MutableLiveData<Event<CityColorCombination>>()
    private val _errorMessageEvent = MutableLiveData<Event<Error>>()
    private var job: Job? = null
    private var isInitialized = false

    fun start() {
        if (isInitialized) return
        isInitialized = true
        loadData()
    }

    private fun loadData() {
        repository.getAllCombinations()
            .catch {
                _errorMessageEvent.setEvent(Error.Retrieving)
            }
            .onEach {
                _list.value = it.toMutableList()
            }
            .launchIn(viewModelScope)
    }

    fun startGenerator() {
        if (job?.isActive == true) return
        job = viewModelScope.launch(dispatcher) {
            while (isActive) {
                delay(generatorPeriod)
                generateNext()
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
        saveItem(newElement)
        _list.postValue(list)
    }

    private fun saveItem(item: CityColorCombination) {
        repository.saveCombination(item)
            .catch {
                _errorMessageEvent.setEvent(Error.Saving)
            }
            .launchIn(viewModelScope)
    }

    fun stopGenerator() {
        job?.run {
            cancel()
            this@CombinationProducerViewModel.job = null
        }
    }

    fun selectItem(position: Int) {
        val item = _list.value?.getOrNull(position) ?: return
        _detailsEvent.setEvent(item)
    }

    fun clearData() {
        stopGenerator()
        _list.value = mutableListOf()
        clearSavedData()
        startGenerator()
    }

    private fun clearSavedData() {
        repository.clearSavedData()
            .catch {
                _errorMessageEvent.setEvent(Error.Deleting)
            }
            .launchIn(viewModelScope)
    }
}