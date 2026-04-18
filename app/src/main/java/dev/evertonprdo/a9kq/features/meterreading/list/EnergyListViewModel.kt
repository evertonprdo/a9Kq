package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.data.old.EnergyRepository
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.old.EnergyRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EnergyListViewModel(
    energyRepository: EnergyRepository
) : ViewModel() {

    val records: StateFlow<List<EnergyRecord>> = energyRepository.records.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    companion object {
        val factory = viewModelFactory {

            initializer {
                EnergyListViewModel(
                    energyRepository = ServiceLocator.energyRepository
                )
            }
        }
    }
}