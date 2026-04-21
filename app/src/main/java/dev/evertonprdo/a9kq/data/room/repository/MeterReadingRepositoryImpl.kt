package dev.evertonprdo.a9kq.data.room.repository

import dev.evertonprdo.a9kq.data.room.dao.MeterReadingDao
import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.entities.vo.MeterReadingHistory
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class MeterReadingRepositoryImpl(
    private val meterReadingDao: MeterReadingDao,
    private val meterReadingMapper: MeterReadingMapper
) : MeterReadingRepository {
    init {
        // A very simple way to avoid using WorkManager
        runBlocking {
            meterReadingDao.deleteAllExpiredMarkedToRemove()
        }
        // This is something I could keep if I wanted, except for the blocking call,
        // which should instead be done with coroutineScope.launch
        // TODO: When WorkManager is implemented, delegate this to it
    }

    override fun getHistory(): Flow<MeterReadingHistory> =
        meterReadingDao.getAllByReadAtDesc().map { meterReadingMapper.toDomain(it) }

    override suspend fun getMostRecent(): MeterReading? =
        meterReadingDao.getMostRecent()?.let { meterReadingMapper.toDomain(it) }

    override suspend fun add(read: MeterReading) {
        meterReadingDao.deleteAllMarkedToRemove()
        meterReadingDao.insert(meterReadingMapper.fromDomain(read))
    }

    override suspend fun remove(keys: List<Long>) =
        meterReadingDao.softDelete(keys)

    override suspend fun undoPendingRemoval() =
        meterReadingDao.unmarkToRemoveAll()
}
