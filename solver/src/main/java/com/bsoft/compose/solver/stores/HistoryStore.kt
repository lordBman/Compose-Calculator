package com.bsoft.compose.solver.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.core.Serializer
import com.bsoft.compose.solver.SolverUtils
import com.bsoft.compose.solver.models.Datum
import com.bsoft.compose.solver.models.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class HistoryData(val histories: List<History> = emptyList()){
}

object HistoryDataSerializer: Serializer<HistoryData> {
    override val defaultValue: HistoryData
        get() = HistoryData()

    override suspend fun readFrom(input: InputStream): HistoryData {
        try {
            return Json.decodeFromString<HistoryData>(input.readBytes().decodeToString())
        }catch (exception: SerializationException){
            throw Exception("Unable to read Settings", exception)
        }
    }

    override suspend fun writeTo(t: HistoryData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).toByteArray())
        }
    }
}

val Context.historyDataStore: DataStore<HistoryData> by dataStore( fileName = "history.json", serializer = HistoryDataSerializer)

class HistoryStore(val context: Context){
    val history: Flow<List<History>>
        get() = context.historyDataStore.data.map { it.histories }

    suspend fun save(data: List<Datum>) = context.historyDataStore.updateData {
        val mutableHistories = it.histories.toMutableList()
        if(mutableHistories.isNotEmpty() && SolverUtils.isToday(it.histories.last())){
            val last = it.histories.last()
            val mutableData:  MutableList<Datum> = mutableListOf()
            mutableData.addAll(last.data.filter { datum ->  !data.contains(datum) })
            mutableData.addAll(data)

            val init = last.copy(data = mutableData)
            mutableHistories.removeAt(mutableHistories.lastIndex)
            mutableHistories.add(init)
        }else{
            val init = History(data)
            mutableHistories.add(init)
        }
        it.copy(histories = mutableHistories)
    }

    suspend fun clear() = context.historyDataStore.updateData {
        it.copy(histories = emptyList())
    }
}