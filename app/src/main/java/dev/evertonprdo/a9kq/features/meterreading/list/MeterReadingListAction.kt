package dev.evertonprdo.a9kq.features.meterreading.list

import dev.evertonprdo.a9kq.domain.entities.MeterReading

sealed interface MeterReadingListAction {
    data class RemoveMeterReading(val read: MeterReading) : MeterReadingListAction
    data object UndoRemove : MeterReadingListAction
    data object DispatchRemovedMeterReadings : MeterReadingListAction
}