package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeterReadingListViewModel(
    meterReadingRepository: MeterReadingRepository
) : ViewModel() {

    val meterReadings: StateFlow<List<MeterReading>> =
        meterReadingRepository.getAll()
            .map { it.asReversed() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    companion object {
        val factory = viewModelFactory {

            initializer {
                MeterReadingListViewModel(
                    meterReadingRepository = ServiceLocator.meterReadingRepository
                )
            }
        }
    }
}