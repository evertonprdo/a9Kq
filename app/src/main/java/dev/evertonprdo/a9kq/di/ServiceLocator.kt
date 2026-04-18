package dev.evertonprdo.a9kq.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import dev.evertonprdo.a9kq.data.old.BillRepository
import dev.evertonprdo.a9kq.data.old.EnergyDataSource
import dev.evertonprdo.a9kq.data.old.EnergyRepository
import dev.evertonprdo.a9kq.data.old.energyDataSource
import dev.evertonprdo.a9kq.data.room.AppDatabase

object ServiceLocator {

    private lateinit var energyDataSource: DataStore<EnergyDataSource>
    private lateinit var database: AppDatabase

    val energyRepository: EnergyRepository by lazy { EnergyRepository(energyDataSource) }
    val billRepository: BillRepository by lazy { BillRepository(energyDataSource) }

    fun initialize(context: Context) {
        energyDataSource = context.energyDataSource
        database = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).build()
    }
}