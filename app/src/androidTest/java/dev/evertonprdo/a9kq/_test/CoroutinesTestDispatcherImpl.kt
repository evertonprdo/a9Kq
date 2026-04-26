@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.evertonprdo.a9kq._test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

class CoroutinesTestDispatcherImpl(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : CoroutinesTestDispatcher {
    override fun testDispatcher(): TestDispatcher = dispatcher

    @Before
    override fun starting() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    override fun finished() {
        Dispatchers.resetMain()
    }
}