package dev.evertonprdo.a9kq

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.evertonprdo.a9kq.features.bill.add.AddBillScreen
import dev.evertonprdo.a9kq.features.bill.list.BillListScreen
import dev.evertonprdo.a9kq.features.energy.add.AddEnergyScreen
import dev.evertonprdo.a9kq.features.energy.list.ListEnergyScreen

@Composable
fun App() {

    val backStack = rememberSaveable { mutableStateListOf<Any>("/") }
    val focusManager = LocalFocusManager.current

    Scaffold { contentPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .padding(contentPadding)

        ) { key ->
            when (key) {
                "/" -> NavEntry(key) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button({ backStack.add("/records") }) { Text("Records ->") }
                        Button({ backStack.add("/bills") }) { Text("Bills ->") }
                    }
                }

                "/records" -> NavEntry(key) { ListEnergyScreen { backStack.add("/record/add") } }
                "/record/add" -> NavEntry(key) { AddEnergyScreen { backStack.removeLastOrNull() } }
                "/bills" -> NavEntry(key) { BillListScreen { backStack.add("/bill/add") } }
                "/bill/add" -> NavEntry(key) { AddBillScreen { backStack.removeLastOrNull() } }

                else -> NavEntry(Unit) { Text("404") }
            }
        }
    }
}