package dev.evertonprdo.a9kq.model

import dev.evertonprdo.a9kq.lib.KWh
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EnergyRecord(
    val instant: Instant,
    val amount: KWh,
)
