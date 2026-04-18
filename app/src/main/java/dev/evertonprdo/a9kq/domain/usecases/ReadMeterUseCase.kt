package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class ReadMeterUseCase(
    private val meterReadingRepository: MeterReadingRepository
) {

    suspend operator fun invoke(reading: MeterReading) {
        val lastRead = meterReadingRepository.getMostRecent()
        lastRead?.let {
            if (reading.meterIndex < it.meterIndex)
                throw IllegalArgumentException("Meter index cannot decrease.")
        }

        meterReadingRepository.add(reading)
    }
}