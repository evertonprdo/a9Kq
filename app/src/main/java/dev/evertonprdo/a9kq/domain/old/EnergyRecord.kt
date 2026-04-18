package dev.evertonprdo.a9kq.domain.old

import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EnergyRecord(
    val instant: Instant,
    val amount: KWh,
)
