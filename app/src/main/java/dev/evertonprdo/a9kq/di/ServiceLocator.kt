package dev.evertonprdo.a9kq.di

import android.content.Context
import androidx.datastore.core.DataStore
import dev.evertonprdo.a9kq.data.BillRepository
import dev.evertonprdo.a9kq.data.EnergyDataSource
import dev.evertonprdo.a9kq.data.EnergyRepository
import dev.evertonprdo.a9kq.data.energyDataSource

object ServiceLocator {

    private lateinit var energyDataSource: DataStore<EnergyDataSource>

    val energyRepository: EnergyRepository by lazy { EnergyRepository(energyDataSource) }
    val billRepository: BillRepository by lazy { BillRepository(energyDataSource) }

    fun initialize(context: Context) {
        energyDataSource = context.energyDataSource
    }
}