package dev.evertonprdo.a9kq

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.evertonprdo.a9kq.data.room.AppDatabase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() = runTest {
        val appContext = ApplicationProvider.getApplicationContext<Context>()

        val db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        Assert.assertFalse(db.meterReadingDao().isAllEligibleToUnmarkToRemove(1, 2))
    }
}