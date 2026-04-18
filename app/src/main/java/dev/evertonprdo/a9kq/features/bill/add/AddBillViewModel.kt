package dev.evertonprdo.a9kq.features.bill.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.evertonprdo.a9kq.data.BillRepository
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.lib.KWh
import dev.evertonprdo.a9kq.lib.Money
import dev.evertonprdo.a9kq.model.Bill
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AddBillViewModel(private val repository: BillRepository) : ViewModel() {

    var currRead by mutableStateOf("")
    var nextRead by mutableStateOf("")
    var amount by mutableStateOf("")
    var total by mutableStateOf("")

    val isCurrReadValid: Boolean
        get() =
            runCatching { LocalDate.parse(currRead) }.isSuccess

    val isNextReadValid: Boolean
        get() =
            runCatching { LocalDate.parse(nextRead) }.isSuccess

    val canSubmit get() = isCurrReadValid && isNextReadValid

    fun submit() {
        if (!canSubmit)
            return

        val bill = Bill(
            currRead = LocalDate.parse(currRead),
            nextRead = LocalDate.parse(nextRead),
            amount = KWh(amount.toInt()),
            total = Money(total.toLong())
        )

        viewModelScope.launch {
            repository.addRecord(bill)
        }
    }

    companion object {

        private val Factory = viewModelFactory {
            initializer {
                AddBillViewModel(
                    repository = ServiceLocator.billRepository
                )
            }
        }

        @Composable
        fun create() = viewModel<AddBillViewModel>(factory = Factory)
    }
}