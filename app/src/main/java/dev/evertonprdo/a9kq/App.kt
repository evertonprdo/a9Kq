package dev.evertonprdo.a9kq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.evertonprdo.a9kq.features.billing.add.AddBillScreen
import dev.evertonprdo.a9kq.features.billing.list.BillListScreen
import dev.evertonprdo.a9kq.features.meterreading.add.AddMeterReadingScreen
import dev.evertonprdo.a9kq.features.meterreading.list.MeterReadingListScreen
import dev.evertonprdo.a9kq.libs.snackbar.ProvideSnackbarDispatcher
import dev.evertonprdo.a9kq.libs.snackbar.rememberAppSnackbarDispatcher
import dev.evertonprdo.a9kq.libs.utils.unfocusOnTap

@Composable
fun App() {

    val backStack = rememberSaveable { mutableStateListOf<Any>("/records") }
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarDispatcher = rememberAppSnackbarDispatcher(snackbarHostState)

    val shouldHideBottomBar = backStack.lastOrNull().toString().endsWith("/add")

    ProvideSnackbarDispatcher(snackbarDispatcher) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                AnimatedVisibility(!shouldHideBottomBar) {
                    BottomAppBar(actions = {
                        IconButton(
                            onClick = { backStack.clear(); backStack.add("/records") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.timer),
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { backStack.clear(); backStack.add("/bills") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.receipt_long),
                                contentDescription = null,
                            )
                        }
                    })
                }
            }
        ) { contentPadding ->

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .unfocusOnTap(focusManager)
                    .padding(contentPadding)

            ) { key ->
                when (key) {
                    "/records" -> NavEntry(key) { MeterReadingListScreen({ backStack.add("/record/add") }) }
                    "/record/add" -> NavEntry(key) { AddMeterReadingScreen({ backStack.removeLastOrNull() }) }
                    "/bills" -> NavEntry(key) { BillListScreen { backStack.add("/bill/add") } }
                    "/bill/add" -> NavEntry(key) { AddBillScreen { backStack.removeLastOrNull() } }

                    else -> NavEntry(Unit) { Text("404") }
                }
            }
        }
    }
}