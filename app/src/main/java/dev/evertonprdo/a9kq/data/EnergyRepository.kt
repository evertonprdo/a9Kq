package dev.evertonprdo.a9kq.data

import androidx.datastore.core.DataStore
import dev.evertonprdo.a9kq.model.EnergyRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EnergyRepository(private val source: DataStore<EnergyDataSource>) {

    val records: Flow<List<EnergyRecord>> =
        source.data.map { data -> data.records.sortedByDescending { it.instant.epochSeconds } }

    suspend fun addRecord(item: EnergyRecord) {

        withContext(Dispatchers.IO) {
            source.updateData { data ->
                data.copy(records = data.records + item)
            }
        }
    }
}