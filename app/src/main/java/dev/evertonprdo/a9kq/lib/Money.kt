package dev.evertonprdo.a9kq.lib

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Money(val cents: Long) : Comparable<Money> {
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

    override fun compareTo(other: Money): Int = cents.compareTo(other.cents)

    operator fun plus(other: Money) = Money(this.cents + other.cents)
    operator fun minus(other: Money) = Money(this.cents - other.cents)
    operator fun times(scalar: Int) = Money(this.cents * scalar)
    operator fun times(scalar: Double) = Money((this.cents * scalar).toLong())
    operator fun div(scalar: Int) = Money(this.cents / scalar)
    operator fun div(scalar: Double) = Money((this.cents / scalar).toLong())
    operator fun unaryMinus() = Money(-cents)

    companion object {
        val ZERO = Money(0)
        fun fromDouble(amount: Double) = Money((amount * 100).toLong())
    }
}
