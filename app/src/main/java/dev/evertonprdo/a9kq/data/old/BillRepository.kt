package dev.evertonprdo.a9kq.data.old

import androidx.datastore.core.DataStore
import dev.evertonprdo.a9kq.domain.old.Bill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BillRepository(private val source: DataStore<EnergyDataSource>) {

    val bills: Flow<List<Bill>> =
        source.data.map { data -> data.bills.sortedByDescending { it.currRead } }

    suspend fun addRecord(item: Bill) {

        withContext(Dispatchers.IO) {
            source.updateData { data ->
                data.copy(bills = data.bills + item)
            }
        }
    }
}