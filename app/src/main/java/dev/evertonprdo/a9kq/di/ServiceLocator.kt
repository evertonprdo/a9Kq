package dev.evertonprdo.a9kq.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import dev.evertonprdo.a9kq.data.old.BillRepository
import dev.evertonprdo.a9kq.data.old.EnergyDataSource
import dev.evertonprdo.a9kq.data.old.energyDataSource
import dev.evertonprdo.a9kq.data.room.AppDatabase
import dev.evertonprdo.a9kq.data.room.mappers.MeterReadingMapper
import dev.evertonprdo.a9kq.data.room.repository.MeterReadingRepositoryImpl
import dev.evertonprdo.a9kq.domain.repositories.MeterReadingRepository
import dev.evertonprdo.a9kq.libs.coroutines.AppCoroutineScope

object ServiceLocator {

    private lateinit var energyDataSource: DataStore<EnergyDataSource>
    private lateinit var database: AppDatabase

    lateinit var appCoroutineScope: AppCoroutineScope
        private set

    val meterReadingRepository: MeterReadingRepository by lazy {
        MeterReadingRepositoryImpl(
            meterReadingDao = database.meterReadingDao(),
            meterReadingMapper = MeterReadingMapper()
        )
    }
    val billRepository: BillRepository by lazy { BillRepository(energyDataSource) }

    fun initialize(context: Context) {
        energyDataSource = context.energyDataSource

        database = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).build()

        appCoroutineScope = AppCoroutineScope()
    }
}