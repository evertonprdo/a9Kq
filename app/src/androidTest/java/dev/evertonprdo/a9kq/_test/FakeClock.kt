package dev.evertonprdo.a9kq._test

import kotlin.time.Clock
import kotlin.time.Instant

class FakeClock(private var instant: Instant) : Clock {

    override fun now(): Instant = instant

    fun advanceBy(seconds: Long) {
        val timestamp = instant.epochSeconds + seconds
        instant = Instant.fromEpochSeconds(timestamp)
    }

    companion object {
        val Zero = FakeClock(Instant.fromEpochSeconds(0))
    }
}