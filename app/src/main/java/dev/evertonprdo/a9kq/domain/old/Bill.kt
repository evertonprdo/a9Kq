package dev.evertonprdo.a9kq.domain.old

import dev.evertonprdo.a9kq.libs.BRLMoney
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Bill(
    val currRead: LocalDate,
    val nextRead: LocalDate,
    val amount: KWh,
    val total: BRLMoney
)