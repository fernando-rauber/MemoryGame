package uk.fernando.memory.datastore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    suspend fun getVersion(): Int
    fun isDarkMode(): Flow<Boolean>
    fun isPremium(): Flow<Boolean>
    fun isSoundEnabled(): Flow<Boolean>
    fun getStarCount(): Flow<Int>

    suspend fun storeVersion(value: Int)
    suspend fun storeDarkMode(value: Boolean)
    suspend fun storePremium(value: Boolean)
    suspend fun storeSound(enabled: Boolean)
    suspend fun storeStar(value: Int)
}