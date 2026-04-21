package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository

class RemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository,
) {

    suspend operator fun invoke(readAt: List<Long>) {
        meterReadingRepository.remove(readAt)
    }

    suspend operator fun invoke() {
        println("TODO")
    }
}