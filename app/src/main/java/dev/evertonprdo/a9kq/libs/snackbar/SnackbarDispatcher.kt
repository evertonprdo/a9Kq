package dev.evertonprdo.a9kq.libs.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class SnackbarDispatcher(private val snackHost: SnackbarHostState) {

    suspend fun showMessage(message: String) =
        snackHost.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )

    suspend fun showMessage(message: String, label: String, onAction: () -> Unit) {
        val res = snackHost.showSnackbar(
            message = message,
            actionLabel = label,
            withDismissAction = true,
            duration = SnackbarDuration.Long
        )

        if (res == SnackbarResult.ActionPerformed)
            onAction()
    }

    @Composable
    fun SnackbarHost() =
        SnackbarHost(snackHost)
}

@Composable
fun rememberAppSnackbarDispatcher(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): SnackbarDispatcher = remember(snackbarHostState) { SnackbarDispatcher(snackbarHostState) }

