package dev.evertonprdo.a9kq.libs.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


class Signaler {
    private val channel: Channel<Unit> = Channel(Channel.BUFFERED)

    suspend fun signal() = channel.send(Unit)
    fun signals() = channel.receiveAsFlow()
}

suspend inline fun Signaler.onSignal(crossinline callback: () -> Unit) =
    this.signals().collect { callback() }
