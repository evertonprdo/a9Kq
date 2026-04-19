package dev.evertonprdo.a9kq.features.meterreading.add

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.usecases.ReadMeterUseCase
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class AddMeterReadingViewModel(
    private val readMeterUseCase: ReadMeterUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddMeterUiState> = MutableStateFlow(AddMeterUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AddMeterReadingEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AddMeterReadingAction) {
        when (action) {
            AddMeterReadingAction.DismissDialog -> dismissDialog()
            is AddMeterReadingAction.OnMeterReadingIndexChange -> {
                updateMeterIndex(action.value)
            }

            AddMeterReadingAction.Submit -> submit()
        }
    }

    private fun updateMeterIndex(value: Int?) {
        _uiState.update { it.copy(meterIndex = value) }
    }

    private fun submit() {
        val newRecord = uiState.value.meterIndex ?: return

        val read = MeterReading(
            readAt = Clock.System.now(),
            meterIndex = KWh(newRecord)
        )

        viewModelScope.launch {
            _uiState.update { it.copy(submissionState = AddMeterUiState.toSubmitting()) }
            delay(2000)

            try {
                readMeterUseCase(read)
                dismissDialog()
                _events.send(AddMeterReadingEvent.Submitted)
            } catch (e: Exception) {
                _uiState.update { it.copy(submissionState = AddMeterUiState.toFailure(e)) }
            }
        }
    }

    private fun dismissDialog() =
        _uiState.update { it.copy(submissionState = AddMeterUiState.toIdle()) }

    companion object {
        private val Factory = viewModelFactory {

            val readMeterUseCase = ReadMeterUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )

            initializer {
                AddMeterReadingViewModel(
                    readMeterUseCase = readMeterUseCase,
                )
            }
        }

        @Composable
        fun create() = viewModel<AddMeterReadingViewModel>(factory = Factory)
    }
}