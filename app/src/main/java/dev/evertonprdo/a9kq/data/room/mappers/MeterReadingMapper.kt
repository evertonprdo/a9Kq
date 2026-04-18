package dev.evertonprdo.a9kq.data.room.mappers

import dev.evertonprdo.a9kq.libs.KWh
import kotlin.time.Instant
import dev.evertonprdo.a9kq.data.room.schema.MeterReading as RoomMeterReading
import dev.evertonprdo.a9kq.domain.entities.MeterReading as DomainMeterReading

class MeterReadingMapper {

    fun fromDomain(domainMeterReading: DomainMeterReading): RoomMeterReading =
        RoomMeterReading(
            meterIndex = domainMeterReading.meterIndex.value.toLong(),
            readAt = domainMeterReading.readAt.epochSeconds
        )

    fun toDomain(roomMeterReading: RoomMeterReading): DomainMeterReading =
        DomainMeterReading(
            meterIndex = KWh(roomMeterReading.meterIndex.toInt()),
            readAt = Instant.fromEpochSeconds(roomMeterReading.readAt)
        )

    fun toDomain(roomMeterReadingList: List<RoomMeterReading>): List<DomainMeterReading> =
        roomMeterReadingList.map { toDomain(it) }
}