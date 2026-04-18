package dev.evertonprdo.a9kq.data.room.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meter_reading")
data class MeterReading(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "read_at") val readAt: Long,
    @ColumnInfo(name = "meter_index") val meterIndex: Long
)
