package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.DatabaseBuilder
import dev.evertonprdo.a9kq._test.TestAppDatabase
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

    lateinit var db: TestAppDatabase
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
    fun givenValidReading_whenRead_thenPersistWithCorrectValues() = runTest {
        val meterIndex = 100
        val now = Clock.System.now()

        val reading = MeterReading(
            readAt = now,
            meterIndex = KWh(meterIndex)
        )

        readMeterUseCase(reading)
        val persisted = db.meterReadingTestDao().getBy(now.epochSeconds)

        Assert.assertNotNull(persisted)
        Assert.assertEquals(persisted?.meterIndex, meterIndex.toLong())
    }
}