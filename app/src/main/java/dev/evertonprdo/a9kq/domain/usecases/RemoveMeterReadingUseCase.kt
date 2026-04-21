package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class RemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository,
) {

    suspend operator fun invoke(keys: List<Long>) =
        meterReadingRepository.remove(keys)

    suspend operator fun invoke(key: Long) =
        meterReadingRepository.remove(key)
}