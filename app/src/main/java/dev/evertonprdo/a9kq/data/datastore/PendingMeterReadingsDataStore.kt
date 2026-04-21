package dev.evertonprdo.a9kq.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PendingMeterReadingsDataStore(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun updatePendingMeterReadings(keys: List<Long>) {
        dataStore.edit { storage ->
            storage[APP_CACHED_REMOVED_METER_READINGS] = Json.encodeToString(keys)
        }
    }

    suspend fun updatePendingMeterReadings(key: Long) {
        val curr = loadPendingMeterReadings().firstOrNull() ?: emptyList()
        dataStore.edit { storage ->
            storage[APP_CACHED_REMOVED_METER_READINGS] = Json.encodeToString(curr + key)
        }
    }

    suspend fun clearPendingMeterReadings() = updatePendingMeterReadings(emptyList())

    fun loadPendingMeterReadings(): Flow<List<Long>> =
        dataStore.data.map { storage ->
            storage[APP_CACHED_REMOVED_METER_READINGS]
                ?.let { Json.decodeFromString<List<Long>>(it) }
                ?: emptyList()
        }

    private companion object {

        val APP_CACHED_REMOVED_METER_READINGS = stringPreferencesKey("app_pending_meter_readings")
    }
}