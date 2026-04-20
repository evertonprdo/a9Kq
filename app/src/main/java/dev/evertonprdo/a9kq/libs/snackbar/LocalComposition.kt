package dev.evertonprdo.a9kq.libs.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarDispatcher =
    staticCompositionLocalOf<SnackbarDispatcher> { error("No SnackbarDispatcher provided") }

@Composable
fun ProvideSnackbarDispatcher(
    dispatcher: SnackbarDispatcher,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSnackbarDispatcher provides dispatcher,
        content = content
    )
}