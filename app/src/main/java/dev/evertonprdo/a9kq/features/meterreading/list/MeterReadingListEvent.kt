package dev.evertonprdo.a9kq.features.meterreading.list

sealed interface MeterReadingListEvent {
    data object ReadingRemoved : MeterReadingListEvent
    data object RemoveFailure : MeterReadingListEvent
}