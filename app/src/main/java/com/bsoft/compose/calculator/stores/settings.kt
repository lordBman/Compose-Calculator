package com.bsoft.compose.calculator.stores

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
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
enum class ThemeMode(val value: String){
    System("system"), Light("light"), Dark("dark")
}

@Serializable
data class Settings(val save: Boolean = false, val dynamicTheme: Boolean = true, val themeMode: ThemeMode = ThemeMode.System)

object SettingsSerializer: Serializer<Settings>{
    override val defaultValue: Settings
        get() = Settings()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Json.decodeFromString<Settings>(input.readBytes().decodeToString())
        }catch (exception: SerializationException){
            throw CorruptionException("Unable to read Settings", exception)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).toByteArray())
        }
    }
}

val Context.settingsDataStore: DataStore<Settings> by dataStore( fileName = "settings.json", serializer = SettingsSerializer)

class SettingsStore(val context: Context) {
    val settings: Flow<Settings>
        get() = context.settingsDataStore.data.map { it }

    suspend fun updateTheme(themeMode: ThemeMode) = context.settingsDataStore.updateData { it.copy(themeMode = themeMode) }
    suspend fun updateSave(save: Boolean) = context.settingsDataStore.updateData { it.copy(save = save) }
    suspend fun updateDynamicTheme(dynamicTheme: Boolean) = context.settingsDataStore.updateData { it.copy(dynamicTheme = dynamicTheme) }
}