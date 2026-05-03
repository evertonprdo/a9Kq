@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.TestWithDatabase
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.Instant

class MeterReadingHistoryUseCaseTest : TestWithDatabase() {

    lateinit var meterReadingHistoryUseCase: MeterReadingHistoryUseCase

    @Before
    fun setup() {
        meterReadingHistoryUseCase = MeterReadingHistoryUseCase(meterReadingRepository)
    }

    suspend fun addEntry(n: Int) {
        meterReadingRepository.add(
            read = MeterReading(Instant.fromEpochSeconds(n.toLong()), KWh(n))
        )
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

        addEntry(0)
        verifyHistorySize(1)

        addEntry(10)
        addEntry(12)
        verifyHistorySize(3)
    }

    @Test
    fun historyIsSortedByDescending() = runTest {
        repeat(10) { addEntry(it) }
        val history = meterReadingHistoryUseCase().first()

        Assert.assertEquals(
            history.sortedByDescending { it.readAt },
            history
        )
    }
}