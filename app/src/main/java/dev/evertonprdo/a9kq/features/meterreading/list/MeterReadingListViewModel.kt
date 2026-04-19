package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.usecases.MeterReadingHistoryUseCase
import dev.evertonprdo.a9kq.domain.usecases.RemoveMeterReadingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeterReadingListViewModel(
    private val meterReadingHistoryUseCase: MeterReadingHistoryUseCase,
    private val removeMeterReadingUseCase: RemoveMeterReadingUseCase
) : ViewModel() {
    val uiState: StateFlow<MeterReadingListUiState> = meterReadingHistoryUseCase()
        .map { MeterReadingListUiState.toStandard(it) }
        .catch { emit(MeterReadingListUiState.toFailure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MeterReadingListUiState.Loading
        )

    companion object {
        private val Factory = viewModelFactory {

            val meterReadingHistoryUseCase = MeterReadingHistoryUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )
            val removeMeterReadingUseCase = RemoveMeterReadingUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )

            initializer {
                MeterReadingListViewModel(
                    meterReadingHistoryUseCase = meterReadingHistoryUseCase,
                    removeMeterReadingUseCase = removeMeterReadingUseCase
                )
            }
        }

        @Composable
        fun create() = viewModel<MeterReadingListViewModel>(factory = Factory)
    }
}