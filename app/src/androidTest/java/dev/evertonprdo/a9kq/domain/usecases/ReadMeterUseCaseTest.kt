package dev.evertonprdo.a9kq.domain.usecases

import dev.evertonprdo.a9kq._test.FakeClock
import dev.evertonprdo.a9kq._test.TestWithDatabase
import dev.evertonprdo.a9kq.libs.KWh
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReadMeterUseCaseTest : TestWithDatabase() {

    lateinit var readMeterUseCase: ReadMeterUseCase
    val clock: FakeClock = FakeClock.Zero

    @Before
    fun setup() {
        readMeterUseCase = ReadMeterUseCase(
            meterReadingRepository = meterReadingRepository,
            clock = clock
        )
    }

    @Test
    fun givenPositiveMeterIndex_whenRead_thenPersistWithNowTimestamp() = runTest {
        val meterIndex = KWh(100)
        val now = clock.now().epochSeconds

        readMeterUseCase(meterIndex)

        val persisted = db.meterReadingTestDao().getBy(now)
        Assert.assertNotNull(persisted); persisted!!

        Assert.assertEquals(persisted.meterIndex, meterIndex.toLong())
        Assert.assertEquals(persisted.readAt, now)
    }

    @Test(expected = IllegalArgumentException::class)
    fun givenLowerMeterIndexThanLastRead_whenRead_thenThrowException() = runTest {
        readMeterUseCase(KWh(50))

        clock.advanceBy(50)
        readMeterUseCase(KWh(20))
    }

    @Test(expected = IllegalArgumentException::class)
    fun givenNegativeMeterIndex_whenRead_thenThrowException() = runTest {
        readMeterUseCase(KWh(-50))
    }
}