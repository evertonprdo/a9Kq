package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.usecases.MeterReadingHistoryUseCase
import dev.evertonprdo.a9kq.domain.usecases.RemoveMeterReadingUseCase
import dev.evertonprdo.a9kq.features.meterreading.extensions.MeterReadingKey
import dev.evertonprdo.a9kq.features.meterreading.extensions.key
import dev.evertonprdo.a9kq.libs.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MeterReadingListViewModel(
    private val meterReadingHistoryUseCase: MeterReadingHistoryUseCase,
    private val removeMeterReadingUseCase: RemoveMeterReadingUseCase,
    private val appScope: AppCoroutineScope
) : ViewModel() {
    private val _events = Channel<MeterReadingListEvent>()
    val events = _events.receiveAsFlow()

    private val pendingRemovals = MutableStateFlow<List<MeterReadingKey>>(emptyList())
    val uiState: StateFlow<MeterReadingListUiState> = meterReadingHistoryUseCase()
        .combine(pendingRemovals) { list, pend -> list.filterNot { it.key in pend } }
        .map { MeterReadingListUiState.toStandard(it) }
        .catch { emit(MeterReadingListUiState.toFailure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MeterReadingListUiState.Loading
        )

    fun onAction(action: MeterReadingListAction) {
        when (action) {
            is MeterReadingListAction.RemoveMeterReading -> markToRemove(action.read)

            MeterReadingListAction.UndoRemove -> pendingRemovals.update { emptyList() }

            MeterReadingListAction.DispatchRemovedMeterReadings -> removeMeterReadings()
        }
    }

    private fun markToRemove(read: MeterReading) {
        val key = read.key
        pendingRemovals.update { it + key }
        viewModelScope.launch { _events.send(MeterReadingListEvent.ReadMarkedToRemove) }
    }

    private fun removeMeterReadings() {
        val keysToRemove = pendingRemovals.value
        if (keysToRemove.isEmpty()) return

        // AppScope ensures this work finishes regardless of the UI lifecycle
        appScope.launch {
            runCatching { removeMeterReadingUseCase(keysToRemove) }
                .onFailure { _events.send(MeterReadingListEvent.RemoveFailure) }
        }
    }

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
                    removeMeterReadingUseCase = removeMeterReadingUseCase,
                    appScope = ServiceLocator.appCoroutineScope
                )
            }
        }

        @Composable
        fun create() = viewModel<MeterReadingListViewModel>(factory = Factory)
    }
}