package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class RemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository
) {

    suspend operator fun invoke(meterReading: MeterReading) {
        if (meterReadingRepository.exists(meterReading))
            throw IllegalArgumentException("The reading doesn't exist")

        meterReadingRepository.remove(meterReading)
    }
}