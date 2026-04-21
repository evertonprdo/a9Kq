package dev.evertonprdo.a9kq.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.evertonprdo.a9kq.data.room.schema.MeterReading
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterReadingDao {

    @Query("SELECT * FROM meter_reading")
    fun getAll(): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_reading WHERE marked_to_remove_at IS NOT NULL ORDER BY read_at DESC")
    fun getAllByReadAtDesc(): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_reading ORDER BY read_at DESC LIMIT 1")
    suspend fun getMostRecent(): MeterReading?

    @Insert
    suspend fun insert(item: MeterReading)

    @Delete
    suspend fun delete(item: MeterReading)

    @Query("DELETE FROM meter_reading WHERE read_at IN (:keys)")
    suspend fun delete(keys: List<Long>)

    @Query(
        """
            UPDATE meter_reading 
            SET marked_to_remove_at = strftime('%s', 'now')
            WHERE read_at IN (:keys)
        """
    )
    suspend fun softDelete(keys: List<Long>)

    @Query("SELECT EXISTS(SELECT 1 FROM meter_reading WHERE read_at = :readAt)")
    suspend fun exists(readAt: Long): Boolean
}