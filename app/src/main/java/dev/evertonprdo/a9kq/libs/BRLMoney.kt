package dev.evertonprdo.a9kq.libs

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class BRLMoney(val cents: Long) : Comparable<BRLMoney> {
    override fun toString(): String {
        val absCents = kotlin.math.abs(cents)
        val dollars = absCents / 100
        val remainingCents = absCents % 100
        val sign = if (cents < 0) "-" else ""
        return buildString {
            append(sign)
            append("R$ ")
            append(dollars)
            append(".")
            append(remainingCents.toString().padStart(2, '0'))
        }
    }

    override fun compareTo(other: BRLMoney): Int = cents.compareTo(other.cents)

    operator fun plus(other: BRLMoney) = BRLMoney(this.cents + other.cents)
    operator fun minus(other: BRLMoney) = BRLMoney(this.cents - other.cents)
    operator fun times(scalar: Int) = BRLMoney(this.cents * scalar)
    operator fun times(scalar: Double) = BRLMoney((this.cents * scalar).toLong())
    operator fun div(scalar: Int) = BRLMoney(this.cents / scalar)
    operator fun div(scalar: Double) = BRLMoney((this.cents / scalar).toLong())
    operator fun unaryMinus() = BRLMoney(-cents)

    companion object {
        val ZERO = BRLMoney(0)
        fun fromDouble(amount: Double) = BRLMoney((amount * 100).toLong())
    }
}
