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
import dev.evertonprdo.a9kq.domain.usecases.UndoRemoveMeterReadingUseCase
import dev.evertonprdo.a9kq.features.meterreading.extensions.key
import dev.evertonprdo.a9kq.libs.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeterReadingListViewModel(
    private val meterReadingHistoryUseCase: MeterReadingHistoryUseCase,
    private val removeMeterReadingUseCase: RemoveMeterReadingUseCase,
    private val undoRemoveMeterReadingUseCase: UndoRemoveMeterReadingUseCase,
    private val appScope: AppCoroutineScope
) : ViewModel() {
    private val _events = Channel<MeterReadingListEvent>()
    val events = _events.receiveAsFlow()

    private val localPendingToRemove = mutableListOf<Long>()
    val uiState: StateFlow<MeterReadingListUiState> = meterReadingHistoryUseCase()
        .map { MeterReadingListUiState.toStandard(it) }
        .catch { emit(MeterReadingListUiState.toFailure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MeterReadingListUiState.Loading
        )

    fun onAction(action: MeterReadingListAction) {
        when (action) {
            is MeterReadingListAction.RemoveMeterReading -> remove(action.read)
            MeterReadingListAction.UndoRemove -> undoRemove()
            MeterReadingListAction.ClearLocalPendingToRemove -> localPendingToRemove.clear()
        }
    }

    private fun remove(read: MeterReading) {
        val key = read.key
        localPendingToRemove.add(key)

        viewModelScope.launch {
            removeMeterReadingUseCase(key)
            _events.send(MeterReadingListEvent.ReadingRemoved)
        }
    }

    private fun undoRemove() {
        appScope.launch { undoRemoveMeterReadingUseCase() }
        localPendingToRemove.clear()
    }

    companion object {
        private val Factory = viewModelFactory {

            val meterReadingHistoryUseCase = MeterReadingHistoryUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )
            val removeMeterReadingUseCase = RemoveMeterReadingUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository,
            )
            val undoRemoveMeterReadingUseCase = UndoRemoveMeterReadingUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )

            initializer {
                MeterReadingListViewModel(
                    meterReadingHistoryUseCase = meterReadingHistoryUseCase,
                    removeMeterReadingUseCase = removeMeterReadingUseCase,
                    undoRemoveMeterReadingUseCase = undoRemoveMeterReadingUseCase,
                    appScope = ServiceLocator.appCoroutineScope
                )
            }
        }

        @Composable
        fun create() = viewModel<MeterReadingListViewModel>(factory = Factory)
    }
}