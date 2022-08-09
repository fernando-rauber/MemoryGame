package uk.fernando.memory.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.memory.database.dao.LevelDao
import uk.fernando.memory.database.entity.LevelEntity


open class LevelRepositoryImpl(private val dao: LevelDao) : LevelRepository {

    override suspend fun insert(list: List<LevelEntity>) {
        withContext(Dispatchers.IO) {
            dao.insert(list)
        }
    }
}
