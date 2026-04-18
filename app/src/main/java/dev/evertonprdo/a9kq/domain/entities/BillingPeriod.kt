package dev.evertonprdo.a9kq.domain.entities

import dev.evertonprdo.a9kq.libs.BRLMoney
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.datetime.LocalDate

data class BillingPeriod(
    val id: Int,
    val startedAt: LocalDate,
    val endedAt: LocalDate,
    val startMeterIndex: KWh,
    val endMeterIndex: KWh,
    val totalCost: BRLMoney
)
