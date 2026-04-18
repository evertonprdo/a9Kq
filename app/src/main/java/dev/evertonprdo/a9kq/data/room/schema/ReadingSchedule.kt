package dev.evertonprdo.a9kq.data.room.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_schedule")
data class ReadingSchedule(
    @PrimaryKey @ColumnInfo(name = "expected_reading_at") val expectedReadingAt: String
)
