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

    override fun getHistory(): Flow<MeterReadingHistory> =
        meterReadingDao.getAllByReadAtDesc().map { meterReadingMapper.toDomain(it) }

    override suspend fun getMostRecent(): MeterReading? =
        meterReadingDao.getMostRecent()?.let { meterReadingMapper.toDomain(it) }

    override suspend fun add(read: MeterReading) =
        meterReadingDao.insert(meterReadingMapper.fromDomain(read))

    override suspend fun remove(reads: List<Long>) {
        meterReadingDao.softDelete(reads)
    }
}
