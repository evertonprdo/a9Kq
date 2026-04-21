package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.data.datastore.PendingMeterReadingsDataStore
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.usecases.MeterReadingHistoryUseCase
import dev.evertonprdo.a9kq.domain.usecases.RemoveMeterReadingUseCase
import dev.evertonprdo.a9kq.features.meterreading.extensions.key
import dev.evertonprdo.a9kq.libs.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeterReadingListViewModel(
    private val meterReadingHistoryUseCase: MeterReadingHistoryUseCase,
    private val removeMeterReadingUseCase: RemoveMeterReadingUseCase,
    private val pendingStorage: PendingMeterReadingsDataStore,
    private val appScope: AppCoroutineScope
) : ViewModel() {
    private val _events = Channel<MeterReadingListEvent>()
    val events = _events.receiveAsFlow()

    val uiState: StateFlow<MeterReadingListUiState> =
        combine(
            meterReadingHistoryUseCase(),
            pendingStorage.loadPendingMeterReadings()
        ) { list, pend -> list.filterNot { it.key in pend } }
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
            MeterReadingListAction.UndoRemove -> undoRemove()
            MeterReadingListAction.DispatchRemovedMeterReadings -> removeMeterReadings()
        }
    }

    private fun markToRemove(read: MeterReading) {
        viewModelScope.launch {
            pendingStorage.updatePendingMeterReadings(read.key)
            _events.send(MeterReadingListEvent.ReadMarkedToRemove)
        }
    }

    private fun removeMeterReadings() {
        // AppScope ensures this work finishes regardless of the UI lifecycle
        appScope.launch {
            val keysToRemove = pendingStorage.loadPendingMeterReadings().first()
            if (keysToRemove.isEmpty()) return@launch

            runCatching { removeMeterReadingUseCase(keysToRemove) }
                .onFailure { _events.send(MeterReadingListEvent.RemoveFailure) }
        }
    }

    private fun undoRemove() {
        appScope.launch { pendingStorage.clearPendingMeterReadings() }
    }

    companion object {
        private val Factory = viewModelFactory {

            val meterReadingHistoryUseCase = MeterReadingHistoryUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )
            val removeMeterReadingUseCase = RemoveMeterReadingUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository,
            )

            initializer {
                MeterReadingListViewModel(
                    meterReadingHistoryUseCase = meterReadingHistoryUseCase,
                    removeMeterReadingUseCase = removeMeterReadingUseCase,
                    pendingStorage = ServiceLocator.pendingMeterReadingsDataStore,
                    appScope = ServiceLocator.appCoroutineScope
                )
            }
        }

        @Composable
        fun create() = viewModel<MeterReadingListViewModel>(factory = Factory)
    }
}