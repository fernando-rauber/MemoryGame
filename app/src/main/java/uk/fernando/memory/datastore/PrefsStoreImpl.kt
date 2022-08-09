package uk.fernando.memory.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

private const val STORE_NAME = "memory_data_store"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class PrefsStoreImpl(context: Context) : PrefsStore {

    private val dataStore = context.dataStore
    override suspend fun getVersion(): Int {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.VERSION] ?: 1 }.first()
    }

    override fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.DARK_MODE] ?: false }
    }

    override fun isSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.SOUND_ENABLED] ?: true }
    }

    override suspend fun storeVersion(value: Int) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.VERSION] = value }
    }

    override suspend fun storeDarkMode(value: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.DARK_MODE] = value }
    }

    override suspend fun storeSound(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.SOUND_ENABLED] = enabled }
    }

    private object PreferencesKeys {
        val VERSION = intPreferencesKey("version")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    }
}