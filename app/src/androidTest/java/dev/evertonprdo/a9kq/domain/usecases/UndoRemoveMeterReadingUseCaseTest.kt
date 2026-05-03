package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.TestWithDatabase
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Instant

class UndoRemoveMeterReadingUseCaseTest : TestWithDatabase() {

    lateinit var undoRemoveMeterReadingUseCase: UndoRemoveMeterReadingUseCase

    @Before
    fun setup() {
        undoRemoveMeterReadingUseCase = UndoRemoveMeterReadingUseCase(
            meterReadingRepository = meterReadingRepository
        )
    }

    suspend fun addEntry(n: Int) {
        meterReadingRepository.add(
            read = MeterReading(Instant.fromEpochSeconds(n.toLong()), KWh(n))
        )
    }

    @Test
    fun undoRemoveMeterReading_restoresEntry() = runTest {
        val key = 0L
        addEntry(key.toInt())

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
        addEntry(key.toInt())

        var entries = meterReadingRepository.getHistory().first()
        assert(entries.isNotEmpty())

        meterReadingRepository.remove(key)

        entries = meterReadingRepository.getHistory().first()
        assert(entries.isEmpty())

        addEntry(2)
        undoRemoveMeterReadingUseCase(key)
    }
}