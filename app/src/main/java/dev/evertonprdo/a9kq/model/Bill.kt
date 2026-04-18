package dev.evertonprdo.a9kq.model

import dev.evertonprdo.a9kq.lib.KWh
import dev.evertonprdo.a9kq.lib.Money
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Bill(
    val currRead: LocalDate,
    val nextRead: LocalDate,
    val amount: KWh,
    val total: Money
)