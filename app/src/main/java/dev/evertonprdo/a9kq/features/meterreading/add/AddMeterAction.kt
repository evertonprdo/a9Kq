package dev.evertonprdo.a9kq.features.meterreading.add

sealed interface AddMeterAction {
    data object DismissDialog : AddMeterAction
    data class OnMeterIndexChange(val value: Int?) : AddMeterAction
    data object Submit : AddMeterAction
}