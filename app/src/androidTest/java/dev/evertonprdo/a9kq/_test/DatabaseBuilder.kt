package dev.evertonprdo.a9kq._test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestDispatcher

object DatabaseBuilder {
    fun build(
        dispatcher: TestDispatcher,
        context: Context = ApplicationProvider.getApplicationContext()
    ): TestAppDatabase {

        return Room
            .inMemoryDatabaseBuilder(context, TestAppDatabase::class.java)
            .setTransactionExecutor(dispatcher.asExecutor())
            .setQueryExecutor(dispatcher.asExecutor())
            .build()
    }
}