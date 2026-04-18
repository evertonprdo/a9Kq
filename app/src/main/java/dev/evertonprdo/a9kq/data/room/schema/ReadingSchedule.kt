package dev.evertonprdo.a9kq.data.room.schema

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "reading_schedule")
data class ReadingSchedule(
    @ColumnInfo(name = "expected_reading_at") val expectedReadingAt: String
)
