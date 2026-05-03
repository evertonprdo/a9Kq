package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class UndoRemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository
) {

    suspend operator fun invoke(vararg keys: Long) {
        if (keys.isEmpty() || meterReadingRepository.canRestore(*keys))
            meterReadingRepository.undoPendingRemoval(*keys)
        else
            throw IllegalStateException("Cannot undo remove")
    }
}