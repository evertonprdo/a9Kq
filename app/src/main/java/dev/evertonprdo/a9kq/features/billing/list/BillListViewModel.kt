package dev.evertonprdo.a9kq.features.billing.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.data.old.BillRepository
import dev.evertonprdo.a9kq.di.ServiceLocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BillListViewModel(
    private val repository: BillRepository
) : ViewModel() {

    val bills = repository.bills.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    companion object {

        private val Factory = viewModelFactory {
            initializer {
                BillListViewModel(
                    repository = ServiceLocator.billRepository
                )
            }
        }

        @Composable
        fun create() =
            viewModel<BillListViewModel>(factory = Factory)
    }
}