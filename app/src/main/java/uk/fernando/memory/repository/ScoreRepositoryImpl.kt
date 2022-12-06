package uk.fernando.memory.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.memory.database.dao.ScoreDao
import uk.fernando.memory.database.entity.ScoreEntity

class ScoreRepositoryImpl(private val dao: ScoreDao) : ScoreRepository {

    override suspend fun insertHistory(history: ScoreEntity) = withContext(Dispatchers.IO) {
        dao.insert(history).toInt()
    }

    override fun getAllHistory(isMultiplayer: Boolean) = dao.getHistory(isMultiplayer)

}