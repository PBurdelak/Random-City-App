package com.pburdelak.randomcityapp.screen.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pburdelak.randomcityapp.model.CityColorCombination

class DetailsViewModel: ViewModel() {

    private val _currentItem = MutableLiveData<CityColorCombination>()
    val currentItem: LiveData<CityColorCombination> get() = _currentItem

    fun setCurrentItem(item: CityColorCombination) {
        _currentItem.value = item
    }
}