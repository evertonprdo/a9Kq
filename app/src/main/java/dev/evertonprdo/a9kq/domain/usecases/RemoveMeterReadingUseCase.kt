package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq.data.datastore.PendingMeterReadingsDataStore
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import kotlinx.coroutines.flow.first

class RemoveMeterReadingUseCase(
    private val meterReadingRepository: MeterReadingRepository,
    private val pendingMeterReadingsDataStore: PendingMeterReadingsDataStore
) {

    suspend operator fun invoke(meterReading: MeterReading) {
        if (meterReadingRepository.exists(meterReading))
            throw IllegalArgumentException("The reading doesn't exist")

        meterReadingRepository.remove(meterReading)
    }

    suspend operator fun invoke(readAt: List<Long>) {
        meterReadingRepository.remove(readAt)
        pendingMeterReadingsDataStore.updatePendingMeterReadings(emptyList())
    }

    suspend operator fun invoke() {
        val pendingRemovals =
            pendingMeterReadingsDataStore.loadPendingMeterReadings().first()

        if (pendingRemovals.isEmpty())
            return

        this(pendingRemovals)
    }
}