package dev.evertonprdo.a9kq.features.meterreading.extensions

import dev.evertonprdo.a9kq.domain.entities.MeterReading

typealias MeterReadingKey = Long

val MeterReading.key: MeterReadingKey get() = this.readAt.epochSeconds
fun MeterReading.same(other: MeterReading): Boolean = this.key == other.key
