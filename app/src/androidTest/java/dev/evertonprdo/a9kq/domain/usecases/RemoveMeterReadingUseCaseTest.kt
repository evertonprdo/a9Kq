package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.TestWithDatabase
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Instant

class RemoveMeterReadingUseCaseTest : TestWithDatabase() {

    lateinit var removeMeterReadingUseCase: RemoveMeterReadingUseCase

    @Before
    fun setup() {
        removeMeterReadingUseCase = RemoveMeterReadingUseCase(
            meterReadingRepository = meterReadingRepository
        )
    }

    suspend fun addEntry(n: Int) {
        meterReadingRepository.add(
            read = MeterReading(Instant.fromEpochSeconds(n.toLong()), KWh(n))
        )
    }

    @Test
    fun shouldRemoveMeterReading() = runTest {
        addEntry(0)

        var entries = meterReadingRepository.getHistory().first()
        assert(entries.isNotEmpty())

        removeMeterReadingUseCase(0)

        entries = meterReadingRepository.getHistory().first()
        assert(entries.isEmpty())
    }
}