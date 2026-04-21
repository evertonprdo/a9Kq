package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.DatabaseBuilder
import dev.evertonprdo.a9kq.data.room.AppDatabase
import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.data.room.repository.MeterReadingRepositoryImpl
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock

class ReadMeterUseCaseTest {

    lateinit var db: AppDatabase
    lateinit var readMeterUseCase: ReadMeterUseCase

    @Before
    fun setup() {
        db = DatabaseBuilder.build()
        val meterReadingRepository = MeterReadingRepositoryImpl(
            meterReadingDao = db.meterReadingDao(),
            meterReadingMapper = MeterReadingMapper()
        )

        readMeterUseCase = ReadMeterUseCase(
            meterReadingRepository = meterReadingRepository
        )
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun readMeter() = runTest {
        val meterIndex = 100
        val now = Clock.System.now()

        val reading = MeterReading(
            readAt = now,
            meterIndex = KWh(meterIndex)
        )

        readMeterUseCase(reading)

        val persisted =
            db.query("SELECT * FROM meter_reading WHERE read_at = ?", arrayOf(now.epochSeconds))

        Assert.assertTrue(persisted.count == 1)
        persisted.use {
            val idx = it.getColumnIndex("meter_index")

            it.moveToNext()
            Assert.assertEquals(it.getLong(idx), meterIndex.toLong())
        }
    }
}