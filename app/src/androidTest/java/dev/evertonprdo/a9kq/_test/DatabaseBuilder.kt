package dev.evertonprdo.a9kq._test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.evertonprdo.a9kq.data.room.AppDatabase

object DatabaseBuilder {

    fun build(): AppDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
    }
}