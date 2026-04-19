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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class AddMeterReadingViewModel(
    private val readMeterUseCase: ReadMeterUseCase,
    private val onSubmit: () -> Unit
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddMeterUiState> = MutableStateFlow(AddMeterUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddMeterAction) {
        when (action) {
            AddMeterAction.DismissDialog -> dismissDialog()
            is AddMeterAction.OnMeterIndexChange -> {
                updateMeterIndex(action.value)
            }

            AddMeterAction.Submit -> submit()
        }
    }

    fun updateMeterIndex(value: Int?) {
        _uiState.update { it.copy(meterIndex = value) }
    }

    fun submit() {
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
                onSubmit()
            } catch (e: Exception) {
                _uiState.update { it.copy(submissionState = AddMeterUiState.toFailure(e)) }
            }
        }
    }

    fun dismissDialog() = _uiState.update { it.copy(submissionState = AddMeterUiState.toIdle()) }

    companion object {
        private fun factory(onSubmit: () -> Unit) = viewModelFactory {

            val readMeterUseCase = ReadMeterUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )

            initializer {
                AddMeterReadingViewModel(
                    readMeterUseCase = readMeterUseCase,
                    onSubmit = onSubmit
                )
            }
        }

        @Composable
        fun create(onSubmit: () -> Unit) =
            viewModel<AddMeterReadingViewModel>(factory = factory(onSubmit))
    }
}