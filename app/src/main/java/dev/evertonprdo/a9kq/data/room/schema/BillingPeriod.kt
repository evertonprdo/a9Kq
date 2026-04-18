package dev.evertonprdo.a9kq.data.room.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "billing_period")
data class BillingPeriod(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "started_at") val startedAt: String,
    @ColumnInfo(name = "ended_at") val endedAt: String,
    @ColumnInfo(name = "start_meter_index") val startMeterIndex: Long,
    @ColumnInfo(name = "end_meter_index") val endMeterIndex: Long,
    @ColumnInfo(name = "total_cost") val totalCost: Long
)