package dev.evertonprdo.a9kq.features.meterreading.add

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.usecases.ReadMeterUseCase
import dev.evertonprdo.a9kq.libs.KWh
import dev.evertonprdo.a9kq.libs.utils.Signaler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMeterReadingViewModel(
    private val readMeterUseCase: ReadMeterUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddMeterReadingUiState> =
        MutableStateFlow(AddMeterReadingUiState())
    val uiState = _uiState.asStateFlow()
    val successSignaler = Signaler()

    fun onAction(action: AddMeterReadingAction) {
        when (action) {
            AddMeterReadingAction.DismissDialog -> clearSubmissionState()
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
        val newReading = uiState.value.meterIndex?.let { KWh(it) } ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(submissionState = AddMeterReadingUiState.toSubmitting()) }

            try {
                readMeterUseCase(newReading)
                successSignaler.signal()
            } catch (e: Exception) {
                _uiState.update { it.copy(submissionState = AddMeterReadingUiState.toFailure(e)) }
            }
        }
    }

    private fun clearSubmissionState() =
        _uiState.update { it.copy(submissionState = AddMeterReadingUiState.toIdle()) }

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