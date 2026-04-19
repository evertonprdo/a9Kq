package dev.evertonprdo.a9kq.domain.repositories

import dev.evertonprdo.a9kq.domain.entities.MeterReading
import kotlinx.coroutines.flow.Flow

interface MeterReadingRepository {

    fun getAll(): Flow<List<MeterReading>>

    suspend fun getMostRecent(): MeterReading?

    suspend fun add(read: MeterReading)
    suspend fun remove(read: MeterReading)

    suspend fun exists(read: MeterReading): Boolean
}