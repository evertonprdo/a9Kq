package dev.evertonprdo.a9kq.data.room.repository

import dev.evertonprdo.a9kq.data.room.dao.MeterReadingDao
import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.entities.vo.MeterReadingHistory
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeterReadingRepositoryImpl(
    private val meterReadingDao: MeterReadingDao,
    private val meterReadingMapper: MeterReadingMapper
) : MeterReadingRepository {

    override fun getAll(): Flow<List<MeterReading>> =
        meterReadingDao.getAll().map { meterReadingMapper.toDomain(it) }

    override fun getHistory(): Flow<MeterReadingHistory> =
        meterReadingDao.getAllByReadAtDesc().map { meterReadingMapper.toDomain(it) }

    override suspend fun getMostRecent(): MeterReading? =
        meterReadingDao.getMostRecent()?.let { meterReadingMapper.toDomain(it) }

    override suspend fun add(read: MeterReading) =
        meterReadingDao.insert(meterReadingMapper.fromDomain(read))

    override suspend fun remove(read: MeterReading) =
        meterReadingDao.delete(meterReadingMapper.fromDomain(read))

    override suspend fun remove(reads: List<Long>) =
        meterReadingDao.delete(reads)

    override suspend fun exists(read: MeterReading): Boolean =
        meterReadingDao.exists(meterReadingMapper.fromDomain(read).readAt)

    override suspend fun exists(readAt: Long): Boolean =
        meterReadingDao.exists(readAt)

}
