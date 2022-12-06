package uk.fernando.memory.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "memory_game_data_store"

val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class GamePrefsStoreImpl(context: Context) : GamePrefsStore {

    private val dataStore = context.gameDataStore

    override suspend fun getBoardSize(): Int {
        return dataStore.data.map { prefs -> prefs[PreferencesKeys.BOARD_SIZE] ?: 5 }.first()
    }

    override suspend fun getCategoryList(): List<Int> {
        return dataStore.data.map { prefs ->
            val operators = prefs[PreferencesKeys.IMAGES]
            if (operators == null)
                listOf(1, 2, 3)
            else if (operators.isNotEmpty())
                operators.split("*").map { it.toInt() }
            else
                emptyList()
        }.first()
    }

    override suspend fun storeBoardSize(value: Int) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.BOARD_SIZE] = value }
    }

    override suspend fun storeCategory(value: List<Int>) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.IMAGES] = value.map { it }.joinToString(separator = "*") }
    }

    private object PreferencesKeys {
        val BOARD_SIZE = intPreferencesKey("board_size")
        val IMAGES = stringPreferencesKey("images")
    }
}