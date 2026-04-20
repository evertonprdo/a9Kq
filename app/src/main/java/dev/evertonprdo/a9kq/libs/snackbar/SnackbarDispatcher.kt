package dev.evertonprdo.a9kq.libs.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarDispatcher(
    private val snackHost: SnackbarHostState,
    private val scope: CoroutineScope
) {

    fun showMessage(message: String) = scope.launch {
        snackHost.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }

    fun showMessage(message: String, label: String, onAction: () -> Unit) = scope.launch {
        val res = snackHost.showSnackbar(
            message = message,
            actionLabel = label,
            withDismissAction = true,
            duration = SnackbarDuration.Long
        )

        if (res == SnackbarResult.ActionPerformed)
            onAction()
    }
}

@Composable
fun rememberAppSnackbarDispatcher(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope = rememberCoroutineScope()
): SnackbarDispatcher = remember(snackbarHostState) {
    SnackbarDispatcher(
        snackHost = snackbarHostState,
        scope = scope
    )
}