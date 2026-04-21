package dev.evertonprdo.a9kq._test

import androidx.room.Dao
import androidx.room.Query
import dev.evertonprdo.a9kq.data.room.schema.MeterReading

@Dao
interface MeterReadingTestDao {

    @Query("SELECT * FROM meter_reading WHERE read_at = :key")
    suspend fun getBy(key: Long): MeterReading?
}