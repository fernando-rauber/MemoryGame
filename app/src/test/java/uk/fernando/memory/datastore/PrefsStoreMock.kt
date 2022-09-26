package uk.fernando.memory.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PrefsStoreMock : PrefsStore {

    private var version = 1
    private var isPremium = false
    private var isSoundEnabled = true
    private var starCount = 5

    override suspend fun getVersion(): Int {
        return version
    }

    override fun isPremium(): Flow<Boolean> {
        return flow { isPremium }
    }

    override fun isSoundEnabled(): Flow<Boolean> {
        return flow { isSoundEnabled }
    }

    override fun getStarCount(): Flow<Int> {
        return flow { starCount }
    }

    override suspend fun storeVersion(value: Int) {
        version = value
    }

    override suspend fun storePremium(value: Boolean) {
        isPremium = value
    }

    override suspend fun storeSound(enabled: Boolean) {
        isSoundEnabled = enabled
    }

    override suspend fun storeStar(value: Int) {
        starCount = value
    }
}