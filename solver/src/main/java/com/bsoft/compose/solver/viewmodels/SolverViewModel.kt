package com.bsoft.compose.solver.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import com.bsoft.compose.solver.SolverUtils
import com.bsoft.compose.solver.components.Parser
import com.bsoft.compose.solver.models.DRGMode
import com.bsoft.compose.solver.models.Datum
import com.bsoft.compose.solver.models.History
import com.bsoft.compose.solver.models.HypMode
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.KeyMode
import com.bsoft.compose.solver.models.ModesData
import com.bsoft.compose.solver.models.SolverData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

class SolverViewModel(private val app: Application): AndroidViewModel(app) {
    private val mutableState = MutableStateFlow(SolverData())
    val state: StateFlow<SolverData> = mutableState.asStateFlow()

    private val mutableModes = MutableStateFlow(ModesData())
    val modes: StateFlow<ModesData> = mutableModes.asStateFlow()

    val histories: Flow<List<History>>
        get() = this.app.historyDataStore.data.map { it.histories }

    fun solve(){
        try {
            var datum = this.mutableState.value.current
            if(datum.inputs.isNotEmpty()){
                val parser = Parser(datum.inputs)
                val init = parser.parse()
                val result = init.solve(this.mutableModes.value.drg).toString()
                datum = datum.copy(solution = result )
                this.mutableState.update { it.copy( current = datum) }
            }
        }catch (error: Exception){
            mutableState.update { it.copy(error = error.message.let { "Syntax Error" }) }
        }
        mutableModes.update { it.copy(key = KeyMode.Normal, hyp = HypMode.Off) }
    }

    fun clear(){
        val datum = this.mutableState.value.current
        if(datum.solution == null){
            this.mutableState.update { it.copy(current = Datum(), error = null) }
        }else{
            CoroutineScope(Dispatchers.Default).launch{
                val histories = app.historyDataStore.data.first().histories.toMutableList()
                if(histories.isNotEmpty() && SolverUtils.isToday(histories.last())){
                    var history = histories.removeAt(histories.size - 1)
                    val data = history.data.toMutableList()
                    data.add(datum)

                    history = history.copy(data = data)
                    histories.add(history)
                }else{
                    histories.add(History(listOf(datum)))
                }
                app.historyDataStore.updateData {
                    it.copy(histories = histories)
                }
            }
            this.mutableState.update { it.copy(current = Datum(), error = null) }
        }
        mutableModes.update { it.copy(key = KeyMode.Normal, hyp = HypMode.Off) }
    }

    fun delete(){
        var datum = this.mutableState.value.current
        val inputs = datum.inputs.toMutableList()
        if (inputs.isNotEmpty()){
            inputs.removeAt(inputs.lastIndex)

            datum = datum.copy(inputs = inputs)
            this.mutableState.update { it.copy(current = datum) }
        }
    }

    fun clicked(value: String, type: InputType){
        if(type == InputType.Number || type == InputType.Power || (type == InputType.Base && SolverUtils.isBaseCompatible(value))){
            var datum = this.mutableState.value.current

            val inputs = datum.inputs.toMutableList()
            inputs.add(Input(value = value, type = type))
            datum = datum.copy(inputs = inputs, solution = null)
            this.mutableState.update { it.copy( current = datum) }
        }
    }

    fun toggleDRG(){
        when(this.mutableModes.value.drg){
            DRGMode.Deg -> this.mutableModes.update { it.copy(drg = DRGMode.Rad) }
            DRGMode.Rad -> this.mutableModes.update { it.copy(drg = DRGMode.Grad) }
            DRGMode.Grad -> this.mutableModes.update { it.copy(drg = DRGMode.Deg) }
        }
    }

    fun toggleKeyMode(){
        val datum = this.mutableState.value.current
        when(this.mutableModes.value.key){
            KeyMode.Power -> this.mutableModes.update { it.copy(key = KeyMode.Normal) }
            KeyMode.Base -> this.mutableModes.update { it.copy(key = KeyMode.Power) }
            KeyMode.Normal -> {
                if(datum.inputs.isNotEmpty() && datum.inputs.last().value == "log"){
                    this.mutableModes.update { it.copy(key = KeyMode.Base) }
                }else{
                    this.mutableModes.update { it.copy(key = KeyMode.Power) }
                }
            }
        }
    }

    fun toggleHyp(){
        when(this.mutableModes.value.hyp){
            HypMode.Off -> this.mutableModes.update { it.copy(hyp = HypMode.On) }
            HypMode.On -> this.mutableModes.update { it.copy(hyp = HypMode.Off) }
        }
    }

    fun chosen(index: Int){
        val current = this.mutableState.value.current
        CoroutineScope(Dispatchers.Default).launch {
            val histories = app.historyDataStore.data.first().histories.toMutableList()
            if (histories.isNotEmpty() && SolverUtils.isToday(histories.last())) {
                var history = histories.removeAt(histories.size - 1)
                val historyData = history.data.toMutableList()
                val datum = historyData.removeAt(index)
                if(current.solution != null){
                    historyData.add(current)
                    history = history.copy(data = historyData)
                }
                histories.add(history)
                mutableState.update { it.copy(current = datum, error = null) }

                app.historyDataStore.updateData {
                    it.copy(histories = histories)
                }
            }
        }
        mutableModes.update { it.copy(key = KeyMode.Normal, hyp = HypMode.Off) }
    }

    fun clearHistory(){
        CoroutineScope(Dispatchers.Default).launch {
            app.historyDataStore.updateData { it.copy(histories = emptyList()) }
        }
    }
}