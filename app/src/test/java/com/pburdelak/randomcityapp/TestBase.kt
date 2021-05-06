package com.pburdelak.randomcityapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.ClassRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

abstract class TestBase {

    companion object {
        @ClassRule
        @JvmField
        val rule = InstantTaskExecutorRule()

        @ClassRule
        @JvmField
        val mainCoroutineRule = MainCoroutineRule()
    }

    class MainCoroutineRule(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    ) : TestWatcher() {

        override fun starting(description: Description?) {
            super.starting(description)
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description?) {
            super.finished(description)
            Dispatchers.resetMain()
            testDispatcher.cleanupTestCoroutines()
        }
    }
}
