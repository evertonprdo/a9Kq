package dev.evertonprdo.a9kq.data.old

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import dev.evertonprdo.a9kq.domain.old.Bill
import dev.evertonprdo.a9kq.domain.old.EnergyRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class EnergyDataSource(
    val records: List<EnergyRecord>,
    val bills: List<Bill>
) {
    companion object : Serializer<EnergyDataSource> {

        override val defaultValue: EnergyDataSource = EnergyDataSource(emptyList(), emptyList())

        override suspend fun readFrom(input: InputStream): EnergyDataSource {
            try {

                return Json.decodeFromString(
                    serializer(),
                    input.readBytes().decodeToString()
                )
            } catch (serialization: SerializationException) {
                throw CorruptionException("Unable to read Settings", serialization)
            }
        }

        override suspend fun writeTo(t: EnergyDataSource, output: OutputStream) {
            withContext(Dispatchers.IO) {
                val json = Json.encodeToString(serializer(), t)
                output.write(json.toByteArray())
            }
        }
    }
}

val Context.energyDataSource: DataStore<EnergyDataSource> by dataStore(
    fileName = "energy-data-source.json",
    serializer = EnergyDataSource
)