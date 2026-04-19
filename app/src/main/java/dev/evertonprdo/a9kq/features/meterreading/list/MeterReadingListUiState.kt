package dev.evertonprdo.a9kq.features.meterreading.list

import dev.evertonprdo.a9kq.domain.entities.vo.MeterReadingHistory

sealed interface MeterReadingListUiState {

    data class Standard(
        val history: MeterReadingHistory = emptyList()
    ) : MeterReadingListUiState

    data object Loading : MeterReadingListUiState
    data class Failure(val cause: Throwable) : MeterReadingListUiState

    companion object {
        fun toStandard(history: MeterReadingHistory): MeterReadingListUiState = Standard(history)
        fun toFailure(cause: Throwable): MeterReadingListUiState = Failure(cause)
    }
}