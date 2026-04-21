package dev.evertonprdo.a9kq.domain.repositories

import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.entities.vo.MeterReadingHistory
import kotlinx.coroutines.flow.Flow

interface MeterReadingRepository {
    fun getHistory(): Flow<MeterReadingHistory>

    suspend fun getMostRecent(): MeterReading?

    suspend fun add(read: MeterReading)
    suspend fun remove(keys: List<Long>)
    suspend fun remove(key: Long)

    suspend fun undoPendingRemoval()
}