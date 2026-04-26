package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import dev.evertonprdo.a9kq.libs.KWh
import kotlin.time.Clock

class ReadMeterUseCase(
    private val meterReadingRepository: MeterReadingRepository,
    private val clock: Clock = Clock.System
) {

    suspend operator fun invoke(meterIndex: KWh) {
        require(meterIndex >= 0) { "Meter Index should be positive" }

        val lastRead = meterReadingRepository.getMostRecent()
        lastRead?.let {
            if (meterIndex < it.meterIndex)
                throw IllegalArgumentException("Meter index cannot decrease.")
        }

        val reading = MeterReading(
            readAt = clock.now(),
            meterIndex = meterIndex
        )

        meterReadingRepository.add(reading)
    }
}