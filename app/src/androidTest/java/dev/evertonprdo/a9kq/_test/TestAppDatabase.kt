package dev.evertonprdo.a9kq._test

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.evertonprdo.a9kq.data.room.dao.MeterReadingDao
import dev.evertonprdo.a9kq.data.room.schema.BillingPeriod
import dev.evertonprdo.a9kq.data.room.schema.MeterReading
import dev.evertonprdo.a9kq.data.room.schema.ReadingSchedule

@Database(
    entities = [
        MeterReading::class,
        BillingPeriod::class,
        ReadingSchedule::class
    ],
    version = 1
)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun meterReadingDao(): MeterReadingDao
    abstract fun meterReadingTestDao(): MeterReadingTestDao
}