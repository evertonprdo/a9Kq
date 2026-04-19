package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.domain.entities.vo.MeterReadingHistory
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import kotlinx.coroutines.flow.Flow

class MeterReadingHistoryUseCase(
    private val meterReadingRepository: MeterReadingRepository
) {

    operator fun invoke(): Flow<MeterReadingHistory> = meterReadingRepository.getHistory()
}