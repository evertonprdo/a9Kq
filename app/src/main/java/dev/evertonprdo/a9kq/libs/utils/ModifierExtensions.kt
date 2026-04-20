package dev.evertonprdo.a9kq.libs.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.unfocusOnTap(fm: FocusManager) =
    this.pointerInput(Unit) { detectTapGestures(onTap = { fm.clearFocus() }) }