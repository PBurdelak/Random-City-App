package com.pburdelak.randomcityapp.repository

import com.pburdelak.randomcityapp.model.CityColorCombination
import kotlinx.coroutines.flow.Flow

interface ListRepository {
    fun getAllCombinations(): Flow<List<CityColorCombination>>
    fun saveCombination(item: CityColorCombination): Flow<Unit>
    fun clearSavedData(): Flow<Unit>
}