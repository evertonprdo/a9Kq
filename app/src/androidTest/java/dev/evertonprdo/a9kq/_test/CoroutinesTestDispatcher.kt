@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.evertonprdo.a9kq._test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import org.junit.After
import org.junit.Before

interface CoroutinesTestDispatcher {

    fun testDispatcher(): TestDispatcher

    @Before
    fun starting()

    @After
    fun finished()
}