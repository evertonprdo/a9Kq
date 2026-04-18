package dev.evertonprdo.a9kq.domain.entities

import dev.evertonprdo.a9kq.libs.KWh
import kotlin.time.Instant

data class MeterReading(
    val readAt: Instant,
    val meterIndex: KWh
)
