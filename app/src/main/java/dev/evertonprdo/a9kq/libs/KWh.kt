package dev.evertonprdo.a9kq.libs

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class KWh(val value: Int) : Comparable<KWh> {

    override fun toString(): String = "$value kWh"

    override fun compareTo(other: KWh): Int = value.compareTo(other.value)
    operator fun compareTo(other: Int): Int = value.compareTo(other)

    operator fun plus(other: KWh) = KWh(this.value + other.value)
    operator fun minus(other: KWh) = KWh(this.value - other.value)
    operator fun times(scalar: Int) = KWh(this.value * scalar)
    operator fun times(scalar: Double) = KWh((this.value * scalar).toInt())
    operator fun div(scalar: Int) = KWh(this.value / scalar)
    operator fun div(scalar: Double) = KWh((this.value / scalar).toInt())
    operator fun times(BRLMoney: BRLMoney) = BRLMoney(this.value * BRLMoney.cents)
    operator fun unaryMinus() = KWh(-value)
}
