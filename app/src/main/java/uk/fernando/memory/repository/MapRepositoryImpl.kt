package uk.fernando.memory.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.memory.database.dao.MapDao
import uk.fernando.memory.database.entity.MapEntity


open class MapRepositoryImpl(private val dao: MapDao) : MapRepository {

    override suspend fun getMapAndLevelList() = withContext(Dispatchers.IO) {
        dao.getMapAndLevelList()
    }

    override suspend fun insert(map: MapEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(map)
        }
    }
}
