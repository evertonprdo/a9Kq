package dev.evertonprdo.a9kq.domain.entities

import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.datetime.LocalDate

data class MeterReading(
    val id: Int,
    val readAt: LocalDate,
    val meterIndex: KWh
)
