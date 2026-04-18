package dev.evertonprdo.a9kq.features.meterreading.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.usecases.ReadMeterUseCase
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.launch
import kotlin.time.Clock

class AddMeterReadingViewModel(
    private val readMeterUseCase: ReadMeterUseCase
) : ViewModel() {

    var amount by mutableStateOf<Int?>(null)

    fun onAmountChange(value: Int?) {
        amount = value
    }

    fun canBeSubmitted(): Boolean = amount != null

    fun submit() {
        val newRecord = amount ?: return

        val read = MeterReading(
            readAt = Clock.System.now(),
            meterIndex = KWh(newRecord)
        )

        viewModelScope.launch {
            readMeterUseCase(read)
        }
    }

    companion object {
        val factory = viewModelFactory {

            val readMeterUseCase = ReadMeterUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository
            )

            initializer {
                AddMeterReadingViewModel(
                    readMeterUseCase = readMeterUseCase
                )
            }
        }
    }
}