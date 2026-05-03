package dev.evertonprdo.a9kq.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.evertonprdo.a9kq.data.room.schema.MeterReading
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterReadingDao {

    @Query("SELECT * FROM meter_reading WHERE marked_to_remove_at IS NULL ORDER BY read_at DESC")
    fun getAllByReadAtDesc(): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_reading WHERE marked_to_remove_at IS NULL ORDER BY read_at DESC LIMIT 1")
    suspend fun getMostRecent(): MeterReading?

    @Insert
    suspend fun insert(item: MeterReading)

    @Query(
        """
            UPDATE meter_reading 
            SET marked_to_remove_at = strftime('%s', 'now')
            WHERE read_at IN (:keys)
        """
    )
    suspend fun markAsRemoved(vararg keys: Long)

    @Query(
        """
            UPDATE meter_reading
            SET marked_to_remove_at = NULL
            WHERE read_at IN (:keys)
        """
    )
    suspend fun unmarkToRemove(vararg keys: Long)

    @Query("DELETE FROM meter_reading WHERE marked_to_remove_at IS NOT NULL")
    suspend fun deleteAllMarkedToRemove()

    @Query(
        """
            DELETE FROM meter_reading
            WHERE marked_to_remove_at IS NOT NULL
            AND strftime('%s', 'now') - marked_to_remove_at > :grace
        """
    )
    suspend fun deleteAllExpiredMarkedToRemove(grace: Long = DEFAULT_GRACE)

    @Query(
        """
            SELECT COUNT(*) FROM meter_reading
            WHERE read_at IN (:keys)
            AND marked_to_remove_at IS NOT NULL
            AND strftime('%s', 'now') - marked_to_remove_at <= :grace
        """
    )
    suspend fun countEligibleToUnmarkToRemove(vararg keys: Long, grace: Long = DEFAULT_GRACE): Int

    suspend fun isAllEligibleToUnmarkToRemove(
        vararg keys: Long,
        grace: Long = DEFAULT_GRACE
    ): Boolean {
        if (keys.isEmpty()) return true
        val isAll = countEligibleToUnmarkToRemove(*keys, grace) == keys.size
        println("It's all eligible? $isAll")
        return countEligibleToUnmarkToRemove(*keys, grace) == keys.size
    }

    private companion object {
        const val DEFAULT_GRACE = 30L
    }
}