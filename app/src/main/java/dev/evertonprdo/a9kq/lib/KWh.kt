package dev.evertonprdo.a9kq.lib

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class KWh(val value: Int) : Comparable<KWh> {

    override fun toString(): String = "$value kWh"

    override fun compareTo(other: KWh): Int = value.compareTo(other.value)

    operator fun plus(other: KWh) = KWh(this.value + other.value)
    operator fun minus(other: KWh) = KWh(this.value - other.value)
    operator fun times(scalar: Int) = KWh(this.value * scalar)
    operator fun times(scalar: Double) = KWh((this.value * scalar).toInt())
    operator fun div(scalar: Int) = KWh(this.value / scalar)
    operator fun div(scalar: Double) = KWh((this.value / scalar).toInt())
    operator fun times(money: Money) = Money(this.value * money.cents)
    operator fun unaryMinus() = KWh(-value)
}
