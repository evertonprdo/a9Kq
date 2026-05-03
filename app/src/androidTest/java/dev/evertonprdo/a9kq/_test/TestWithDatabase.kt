package dev.evertonprdo.a9kq._test

import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.data.room.repository.MeterReadingRepositoryImpl
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import dev.evertonprdo.a9kq.libs.KWh
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.time.Instant

abstract class TestWithDatabase {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    protected lateinit var db: TestAppDatabase
    protected lateinit var meterReadingRepository: MeterReadingRepository

    @Before
    fun globalSetup() {
        db = DatabaseBuilder.build(mainDispatcherRule.dispatcher)
        meterReadingRepository = MeterReadingRepositoryImpl(
            meterReadingDao = db.meterReadingDao(),
            meterReadingMapper = MeterReadingMapper()
        )
    }

    @After
    fun globalTeardown() {
        db.close()
    }

    suspend fun addMeterReadingEntry(at: Long, v: Int) {
        meterReadingRepository.add(MeterReading(Instant.fromEpochSeconds(at), KWh(v)))
    }
}