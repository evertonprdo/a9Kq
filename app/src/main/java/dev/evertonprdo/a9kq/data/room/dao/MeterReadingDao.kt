package dev.evertonprdo.a9kq.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.evertonprdo.a9kq.data.room.schema.MeterReading
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterReadingDao {

    @Query("SELECT * FROM meter_reading")
    fun getAll(): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_reading ORDER BY read_at DESC LIMIT 1")
    suspend fun getMostRecent(): MeterReading?

    @Insert
    suspend fun insert(item: MeterReading)
}