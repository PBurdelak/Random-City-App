package com.pburdelak.randomcityapp.screen.activity

import com.jraska.livedata.test
import com.pburdelak.randomcityapp.TestBase
import com.pburdelak.randomcityapp.assertEventValue
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.model.Error
import com.pburdelak.randomcityapp.repository.ListRepository
import com.pburdelak.randomcityapp.utils.livedata.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.util.*

class CombinationProducerViewModelTest: TestBase() {

    private lateinit var repository: ListRepository
    private lateinit var dispatcher: TestCoroutineDispatcher
    private lateinit var sut: CombinationProducerViewModel

    @Before
    fun setUp() {
        dispatcher = TestCoroutineDispatcher()
        repository = mock()
        sut = CombinationProducerViewModel(repository, dispatcher)
    }

    @After
    fun tearDown() {
        sut.stopGenerator()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun start_success() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        val result = listOf<CityColorCombination>(mock(), mock())
        whenever(repository.getAllCombinations()).thenReturn(flowOf(result))

        verify(repository, never()).getAllCombinations()
        listObserver.assertValue(emptyList())
        errorObserver.assertNoValue()

        sut.start()

        verify(repository).getAllCombinations()
        listObserver.assertValue(result)
        errorObserver.assertNoValue()
    }

    @Test
    fun start_error() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        whenever(repository.getAllCombinations()).thenReturn(flow { throw Exception() })

        verify(repository, never()).getAllCombinations()
        listObserver.assertValue(emptyList())
        errorObserver.assertNoValue()

        sut.start()

        verify(repository).getAllCombinations()
        listObserver.assertValue(emptyList())
        errorObserver.assertEventValue(Error.Retrieving)
    }

    @Test
    fun selectItem() {
        val observer = sut.detailsEvent.test()
        val list = listOf<CityColorCombination>(mock(), mock())
        whenever(repository.getAllCombinations()).thenReturn(flowOf(list))
        sut.start()

        observer.assertNoValue()
        sut.selectItem(0)
        observer.assertEventValue(list[0])
    }

    @Test
    fun selectItem_outOfBounds() {
        val observer = sut.detailsEvent.test()
        val list = listOf<CityColorCombination>(mock(), mock())
        whenever(repository.getAllCombinations()).thenReturn(flowOf(list))
        sut.start()

        observer.assertNoValue()
        sut.selectItem(-1)
        observer.assertNoValue()
        sut.selectItem(list.size)
        observer.assertNoValue()
    }

    @Test
    fun clearData_success() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        val list = listOf<CityColorCombination>(mock(), mock())
        whenever(repository.getAllCombinations()).thenReturn(flowOf(list))
        whenever(repository.clearSavedData()).thenReturn(flowOf(Unit))
        sut.start()

        verify(repository, never()).clearSavedData()
        listObserver.assertValue(list)
        errorObserver.assertNoValue()

        sut.clearData()

        verify(repository).clearSavedData()
        listObserver.assertValue(emptyList())
        errorObserver.assertNoValue()
    }

    @Test
    fun clearData_error() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        val list = listOf<CityColorCombination>(mock(), mock())
        whenever(repository.getAllCombinations()).thenReturn(flowOf(list))
        whenever(repository.clearSavedData()).thenReturn(flow { throw Exception() })
        sut.start()

        verify(repository, never()).clearSavedData()
        listObserver.assertValue(list)
        errorObserver.assertNoValue()

        sut.clearData()

        verify(repository).clearSavedData()
        listObserver.assertValue(emptyList())
        errorObserver.assertEventValue(Error.Deleting)
    }

    @Test
    fun startGenerator() {
        val observer = sut.list.test()
        observer.assertValue(emptyList())

        sut.startGenerator()

        observer.assertValue(emptyList())

        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod)
        observer.assertValue { it.size == 1 }

        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod * 2)
        observer.assertValue { it.size == 3 }
    }

    @Test
    fun stopGenerator() {
        val observer = sut.list.test()
        sut.startGenerator()
        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod)
        observer.assertValue { it.size == 1 }

        sut.stopGenerator()
        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod * 2)
        observer.assertValue { it.size == 1 }
    }

    @Test
    fun saveNewItem_success() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        whenever(repository.saveCombination(any())).thenReturn(flowOf(Unit))

        verify(repository, never()).saveCombination(any())
        errorObserver.assertNoValue()

        sut.startGenerator()
        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod)

        listObserver.assertValue { it.size == 1 }
        errorObserver.assertNoValue()
        verify(repository).saveCombination(any())
        verify(repository).saveCombination(listObserver.value().first())
    }

    @Test
    fun saveNewItem_error() {
        val listObserver = sut.list.test()
        val errorObserver = sut.errorMessageEvent.test()
        whenever(repository.saveCombination(any())).thenReturn(flow { throw Exception() })

        verify(repository, never()).saveCombination(any())
        errorObserver.assertNoValue()

        sut.startGenerator()
        dispatcher.advanceTimeBy(CombinationProducerViewModel.generatorPeriod)

        listObserver.assertValue { it.size == 1 }
        errorObserver.assertEventValue(Error.Saving)
        verify(repository).saveCombination(any())
        verify(repository).saveCombination(listObserver.value().first())
    }
}