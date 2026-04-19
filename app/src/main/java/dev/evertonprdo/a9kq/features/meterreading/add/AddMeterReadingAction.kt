package dev.evertonprdo.a9kq.features.meterreading.add

sealed interface AddMeterReadingAction {
    data object DismissDialog : AddMeterReadingAction
    data class OnMeterReadingIndexChange(val value: Int?) : AddMeterReadingAction
    data object Submit : AddMeterReadingAction
}