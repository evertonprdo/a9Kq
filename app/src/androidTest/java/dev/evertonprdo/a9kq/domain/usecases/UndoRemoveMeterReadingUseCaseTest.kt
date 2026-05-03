package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.TestWithDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UndoRemoveMeterReadingUseCaseTest : TestWithDatabase() {

    lateinit var undoRemoveMeterReadingUseCase: UndoRemoveMeterReadingUseCase

    @Before
    fun setup() {
        undoRemoveMeterReadingUseCase = UndoRemoveMeterReadingUseCase(
            meterReadingRepository = meterReadingRepository
        )
    }

    @Test
    fun undoRemoveMeterReading_restoresEntry() = runTest {
        val key = 0L
        addMeterReadingEntry(key, 0)

        var entries = meterReadingRepository.getHistory().first()
        assert(entries.isNotEmpty())

        meterReadingRepository.remove(key)

        entries = meterReadingRepository.getHistory().first()
        assert(entries.isEmpty())

        undoRemoveMeterReadingUseCase(key)

        entries = meterReadingRepository.getHistory().first()
        assert(entries.isNotEmpty())
    }

    @Test(expected = IllegalStateException::class)
    fun undoRemoveAfterNewEntryFails() = runTest {
        val key = 0L
        addMeterReadingEntry(key, 0)

        var entries = meterReadingRepository.getHistory().first()
        assert(entries.isNotEmpty())

        meterReadingRepository.remove(key)

        entries = meterReadingRepository.getHistory().first()
        assert(entries.isEmpty())

        addMeterReadingEntry(20, 2)
        undoRemoveMeterReadingUseCase(key)
    }
}