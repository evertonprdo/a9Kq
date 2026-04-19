package dev.evertonprdo.a9kq.features.meterreading.add

sealed interface AddMeterReadingEvent {
    data object Submitted : AddMeterReadingEvent
}