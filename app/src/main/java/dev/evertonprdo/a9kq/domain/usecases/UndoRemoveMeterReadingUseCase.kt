package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class UndoRemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository
) {

    suspend operator fun invoke() =
        meterReadingRepository.undoPendingRemoval()
}