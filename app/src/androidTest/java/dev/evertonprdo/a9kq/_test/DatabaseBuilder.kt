package dev.evertonprdo.a9kq._test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

object DatabaseBuilder {

    fun build(): TestAppDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, TestAppDatabase::class.java)
            .build()
    }
}