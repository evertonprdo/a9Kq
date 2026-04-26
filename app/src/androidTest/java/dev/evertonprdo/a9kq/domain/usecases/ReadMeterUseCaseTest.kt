package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.DatabaseBuilder
import dev.evertonprdo.a9kq._test.FixedClock
import dev.evertonprdo.a9kq._test.TestAppDatabase
import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.data.room.repository.MeterReadingRepositoryImpl
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReadMeterUseCaseTest {

    lateinit var db: TestAppDatabase
    lateinit var readMeterUseCase: ReadMeterUseCase

    val clock: FixedClock = FixedClock.Zero

    @Before
    fun setup() {
        db = DatabaseBuilder.build()
        val meterReadingRepository = MeterReadingRepositoryImpl(
            meterReadingDao = db.meterReadingDao(),
            meterReadingMapper = MeterReadingMapper()
        )

        readMeterUseCase = ReadMeterUseCase(
            meterReadingRepository = meterReadingRepository,
            clock = clock
        )
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun givenPositiveMeterIndex_whenRead_thenPersistWithNowTimestamp() = runTest {
        val meterIndex = 100

        readMeterUseCase(KWh(meterIndex))
        val persisted = db.meterReadingTestDao().getBy(0)

        Assert.assertNotNull(persisted); persisted!!
        Assert.assertEquals(persisted.meterIndex, meterIndex.toLong())
        Assert.assertEquals(persisted.readAt, clock.now().epochSeconds)
    }
}