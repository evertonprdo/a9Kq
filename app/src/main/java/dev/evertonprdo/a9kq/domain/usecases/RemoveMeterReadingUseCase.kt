package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class RemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository,
) {

    suspend operator fun invoke(vararg keys: Long) =
        meterReadingRepository.remove(*keys)
}