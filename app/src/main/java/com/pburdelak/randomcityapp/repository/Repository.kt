package com.pburdelak.randomcityapp.repository

import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.room.CityColorCombinationDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class Repository(
    private val dispatcher: CoroutineDispatcher,
    private val cityColorCombinationDao: CityColorCombinationDao
): ListRepository {

    private fun <T> flowRequest(block: suspend FlowCollector<T>.() -> T): Flow<T> = flow {
        emit(block())
    }.flowOn(dispatcher)

    override fun getAllCombinations(): Flow<List<CityColorCombination>> = flowRequest {
        cityColorCombinationDao.getAll()
    }

    override fun saveCombination(item: CityColorCombination): Flow<Unit> = flowRequest {
        cityColorCombinationDao.insert(item)
    }

    override fun clearSavedData(): Flow<Unit> = flowRequest {
        cityColorCombinationDao.deleteAll()
    }
}