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

    override suspend fun enableLevel(levelID: Int, categoryId: Int) {
        withContext(Dispatchers.IO) {
            dao.enableLevel(levelID, categoryId)
        }
    }

    override suspend fun update(level: LevelEntity) {
        withContext(Dispatchers.IO) {
            dao.update(level)
        }
    }

    override suspend fun getLevelById(id: Int,categoryId: Int) = withContext(Dispatchers.IO) {
        dao.getLevelById(id, categoryId)
    }

    override suspend fun getCardTypeByCategory(id: Int)= withContext(Dispatchers.IO) {
        dao.getCardTypeByCategory(id)
    }
}
