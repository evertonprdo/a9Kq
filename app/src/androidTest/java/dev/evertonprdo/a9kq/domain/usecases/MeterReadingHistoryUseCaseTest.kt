@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.TestWithDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MeterReadingHistoryUseCaseTest : TestWithDatabase() {

    lateinit var meterReadingHistoryUseCase: MeterReadingHistoryUseCase

    @Before
    fun setup() {
        meterReadingHistoryUseCase = MeterReadingHistoryUseCase(meterReadingRepository)
    }

    @Test
    fun historyUpdatesWhenReadingsAdded() = runTest {
        val history = meterReadingHistoryUseCase()
        suspend fun verifyHistorySize(size: Int) {
            advanceUntilIdle()
            val state = history.first()
            advanceUntilIdle()
            Assert.assertEquals(size, state.size)
        }

        verifyHistorySize(0)

        addMeterReadingEntry(0, 0)
        verifyHistorySize(1)

        addMeterReadingEntry(10, 1)
        addMeterReadingEntry(20, 2)
        verifyHistorySize(3)
    }

    @Test
    fun historyIsSortedByDescending() = runTest {
        repeat(10) { addMeterReadingEntry(it.toLong(), it) }
        val history = meterReadingHistoryUseCase().first()

        Assert.assertEquals(
            history.sortedByDescending { it.readAt },
            history
        )
    }
}