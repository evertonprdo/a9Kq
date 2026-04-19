package dev.evertonprdo.a9kq.libs.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextUnit.toDp(scale: Float = 1f): Dp {
    val density = LocalDensity.current
    val value = this * scale

    return with(density) {
        value.toDp()
    }
}