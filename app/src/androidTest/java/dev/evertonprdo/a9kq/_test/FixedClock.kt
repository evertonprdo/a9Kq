package dev.evertonprdo.a9kq._test

import kotlin.time.Clock
import kotlin.time.Instant

class FixedClock(private val instant: Instant) : Clock {

    override fun now(): Instant = instant

    companion object {
        val Zero = FixedClock(Instant.fromEpochSeconds(0))
    }
}