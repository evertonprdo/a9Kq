package dev.evertonprdo.a9kq.features.meterreading.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.data.old.EnergyRepository
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.libs.KWh
import dev.evertonprdo.a9kq.domain.old.EnergyRecord
import kotlinx.coroutines.launch
import kotlin.time.Clock

class AddEnergyViewModel(
    private val energyRepository: EnergyRepository
) : ViewModel() {

    var amount by mutableStateOf<Int?>(null)

    fun onAmountChange(value: Int?) {
        amount = value
    }

    fun canBeSubmitted(): Boolean = amount != null

    fun submit() {
        val newRecord = amount ?: return
        val kWh = KWh(newRecord)

        val record = EnergyRecord(
            instant = Clock.System.now(),
            amount = kWh
        )

        viewModelScope.launch {
            energyRepository.addRecord(record)
        }
    }

    companion object {
        val factory = viewModelFactory {

            initializer {
                AddEnergyViewModel(
                    energyRepository = ServiceLocator.energyRepository
                )
            }
        }
    }
}